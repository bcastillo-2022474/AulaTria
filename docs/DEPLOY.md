# Deploying AulaTria (Railway + `sudoersgt.dev`)

> Status: **pipeline written and tested locally (`docker compose -f docker-compose.deploy-test.yml`), not yet deployed.** Facts about Railway below come from its official docs (read 2026-11-21, URLs at the end);
> costs are my arithmetic from those unit prices, not a quote. Items marked **DECIDE** need the owner.

## 1. What has to be hosted

| Piece | What it is | Needs |
|---|---|---|
| API | Node (`apps/api`), ConnectRPC over plain HTTP, includes the long-lived `WatchLesson` stream | Postgres, `PORT`, `DATABASE_URL`, `JWT_SECRET` |
| Postgres | one database (22 tables), migrations + demo seed | private network only |
| Teacher web | static files (Vite build of `apps/web`) | SPA fallback to `index.html` |
| Student app, browser build | static files (Kotlin/Wasm build of `apps/app`) that make the live demo possible **without installing anything** | SPA fallback, `application/wasm`, compression |
| Student app, native | Android APK / iOS later | the public API URL |

## 2. Recommended architecture: one gateway, three Railway services

```
                       Internet (TLS by Railway)
        aulatria.sudoersgt.dev          estudiantes.aulatria.sudoersgt.dev
                    \                          /
                     v                        v
              ┌──────────────── gateway (Caddy) ─────────────────┐
              │  host aulatria.*       -> teacher web (static)    │
              │  host estudiantes.*    -> student web (static)    │
              │  both:  /api/*         -> api (private network)   │
              └───────────────────────────┬──────────────────────┘
                                          │ http://api.railway.internal:$PORT   (no public domain)
                                     api (Node) ───────── Postgres (private, volume)
```

- **Only the gateway is public.** The API and the database have no public domain.
- **Same-origin API (`/api`)**: both web apps call `/api/...` on their own host, so there is **no CORS** in production. Caddy strips the prefix (`handle_path /api/*`), so the API keeps its root-level Connect paths.
- **Native apps** (Android/iOS) call `https://aulatria.sudoersgt.dev/api`.
- **Streaming**: Caddy proxies with `flush_interval -1` so `WatchLesson` is not buffered.
- **Compression**: Caddy `encode zstd gzip` (the Wasm bundle is several MB).
- **Cache**: hashed assets `immutable`, `index.html` `no-cache`.

### The one thing that depends on your Railway plan: custom domains per service
Railway docs: Trial **1**, Hobby **2**, Pro **20** per service (Free: none). This design needs **2** (teacher host + student host) on the gateway, so **Hobby or Pro**. **DECIDE: which plan holds your "$20 credit"?**
If it is a **Trial** (1 domain, $5 one-time, services stop after 30 days or when it is spent) use **Option B** below.

| | **A. Two hosts (recommended)** | **B. One host, paths** |
|---|---|---|
| URLs | `aulatria.sudoersgt.dev` (teachers), `estudiantes.aulatria.sudoersgt.dev` (students) | `aulatria.sudoersgt.dev/` landing, `/docentes`, `/estudiantes`, `/api` |
| Custom domains needed | 2 | 1 |
| App changes | none beyond config | **base-path support in both SPAs** (React Router `basename` + Vite `base`; Decompose web paths + asset URLs under a prefix): about a day, and the student build has no base-path support today |
| QR for students | short, clean | `.../estudiantes/` |

## 3. DNS (what you configure at the DNS host of `sudoersgt.dev`)

For **each** custom domain Railway shows the exact values in *Service → Settings → Public Networking → + Custom Domain*. **Both records are required** (without the TXT, requests 404 even after the CNAME resolves).

| Type | Name (relative to `sudoersgt.dev`) | Value |
|---|---|---|
| CNAME | `aulatria` | `<target shown by Railway>.up.railway.app` |
| TXT | `_railway-verify.aulatria` | `<value shown by Railway>` |
| CNAME | `estudiantes.aulatria` | `<target shown by Railway>.up.railway.app` |
| TXT | `_railway-verify.estudiantes.aulatria` | `<value shown by Railway>` |

