//go:build mage

// Workspace tasks. Run `mage -l` for the list. Everything that builds, generates or verifies the
// repo is reachable from here, so CI and a laptop run the same commands.
package main

import (
	"bufio"
	"context"
	"crypto/sha256"
	"encoding/hex"
	"errors"
	"fmt"
	"io"
	"io/fs"
	"net"
	"os"
	"os/exec"
	"os/signal"
	"path/filepath"
	"sort"
	"sync"
	"syscall"

	"github.com/magefile/mage/mg"
	"github.com/magefile/mage/sh"
)

// Keep in sync with `pbandk` in apps/app/gradle/libs.versions.toml: the runtime must match the
// generator that produced the committed Kotlin.
const pbandkVersion = "0.16.0"

// Everything `buf generate` writes. Committed, and `mage proto:check` fails when it is stale.
var generatedDirs = []string{
	"packages/rpc/src/gen",
	"packages/rpc/src/hints",
	"apps/web/src/rpc/gen",
	"apps/app/shared/src/commonMain/kotlin/org/aula/app/rpc",
}

// ---------------------------------------------------------------- contract

type Proto mg.Namespace

// Gen lints the contract and regenerates every binding: TypeScript (api + web), Kotlin messages
// (pbandk) and the Kotlin clients/queries (tools/protoc-gen-aula-kotlin).
func (Proto) Gen() error {
	if err := ensurePbandk(); err != nil {
		return err
	}
	if err := inDir("proto", "buf", "lint"); err != nil {
		return err
	}
	// The Kotlin plugin reads the proto options through its own copy of options.proto's descriptor.
	if err := inDir("proto", "buf", "generate", "--template", "buf.gen.tools.yaml", "--path", "aula/options"); err != nil {
		return err
	}
	return inDir("proto", "buf", "generate")
}

// Check regenerates and fails if the committed generated code was stale (the drift check that replaces
// Chassis' contract tests: the contract cannot say anything the code does not).
func (Proto) Check() error {
	before, err := hashDirs(generatedDirs)
	if err != nil {
		return err
	}
	if err := (Proto{}).Gen(); err != nil {
		return err
	}
	after, err := hashDirs(generatedDirs)
	if err != nil {
		return err
	}
	if before != after {
		return errors.New("generated code was out of date; it has been regenerated, commit the result (mage proto:gen)")
	}
	fmt.Println("generated code is up to date")
	return nil
}

// Breaking fails when the contract changed in a way that breaks an existing client: a renamed, renumbered or removed
// field/RPC/enum value, or a changed type. ADDITIVE changes (a new field, RPC or enum value) pass. The contract is
// frozen: this is the guard. It compares against the last commit; set BREAKING_AGAINST to compare with another ref,
// e.g. BREAKING_AGAINST='.git#branch=main,subdir=proto' (CI does that for pull requests).
func (Proto) Breaking() error {
	against := os.Getenv("BREAKING_AGAINST")
	if against == "" {
		against = "../.git#subdir=proto"
	} else {
		against = "../" + against
	}
	return inDir("proto", "buf", "breaking", "--against", against)
}

// ensurePbandk writes the shim that runs protoc-gen-pbandk (a jar). The jar is committed under .tools/.
func ensurePbandk() error {
	jar := fmt.Sprintf(".tools/protoc-gen-pbandk-%s.jar", pbandkVersion)
	if _, err := os.Stat(jar); err != nil {
		return fmt.Errorf("%s is missing: download pro.streem.pbandk:protoc-gen-pbandk-jvm:%s (jvm8 jar) from Maven Central into .tools/: %w", jar, pbandkVersion, err)
	}
	if _, err := exec.LookPath("java"); err != nil {
		return errors.New("java is required to generate Kotlin messages (protoc-gen-pbandk is a jar)")
	}
	return nil
}

// ---------------------------------------------------------------- database

type Db mg.Namespace

