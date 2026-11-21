# Deploying AulaTria (Railway + `sudoersgt.dev`)

Production is ONE Railway service (`app`: the API and Caddy in one container) plus the Railway Postgres, behind one host:

```
https://aulatria.sudoersgt.dev
   /              the landing page            apps/landing   (static HTML)
   /docentes/     the teacher web app         apps/web       (Vite build, base /docentes/)
   /estudiantes/  the student app, browser    apps/app       (Kotlin/Wasm build, no install needed for the demo)
   /api/*         the API                     apps/api       (ConnectRPC, incl. the WatchLesson stream), same origin: no CORS
```

Why one service: the workspace is on Railway's Free/trial plan (**1 custom domain, and a hard cap on services**: a third service was refused with "Free plan resource provision limit exceeded"). Facts about Railway come from its docs (docs.railway.com: pricing/plans, networking/domains, networking/public-networking/specs-and-limits, builds, cli). The trial shows "30 days or $5 left"; at ~$4-5 of usage per month that is about three weeks. If the plan is upgraded later, the same image can be split into gateway + api without touching the apps.

## How it is built

| Piece | File |
|---|---|
| Image: node (API) + Caddy + both web builds | `deploy/app.Dockerfile` |
| Caddy: paths, SPA fallback, `/api` proxy (no buffering for streams), gzip/zstd, cache headers | `deploy/Caddyfile` |
| Entrypoint: migrations, seed only into an EMPTY database, API on `localhost:3000`, Caddy on `$PORT` | `deploy/start.sh` |
| Builds + assembles the Docker context (`.deploy/app`) | `mage deploy:bundle deploy:stage` |
| CI deploy on push to `master` (or by hand) | `.github/workflows/deploy.yml` |

- Railway terminates TLS at its edge and sends plain HTTP to `$PORT`; Caddy listens on HTTP only. **Do not set `PORT` to 3000**: the platform value (`8080`) is for Caddy; the API is started on 3000 by `start.sh`.
- Caddy strips the path prefix (`handle_path`), so the static roots hold the plain `dist` folders. The web apps are built with that base path (`VITE_BASE=/docentes/`; the Wasm app detects `/estudiantes` at runtime); local dev still serves both at `/`.
- `/healthz` goes through Caddy to the API: if node dies the healthcheck fails and Railway restarts the container.
- `WatchLesson` is a long-lived response: Railway closes requests after 5 minutes without data and after 15 in total. The API sends a keepalive snapshot every 30 s and the clients reconnect (and fall back to polling).
- The API refuses to start in production with a weak/default `JWT_SECRET`; the seed refuses to run in production without `SEED_PASSWORD`; `seed --if-empty` makes redeploys safe.

## Railway setup (done through the CLI)

Project `selfless-clarity`, environment `production`, workspace of the owner's Daniel account.

```bash
railway link -p selfless-clarity -e production
railway add -d postgres
railway add -s app -v PORT=8080 -v NODE_ENV=production
railway variables --service app --set 'DATABASE_URL=${{Postgres.DATABASE_URL}}' \
  --set "JWT_SECRET=$(openssl rand -hex 32)" --set "SEED_PASSWORD=<password for the demo accounts>"
railway domain aulatria.sudoersgt.dev --service app     # prints the two DNS records below
```

Deploy by hand: `mage deploy:bundle deploy:stage && railway up --ci --no-gitignore --service app .deploy/app`.
Deploy from CI: add the secret `RAILWAY_TOKEN` (a PROJECT token: Railway project -> Settings -> Tokens) with `gh secret set RAILWAY_TOKEN`, then push to `master` or run the `deploy` workflow.

## DNS (name.com)

| Type | Host | Answer |
|---|---|---|
| CNAME | `aulatria` | the target `railway domain` printed (`0hglbyrf.up.railway.app`) |
| TXT | `_railway-verify.aulatria` | the value it printed (`railway-verify=...`) |

Both records are required (without the TXT the host answers 404 even when the CNAME resolves). Replace any old CNAME for `aulatria` (it used to point at GitHub Pages for the old landing); a name can have only one. Leave the apex `A` records and `www` alone. If DNS is ever moved to Cloudflare, keep these records DNS-only (grey cloud). TLS certificates are automatic (Let's Encrypt). Check: `railway domain status <id>` -> `Verified: yes`.

## Cost control and risks

- Set a spending limit: `railway usage limit set --target workspace --soft 4 --hard 10` (minimum hard limit is $10; at the hard limit all workloads go offline). Free/trial: services stop after 30 days or when the $5 is spent.
- **Kotlin/Wasm needs a browser with WasmGC** (Chrome/Edge 119+, Firefox 120+, Safari 18.2+). An older iPhone cannot run the browser build; the Android release APK (`mage` builds it with `:androidApp:assembleRelease`, signed with the debug key) is the fallback.
- **Streaming through Railway's edge** is not explicitly documented for server-streaming over HTTP (only "requests up to 15 minutes while data flows"). The polling fallback bounds the damage to a few seconds of delay. Test on the real domain before the demo.
- The seeded accounts are public by design (fake school, fake people): use a non-dev `SEED_PASSWORD`, which the production seed enforces.
- Not found in Railway's docs: request-body size limit, default Postgres CPU/RAM.

## Local rehearsal of the production image

```bash
mage deploy:bundle deploy:stage
docker build -t aula-app .deploy/app
docker run --rm -p 8088:8080 -e PORT=8080 -e NODE_ENV=production -e JWT_SECRET=$(openssl rand -hex 32) \
  -e SEED_PASSWORD=x --add-host=host.docker.internal:host-gateway \
  -e DATABASE_URL=postgres://aula:aula@host.docker.internal:5440/aula aula-app
# http://localhost:8088/  /docentes/  /estudiantes/  /api/...
```
