# AulaTria: rules for every contributor (human or agent)

Classroom companion: live pulse, doubts, QR attendance, floor queue and quizzes. Teacher/admin = web, student = phone
(Compose Multiplatform, also runnable in the browser for demos). Read `docs/README.md` for the map.

## The order of work is the architecture
**DBML → proto (contract) → generate → API handler → client screens.** Nobody writes a handler or a screen against
a shape that is not in the contract. Use the skills in `.claude/skills/` for every kind of change; they are strict on purpose.

| I want to... | Skill |
|---|---|
| add or change a table | `new-table` |
| add or change an RPC (feature backend) | `new-rpc` |
| add a domain error code | `new-error-code` |
| build a teacher/admin screen | `new-web-screen` |
| build a student screen | `new-mobile-screen` |
| add or change a design-system component or token | `change-design-system` |
| change what is pushed live | `touch-realtime` |
| check my work before saying "done" | `verify` |

## Deliberate shortcuts (this is a prototype; do NOT "fix" these, and do NOT reintroduce what was dropped)
- **No ports/interfaces, no DI container, no fakes, no tests, no contract tests.** Repositories are concrete classes, wired by plain construction inside the use case. The seam we keep: domain entities never touch I/O.
- **No offline, no sync, no outbox/RabbitMQ/Redis, no RLS.** Online only. Postgres is the only service.
- Realtime is **one server-streaming RPC** (`WatchLesson`). The server pushes full snapshots; clients replace their cache. Polling (`refetch_seconds`) is the fallback.

## Rules that are NOT shortcuts (never break these)
1. **Tenant.** Every table has `school_id`. Every query takes the tenant from the authenticated actor (`a.schoolId`) and filters by it on every table it touches, joins included. Inserts set it. The client sends `X-School-Id`; the auth interceptor rejects a mismatch. Never take a tenant id from a request message.
2. **The contract is frozen: additive changes only.** New fields (new numbers), new RPCs, new enum values, new error codes are fine. Renaming, renumbering, removing or retyping anything is BREAKING: `mage proto:breaking` (also in `mage fast` and CI) fails, and it needs the owner's explicit OK. Before adding anything, check the flow in `docs/UI-FLOWS.md` really lacks it, and say in the PR why the contract was missing it.
2b. **Contract is the source.** Never hand-write or edit generated code (`packages/rpc/src/gen`, `packages/rpc/src/hints`, `apps/app/shared/**/org/aula/app/rpc/**` except `ConnectTransport/RpcTransport/ConnectError`). Change the `.proto`, run `mage proto:gen`.
3. **DBML first.** `docs/db_schema/db_schema.dbml` is the schema's source of truth. A migration without a DBML change (or the reverse) is a bug.
4. **Errors are specific codes.** Domain refusals are `AppError(ErrorCode.X)` with a code declared in `errors.proto`; clients switch on the code, never on messages. Unknown errors are `internal` and leak nothing.
5. **CQRS, strictly.** WRITES (`commands/`) use a domain ENTITY (`domain/`, pure, owns the rules) loaded and saved through a concrete REPOSITORY (`repository.ts`, no interface, built with the transaction: `inTx(trx => new XRepository(trx)...)`). READS (`queries/`) are SPECIALIZED queries hardcoded in the use case (Kysely) that return the response message directly and **never use repositories or entities**. A repository has no list/count/aggregate methods; a query has no entity. `service.ts` is the thin role table (`actor(ctx, "teacher")`) and holds no logic. Reference: `apps/api/src/modules/lessons`.
   A student never receives teacher-only data: project it away in the query/command response.
6. **Design tokens only.** No raw colours/radii/font sizes outside the token files (web: `tokens.css`, mobile: `AulaTokens.kt`). New component = gallery first. Fidelity to `docs/design` is the bar.
7. **Screens never talk HTTP.** Web: `useAulaQuery` / `useAulaMutation` / `useLessonStream` from `apps/web/src/rpc/hooks.ts`. Mobile: the generated `Query`/`Mutation` descriptors through `QueryClient`. Never invalidate by hand: it comes from the proto `invalidates` option.
8. **Language:** English identifiers and comments; Spanish for anything a user reads.
9. **Locality of behaviour:** a feature's logic lives in one place (backend: one file per use case, in `commands/` or `queries/`, next to its module's entity and repository; web: one folder per area; mobile: one folder per screen with Screen/Component/State/Event). Do not create layers "for symmetry".

## Commands
`mage -l` lists everything. Daily: `mage db:up db:migrate db:seed api:dev` + `mage web:dev` (design system at `/#/design-system`) + `mage app:web`.
Before "done": `mage fast` (contract drift, api typecheck, web build) and, if you touched the app, `mage app:check`. See the `verify` skill.

## Demo data
`mage db:seed` (idempotent). Teacher `ana.morales@sanmarcos.edu.gt`, student `marta.ramirez@sanmarcos.edu.gt` (+37 more), admin `admin@sanmarcos.edu.gt`, all password `demo1234`. Course join code `BD2-4K9P`. Lesson 12 does not exist: the demo opens it live.