// Up starts Postgres (docker compose), waiting until it is healthy.
func (Db) Up() error { return sh.RunV("docker", "compose", "up", "-d", "--wait", "postgres") }

// Down stops Postgres, keeping its data.
func (Db) Down() error { return sh.RunV("docker", "compose", "down") }

// Reset deletes the database volume and starts a clean one.
func (Db) Reset() error {
	if err := sh.RunV("docker", "compose", "down", "-v"); err != nil {
		return err
	}
	return (Db{}).Up()
}

// Sql regenerates migrations/001_init.sql from the DBML. Only for the first migration: later changes are
// hand-written migrations, and the DBML is updated in the same change.
func (Db) Sql() error {
	return sh.RunV("pnpm", "exec", "dbml2sql", "docs/db_schema/db_schema.dbml", "--postgres", "-o", "apps/api/migrations/001_init.sql")
}

// Migrate applies pending migrations.
func (Db) Migrate() error { return pnpm("--filter", "@aula/api", "migrate") }

// Types regenerates the Kysely types from the migrated database.
func (Db) Types() error { return pnpm("--filter", "@aula/api", "db:types") }

// Seed wipes and rebuilds the demo school (teacher, 38 students, 11 past lessons...).
func (Db) Seed() error { return pnpm("--filter", "@aula/api", "seed") }

// ---------------------------------------------------------------- api

type Api mg.Namespace

// Dev runs the API with live reload (needs Db.Up + Db.Migrate first).
func (Api) Dev() error { return pnpm("--filter", "@aula/api", "dev") }

// Check typechecks the API.
func (Api) Check() error { return pnpm("--filter", "@aula/api", "typecheck") }

// Smoke runs the end-to-end check against a running API on a freshly seeded database: auth, tenant
// header, error codes, role scoping and the realtime stream, through the generated TS clients.
func (Api) Smoke() error { return pnpm("--filter", "@aula/api", "smoke") }

// ---------------------------------------------------------------- web (teacher / admin)

type Web mg.Namespace

// Dev runs the web app on :5173 (the design system is at /#/design-system).
func (Web) Dev() error { return pnpm("--filter", "@aula/web", "dev") }

// Check typechecks and builds the web app.
func (Web) Check() error { return pnpm("--filter", "@aula/web", "build") }

// ---------------------------------------------------------------- app (Compose: student phone + browser demo)

type App mg.Namespace

// Check compiles what this host can: the browser (wasmJs) target and the Android debug app. iOS
// targets are skipped by Gradle on non-macOS hosts and are only compiled in CI on a Mac.
func (App) Check() error {
	return inDir("apps/app", "./gradlew", ":shared:compileKotlinWasmJs", ":androidApp:assembleDebug")
}

// Web serves the student app in the browser (the live demo without installing anything).
func (App) Web() error { return inDir("apps/app", "./gradlew", ":shared:wasmJsBrowserDevelopmentRun") }

// Install builds the debug app and installs it on the phone adb sees.
func (App) Install() error {
	if err := inDir("apps/app", "./gradlew", ":androidApp:assembleDebug"); err != nil {
		return err
	}
	return sh.RunV("adb", "install", "-r", "apps/app/androidApp/build/outputs/apk/debug/androidApp-debug.apk")
}

// ---------------------------------------------------------------- everything

// Setup prepares a machine to run everything: JS dependencies, Postgres, the schema and the demo data. Idempotent: run
// it as often as you like (the seed rebuilds the demo school). Then start the three processes (see docs/RUNNING.md):
// `mage api:dev`, `mage web:dev`, `mage app:web`.
func Setup() error {
	mg.SerialDeps(Install, Db.Up, Db.Migrate, Db.Seed)
	return nil
}

