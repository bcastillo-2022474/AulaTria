# THE production image: the API (node) + Caddy serving both web builds, in one container. Build context = the folder
# assembled by `mage deploy:stage` (.deploy/app): the repo's API sources, `site/teacher`, `site/student`, Caddyfile, start.sh.
FROM caddy:2-alpine AS caddy

FROM node:24-slim
RUN corepack enable
COPY --from=caddy /usr/bin/caddy /usr/local/bin/caddy
WORKDIR /app
# Manifests first so the dependency layer is cached until a dependency changes (every workspace manifest is needed for
# --frozen-lockfile to match, even for packages we do not install).
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
COPY apps/api/package.json apps/api/
COPY apps/web/package.json apps/web/
COPY packages/rpc/package.json packages/rpc/
COPY tools/protoc-gen-aula-kotlin/package.json tools/protoc-gen-aula-kotlin/
RUN pnpm install --frozen-lockfile --filter @aula/api...
COPY packages/rpc packages/rpc
COPY apps/api apps/api
COPY site/teacher /srv/teacher
COPY site/student /srv/student
COPY Caddyfile /etc/caddy/Caddyfile
COPY start.sh /app/start.sh
ENV NODE_ENV=production
CMD ["sh", "/app/start.sh"]
