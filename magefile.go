//go:build mage

// Workspace tasks. Run `mage -l` for the list. Everything that builds, generates or verifies the
// repo is reachable from here, so CI and a laptop run the same commands.
package main

import (
	"crypto/sha256"
	"encoding/hex"
	"errors"
	"fmt"
	"io/fs"
	"os"
	"os/exec"
	"path/filepath"
	"sort"

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

// Install installs JS dependencies for the workspace.
func Install() error { return pnpm("install") }

// Check is what CI runs: contract drift, migrations on a throwaway database are NOT run here (they need
// Postgres: see CI), then typecheck/build of the api, web and the Compose app.
func Check() error {
	mg.SerialDeps(Install, Proto.Check, Api.Check, Web.Check, App.Check)
	return nil
}

// Fast is Check without the slow Gradle build: contract, api and web.
func Fast() error {
	mg.SerialDeps(Install, Proto.Check, Api.Check, Web.Check)
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
