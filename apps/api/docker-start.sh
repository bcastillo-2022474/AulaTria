#!/bin/sh
# Container entrypoint: migrations (idempotent), demo seed only if the database is empty, then the server.
set -e
pnpm run migrate:prod
pnpm run seed:prod --if-empty
exec pnpm run start:prod
