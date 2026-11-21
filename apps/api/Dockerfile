# API image. Build context = the REPOSITORY ROOT (the API imports the generated contract in packages/rpc):
#   docker build -f apps/api/Dockerfile -t aula-api .
FROM node:24-slim
RUN corepack enable
WORKDIR /app
# Manifests first so the dependency layer is cached until a dependency changes. Every workspace manifest is
# needed for --frozen-lockfile to match, even for packages we do not install.
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
COPY apps/api/package.json apps/api/
COPY apps/web/package.json apps/web/
COPY packages/rpc/package.json packages/rpc/
COPY tools/protoc-gen-aula-kotlin/package.json tools/protoc-gen-aula-kotlin/
RUN pnpm install --frozen-lockfile --filter @aula/api...
COPY packages/rpc packages/rpc
COPY apps/api apps/api
ENV NODE_ENV=production
WORKDIR /app/apps/api
# Applies migrations, loads the demo school only into an EMPTY database, then starts the server.
CMD ["sh", "docker-start.sh"]