// Ip prints this machine's address on the network and the URLs to use from a PHONE or another computer. Do not use
// `hostname -I`: with Docker running it lists dozens of bridge addresses, and the first one is not always the right one.
func Ip() error {
	conn, err := net.Dial("udp", "1.1.1.1:80") // no packet is sent: it only asks the OS which address routes outward
	if err != nil {
		return fmt.Errorf("no network route: %w", err)
	}
	defer conn.Close()
	ip := conn.LocalAddr().(*net.UDPAddr).IP.String()
	fmt.Printf("This machine on the network: %s\n\n", ip)
	fmt.Printf("  API                     http://%s:3000\n  teacher web             http://%s:5173\n  student app (browser)   http://%s:8080\n\n", ip, ip, ip)
	fmt.Printf("Android phone/emulator: put in apps/app/local.properties   apiBaseUrl=http://%s:3000   then `mage app:install`\n", ip)
	fmt.Printf("iOS: Configuration/Local.xcconfig   API_BASE_URL=http:/$()/%s:3000\n", ip)
	fmt.Println("The phone must be on the same Wi-Fi (some networks isolate clients: then use a hotspot).")
	return nil
}

// Dev runs the API (:3000), the teacher web app (:5173) and the student app's browser build (:8080) TOGETHER, with
// prefixed output. Ctrl-C stops all three. Needs Setup first (Postgres + schema + demo data). One command, so it
// also avoids starting several `mage` processes at once (they race compiling the magefile).
func Dev() error {
	type proc struct {
		name, dir, cmd string
		args           []string
	}
	procs := []proc{
		{"api", ".", "pnpm", []string{"--filter", "@aula/api", "dev"}},
		{"web", ".", "pnpm", []string{"--filter", "@aula/web", "dev"}},
		{"app", "apps/app", "./gradlew", []string{":shared:wasmJsBrowserDevelopmentRun"}},
	}
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()
	var wg sync.WaitGroup
	for _, p := range procs {
		c := exec.CommandContext(ctx, p.cmd, p.args...)
		c.Dir = p.dir
		// Own process group, and stop the WHOLE group: `pnpm` and `gradlew` are wrappers whose children (node, java) would
		// otherwise survive Ctrl-C and keep holding the ports.
		c.SysProcAttr = &syscall.SysProcAttr{Setpgid: true}
		c.Cancel = func() error { return syscall.Kill(-c.Process.Pid, syscall.SIGINT) } // SIGINT = what a terminal Ctrl-C sends: Gradle then cancels the build in its daemon (which owns webpack)
		out, _ := c.StdoutPipe()
		c.Stderr = c.Stdout
		if err := c.Start(); err != nil {
			stop()
			return fmt.Errorf("%s: %w", p.name, err)
		}
		wg.Add(1)
		go func(name string, cmd *exec.Cmd, out io.Reader) {
			defer wg.Done()
			sc := bufio.NewScanner(out)
			sc.Buffer(make([]byte, 1<<20), 1<<20)
			for sc.Scan() {
				fmt.Printf("[%s] %s\n", name, sc.Text())
			}
			// Wait only after the output is drained (the docs: Wait closes the pipe).
			if err := cmd.Wait(); err != nil && ctx.Err() == nil {
				fmt.Printf("[%s] exited: %v\n", name, err)
			}
		}(p.name, c, out)
	}
	fmt.Println("[dev] api http://localhost:3000 | teacher web http://localhost:5173 | student app (browser) http://localhost:8080 (first build takes a minute). From a phone/another computer: `mage ip`")
	<-ctx.Done()
	wg.Wait()
	return nil
}

// Install installs JS dependencies for the workspace.
func Install() error { return pnpm("install") }

// Check is what CI runs: contract drift, migrations on a throwaway database are NOT run here (they need
// Postgres: see CI), then typecheck/build of the api, web and the Compose app.
func Check() error {
	mg.SerialDeps(Install, Proto.Check, Proto.Breaking, Api.Check, Web.Check, App.Check)
	return nil
}

// Fast is Check without the slow Gradle build: contract, api and web.
func Fast() error {
	mg.SerialDeps(Install, Proto.Check, Proto.Breaking, Api.Check, Web.Check)
	return nil
}

// ---------------------------------------------------------------- helpers

