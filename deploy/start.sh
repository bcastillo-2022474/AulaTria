#!/bin/sh
# Container entrypoint: migrations (idempotent), demo seed only into an EMPTY database, the API on localhost:3000 in the
# background, Caddy in the foreground on $PORT. Caddy's /healthz goes through to the API, so a dead API fails the healthcheck.
set -e
cd /app/apps/api
pnpm run migrate:prod
pnpm run seed:prod --if-empty
# One-shot ops switch: RESET_PASSWORDS=1 sets every account to $SEED_PASSWORD (remove the variable afterwards).
[ "$RESET_PASSWORDS" = "1" ] && pnpm run passwords:prod
PORT=3000 pnpm run start:prod &
exec caddy run --config /etc/caddy/Caddyfile