- Leave the apex `sudoersgt.dev` and everything else untouched. TTL 300 s while setting up.
- **If DNS is on Cloudflare: use DNS-only (grey cloud).** Railway's docs: with proxying on, SSL must be "Full" and domains deeper than a first-level subdomain need Cloudflare's paid Advanced Certificate Manager; `estudiantes.aulatria.sudoersgt.dev` is second-level.
- TLS certificates are automatic (Let's Encrypt, renewed by Railway). Expect minutes, not hours, after the records propagate.
- **DECIDE: where is the DNS for `sudoersgt.dev` hosted?** (Cloudflare, the registrar, other.)

## 4. Services and settings

| Service | Source | Settings |
|---|---|---|
| **Postgres** | Railway Postgres template (private by default) | exposes `DATABASE_URL`; keep **no** public TCP proxy; enable Railway backups |
| **api** | this repo, `apps/api/Dockerfile` (to write), built by Railway | vars: `DATABASE_URL=${{Postgres.DATABASE_URL}}`, `JWT_SECRET=<random 48 bytes>`, `NODE_ENV=production`, optional `ANTHROPIC_API_KEY`; `PORT=3000`, `SEED_PASSWORD=<demo password>`; **healthcheck** `GET /healthz`; **no public domain**; Dockerfile path `apps/api/Dockerfile` (variable `RAILWAY_DOCKERFILE_PATH`). The container itself runs migrations, seeds an empty database, then starts (`apps/api/docker-start.sh`) |
| **gateway** | Docker context assembled by CI (`mage deploy:bundle` -> `.deploy/gateway`) and uploaded with `railway up`, because it needs the Wasm build | vars `API_UPSTREAM=http://api.railway.internal:3000`, `STUDENT_HOST=estudiantes.aulatria.sudoersgt.dev`; 2 custom domains; healthcheck `/healthz` (answered by Caddy) |

Railway deprecates config-as-code (`railway.toml`/`railway.json` keep working only **until 2026-12-01**), so settings above are set in the dashboard / variables, not in a committed file.

## 5. Build and deploy pipeline

- The **Wasm build needs Gradle with the Android SDK configured** (the module also has an Android target) and a lot of RAM, which Railway's docs do not promise. So it is built in **GitHub Actions**, not on Railway.
- On every push to `master`: CI builds the teacher web (`VITE_API_URL=/api`, `VITE_STUDENT_APP_URL=https://estudiantes.aulatria.sudoersgt.dev`), the student Wasm distribution, then uploads the gateway context (Caddy + both builds + the Caddyfile) with the Railway CLI (`RAILWAY_TOKEN` project token as a GitHub secret). Workflow: `.github/workflows/deploy.yml` (also runnable by hand: *Actions -> deploy -> Run workflow*).
- The **api** is uploaded by the same workflow (`railway up --service api`; Railway builds `apps/api/Dockerfile`).
- Migrations run at container start (`docker-start.sh`); `seed --if-empty` loads the demo school only when the database has none (the seed script wipes and rebuilds the school, so it must never run blindly on each deploy).

## 6. Code changes needed before the first deploy (DONE on branch `feat/deploy`)

1. **API**: `GET /healthz`; refuse to start with the default `JWT_SECRET` when `NODE_ENV=production`; `seed --if-empty` flag (and `SEED_PASSWORD` env so the public demo password is not the dev one).
2. **API streaming**: send a keepalive on `WatchLesson` every ~30 s (Railway closes requests after 5 min **without data** and after 15 min in total; clients already reconnect, but a keepalive avoids presence flapping).
3. **Teacher web**: production build with `VITE_API_URL=/api` (check that `connect-web` accepts a relative base) and `VITE_STUDENT_APP_URL`.
4. **Student app (Wasm)**: in production the default API is `window.location.origin + "/api"` (rule: host is not `localhost`/an IP); the teacher-app URL for the "wrong app" screen is derived by dropping the leading `estudiantes.` label (today it swaps the port, which only fits local development).
5. **Android release build**: `apiBaseUrl=https://aulatria.sudoersgt.dev/api` for release; cleartext traffic allowed only in debug.
6. **Caddyfile** (new, `deploy/Caddyfile`), `apps/api/Dockerfile`, `deploy/gateway.Dockerfile`, `.github/workflows/deploy.yml`.

## 7. Cost (estimate, Railway unit prices: RAM $10/GB-month, vCPU $20/vCPU-month, egress $0.05/GB, volume $0.15/GB-month)

| Item | Assumption | ≈ $/month |
|---|---|---|
| api | 256 MB, ~0.05 vCPU | 3.5 |
| gateway (Caddy) | 48 MB, negligible CPU | 0.5 |
| Postgres | 300 MB, ~0.05 vCPU, 1 GB volume | 4.2 |
| Egress | ~300 demo visits × ~12 MB Wasm bundle | 0.2 |
| **Total usage** | | **≈ 8–9** |

Hobby ($5/month) includes $5 of usage, so ≈ $8 billed; a Pro plan includes $20 of usage and would cover it. **Cost control**: set a hard limit (minimum $10) on the workspace *Usage* page or `railway usage limit set --target workspace --soft 75 --hard 125`; the soft limit only emails. At the hard limit **all workloads go offline** (do not set it below what the demo needs). After the demo, delete the services or let usage drop.

## 8. Risks and honest unknowns

- **Kotlin/Wasm needs browser WasmGC support** (Chrome/Edge 119+, Firefox 120+, Safari 18.2+; check caniuse before the demo). An older iPhone cannot run the browser build: keep an **APK download link** as a fallback for Android. Compose for Web was **Beta** at last check.
- **Streaming through Railway's edge** is not explicitly documented for server-streaming over HTTP (only "requests up to 15 minutes while data flows"). The app falls back to polling (`GetLessonSnapshot` every 3 s), so the worst case is a few seconds of delay, not a broken screen. **Test it on the real domain before the demo.**
- **Public demo credentials**: the seeded accounts are public by design; use a different `JWT_SECRET` and `SEED_PASSWORD` in production and do not reuse anything from your laptop.
- **Memory on Railway for the API**: Node + Kysely is small; if usage grows the cost rises linearly.
- Not found in the docs: request-body size limit (matters for the 15 MB material upload, which is not implemented), default Postgres CPU/RAM, whether `railway run` can reach the production database from a laptop.

## 9. Order of work (about half a day, plus DNS propagation)

1. Create the Railway project; add Postgres; confirm the plan (domains per service).
2. Code changes in section 6; write the three Docker/Caddy/CI files; test the gateway image locally with `docker run`.
3. Create `api` (GitHub), set variables, first deploy (migrations + seed run in pre-deploy); check `/healthz` through the private network.
4. Create `gateway` from the CI image; add the two custom domains; create the DNS records from section 3.
5. Smoke test on the real domain: teacher login, student login in the browser build, a live lesson with the simulator, back/forward/refresh on both apps, the "wrong app" redirect, streaming vs polling.
6. Set the Railway spending limit.

## Sources (Railway docs)
Pricing: docs.railway.com/reference/pricing/plans · Free/trial: docs.railway.com/pricing/free-plan · Cost control: docs.railway.com/pricing/cost-control · Custom domains: docs.railway.com/networking/domains/working-with-domains · Limits (15 min request, 5 min idle, 32 KB headers): docs.railway.com/networking/public-networking/specs-and-limits · Private networking: docs.railway.com/networking/private-networking · Postgres: docs.railway.com/guides/postgresql · Dockerfiles and build config: docs.railway.com/builds/dockerfiles, docs.railway.com/builds/build-configuration · Pre-deploy command: docs.railway.com/deployments/pre-deploy-command · Healthchecks: docs.railway.com/deployments/healthchecks · CLI/CI token: docs.railway.com/cli · Config as code (deprecation): docs.railway.com/reference/config-as-code

## 10. Runbook: first deploy (what only the owner can do, then what is automated)

1. `railway login` (browser) and, in the dashboard, create project **aulatria**; add **Postgres**; create empty services **api** and **gateway** (names matter: the workflow uses them).
2. Variables, `api`: `DATABASE_URL=${{Postgres.DATABASE_URL}}`, `JWT_SECRET=<openssl rand -hex 32>`, `NODE_ENV=production`, `PORT=3000`, `SEED_PASSWORD=<password for the demo accounts>`, `RAILWAY_DOCKERFILE_PATH=apps/api/Dockerfile`. Gateway: `API_UPSTREAM=http://api.railway.internal:3000`, `STUDENT_HOST=estudiantes.aulatria.sudoersgt.dev`. (The CLI can do all of this: `railway variables --service api --set KEY=value`.)
3. Project token: Project -> Settings -> Tokens (environment `production`), then `gh secret set RAILWAY_TOKEN -R bcastillo-2022474/AulaTria`.
4. First deploy: `gh workflow run deploy -R bcastillo-2022474/AulaTria` (or push to `master`).
5. Gateway -> Settings -> Networking: add both custom domains, create the DNS records of section 3.
6. Smoke test on the real domain (section 9, step 5).

Local rehearsal of the production topology: `mage deploy:bundle && docker compose -f docker-compose.deploy-test.yml up --build`, then use `curl -H "Host: aulatria.localhost" localhost:8088/` (or open `http://aulatria.localhost:8088`, `http://estudiantes.localhost:8088`).