func pnpm(args ...string) error { return sh.RunV("pnpm", args...) }

func inDir(dir, cmd string, args ...string) error {
	c := exec.Command(cmd, args...)
	c.Dir = dir
	c.Stdout, c.Stderr, c.Stdin = os.Stdout, os.Stderr, os.Stdin
	return c.Run()
}

// hashDirs fingerprints every file under dirs (path + content), so drift can be detected without git.
func hashDirs(dirs []string) (string, error) {
	var files []string
	for _, d := range dirs {
		err := filepath.WalkDir(d, func(p string, e fs.DirEntry, err error) error {
			if err != nil {
				if errors.Is(err, fs.ErrNotExist) {
					return nil
				}
				return err
			}
			if !e.IsDir() {
				files = append(files, p)
			}
			return nil
		})
		if err != nil {
			return "", err
		}
	}
	sort.Strings(files)
	h := sha256.New()
	for _, f := range files {
		b, err := os.ReadFile(f)
		if err != nil {
			return "", err
		}
		h.Write([]byte(f))
		h.Write(b)
	}
	return hex.EncodeToString(h.Sum(nil)), nil
}

// Deploy namespace: everything to ship to Railway (see docs/DEPLOY.md).
type Deploy mg.Namespace

// Bundle builds both web apps for production (teacher web under /docentes/, student Wasm distribution). `deploy:stage` packages them.
// Teacher web: same-origin API (/api). Student app: Kotlin/Wasm distribution.
func (Deploy) Bundle() error {
	teacherHost := envOr("TEACHER_HOST", "aulatria.sudoersgt.dev")
	if err := sh.RunWithV(map[string]string{
		"VITE_BASE":            "/docentes/",
		"VITE_API_URL":         "/api",
		"VITE_STUDENT_APP_URL": "https://" + teacherHost + "/estudiantes/",
	}, "pnpm", "--filter", "@aula/web", "build"); err != nil {
		return err
	}
	if err := inDir("apps/app", "./gradlew", ":shared:wasmJsBrowserDistribution"); err != nil {
		return err
	}
	return nil
}

func envOr(k, d string) string {
	if v := os.Getenv(k); v != "" {
		return v
	}
	return d
}

// Stage assembles the production image's build context in .deploy/app (run `mage deploy:bundle` first): the API sources
// and manifests, both web builds under site/, the Caddyfile, start.sh and the Dockerfile.
func (Deploy) Stage() error {
	out := ".deploy/app"
	if err := os.RemoveAll(out); err != nil {
		return err
	}
	cmds := [][]string{
		{"mkdir", "-p", out + "/apps/web", out + "/packages", out + "/tools/protoc-gen-aula-kotlin", out + "/site"},
		{"cp", "package.json", "pnpm-lock.yaml", "pnpm-workspace.yaml", out},
		{"cp", "-r", "apps/api", out + "/apps/api"},
		{"cp", "apps/web/package.json", out + "/apps/web/"},
		{"cp", "-r", "packages/rpc", out + "/packages/rpc"},
		{"cp", "tools/protoc-gen-aula-kotlin/package.json", out + "/tools/protoc-gen-aula-kotlin/"},
		{"cp", "-r", "apps/web/dist", out + "/site/teacher"},
		{"cp", "-r", "apps/app/shared/build/dist/wasmJs/productionExecutable", out + "/site/student"},
		{"cp", "-r", "apps/landing", out + "/site/landing"},
		{"rm", "-f", out + "/site/landing/README.md"},
		{"cp", "deploy/Caddyfile", "deploy/start.sh", out},
		{"cp", "deploy/app.Dockerfile", out + "/Dockerfile"},
		{"rm", "-rf", out + "/apps/api/node_modules", out + "/packages/rpc/node_modules", out + "/apps/api/.env"},
	}
	for _, c := range cmds {
		if err := sh.RunV(c[0], c[1:]...); err != nil {
			return err
		}
	}
	fmt.Println("production image context ready in", out)
	return nil
}
