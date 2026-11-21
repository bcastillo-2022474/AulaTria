# AulaTria

> Tu clase tiene más que decir.

A classroom companion. During a class, students tap *voy bien / me perdí*, write doubts, check in to attendance, raise a hand and answer quizzes from their phone. The teacher sees the class's state live on a laptop and reviews it afterwards.

**Status: skeleton.** The contract, the infrastructure and the reference modules (identity, courses, lessons) are done. Screens and most handlers are built by contributors. What works today and what comes next: [docs/MVP.md](docs/MVP.md).

## Quick start

Prerequisites: Node 24, pnpm 10, Go (for `mage`), `buf` on your PATH, Java 21, Docker.

```bash
pnpm install                              # JS workspace (same as: mage install)
cp apps/api/.env.example apps/api/.env    # skip if you already have one
mage db:up db:migrate db:seed             # Postgres 17 on :5440, schema, demo school
mage api:dev                              # terminal 1: API on :3000
mage web:dev                              # terminal 2: teacher web on :5173
mage app:web                              # terminal 3: student app in the browser
```

Before you call something done: `mage fast` (add `mage app:check` if you touched the app). Demo logins are in [CLAUDE.md](CLAUDE.md#demo-data). `mage -l` lists every task.

## Repo map

| Path | What it is |
|---|---|
| `apps/api` | Backend: TypeScript, `node:http` with ConnectRPC, Kysely on Postgres |
| `apps/web` | Teacher and admin web: React 19, Vite, Tailwind v4 |
| `apps/app` | Student app: Kotlin Multiplatform + Compose (Android, iOS, browser demo) |
| `proto` | The API contract (protobuf, linted by buf). Everything else is generated from it |
| `packages/rpc` | Generated TypeScript for the contract. Do not edit |
| `tools/protoc-gen-aula-kotlin` | Our generator for the Kotlin clients and query layer |
| `docs` | Product, MVP, architecture, contract, UI flows, design systems, DBML schema |
| `.claude/skills` | Step-by-step skills for contributors (new table, RPC, error code...) |
| `magefile.go` | All tasks (`mage -l`) |

Start here: [docs/README.md](docs/README.md) (index of every document), [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) and [docs/CONTRACT.md](docs/CONTRACT.md). The rules every contributor follows are in [CLAUDE.md](CLAUDE.md). Logos will go in `docs/brand/` (not added yet).
