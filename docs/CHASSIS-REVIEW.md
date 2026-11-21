# What we took from Chassis

Reference repo: `~/work/chassis` (its docs are in Spanish; its API is in Go). AulaTria copies its ideas, not its code. Verdicts: **Copied** as is, **Simplified** (same idea, cheaper form), **Dropped** (does not apply to AulaTria, or is out of scope for the MVP).

## Process and contract

| Area | Chassis | Verdict | AulaTria |
|---|---|---|---|
| Contract | protobuf in `proto/`, buf lint and breaking checks, ConnectRPC, protovalidate | **Copied** | Same stack: `proto/aula/**`, `buf.yaml` depends on protovalidate |
| Contract drift | Generated code committed and checked | **Copied** | `mage proto:check` in CI. It replaces Chassis' contract tests: the contract cannot say anything the code does not |
| Error codes | Specific codes per module, one mapping to the transport, log and map once | **Copied** | `errors.proto` enum; `shared/errors.ts` is the one mapping; unmapped codes are `internal` |
| Error header | `Chassis-Error-Code` | **Copied** (renamed) | `Aula-Error-Code` |
| Schema first | DBML first, one file | **Copied** | `docs/db_schema/db_schema.dbml`, with `mage db:sql` generating `001_init.sql` |
| Derived values | Totals and states are computed, never stored | **Copied** | Counts, percentages, pulse bars and participation are read queries |
| Docs split | Product, architecture, UI flows | **Copied** | Same three, plus the contract guide |
| Skills for contributors | Checklists as Claude skills | **Copied** | `.claude/skills/` (new-table, new-rpc, new-error-code, touch-realtime, verify, and the screen and design-system skills) |
| Task runner | mage | **Copied** | `magefile.go`; `mage -l` lists the tasks |
| Hand-written Kotlin transport | Needed because no Connect client supports Kotlin/Native | **Copied** | `apps/app/shared/src/commonMain/kotlin/org/aula/app/rpc/ConnectTransport.kt` |
| Kotlin messages | pbandk | **Copied** | `pbandk` 0.16.0, pinned to the generator jar in `.tools/` |
| Shared test vectors | `proto/**/*-vectors.json` for rules on both sides | **Dropped** | No client-side duplicate validation is planned |
| OpenAPI, Zod, Hono, Express | Not used by Chassis either | (not adopted) | We did not adopt OpenAPI/Zod: see ARCHITECTURE section 2 |

## Tenancy

| Area | Chassis | Verdict | AulaTria |
|---|---|---|---|
| Tenant on every table | Tenant denormalised into every table (`workshop_id`) | **Copied** (renamed) | `school_id` on every table except the `school` root |
| Composite foreign keys | Yes | **Copied** | `(school_id, x) > parent (school_id, id)` in the DBML |
| Tenant from the actor | Every query scoped by the authenticated actor | **Copied** | `actor(ctx, roles...)` and `a.schoolId` on every query |
| Tenant header | Header checked against the token | **Copied** | `X-School-Id` must equal the token's school (`TENANT_MISMATCH`) |
| Row-level security | Considered | **Dropped** for the MVP | Migration `002` records why. Revisit with the minors' data question |

## Backend

| Area | Chassis | Verdict | AulaTria |
|---|---|---|---|
| CQRS | Write side: use case, entity, repository, one transaction. Read side: hardcoded SQL, no repository | **Copied** | `commands/` (entity + repository) and `queries/` (hardcoded SQL), same rules |
| Read rules | No existence pre-check; access rule in the `WHERE`; a query use case only when it adds logic | **Copied** | `visibleLesson` puts the access rule in the `WHERE` |
| Ports (interfaces) for every repository | Yes | **Dropped** | Concrete repository classes; the domain never imports the database |
| DI container, generic unit of work | Yes | **Simplified** | `main.ts` wires the routes; `inTx(fn)` is the unit of work |
| Domain entities | Aggregates per module | **Simplified** | Entities only where a state machine exists (`Lesson`, `Course`, `User`) |
| Fakes, repository contract suites, mutation testing | Yes | **Dropped** | No tests beyond `mage api:smoke` and the drift check |
| Domain events: publisher, registry, outbox modes | Yes | **Simplified** | One in-process hub (`lessons/hub.ts`) that rebuilds snapshots after each change |
| Outbox, RabbitMQ | Yes | **Dropped** | No consumer needs guaranteed delivery |
| Authorization projection in a separate schema | Yes | **Dropped** | Three fixed roles and ownership checks in each handler |
| Auth: short JWT, rotating refresh token, bcrypt | Yes | **Copied** | `jsonwebtoken`, `refresh_token` table, `bcryptjs` |
| Credentials in Redis | Yes | **Dropped** | Postgres tables (`invitation`, `refresh_token`); no Redis |
| Client-minted ids as idempotency keys | Yes (for offline retries) | **Dropped** | Server mints UUIDv7; the natural keys give idempotency |
| Batch command endpoint, op ids, rejected-changes screen | Yes (offline protocol) | **Dropped** | Online only |
| One command per business fact | Yes | **Copied** | A wizard that touches several facts is several RPCs |
| Soft delete only where there is a lifecycle | Yes | **Copied** | Lesson lifecycle (`lobby`, `live`, `ended`, `cancelled`); pulse is append-only (`pulse_event`) |
| UTC timestamps, additive-first migrations | Yes | **Copied** | `timestamptz`; migrations are plain SQL, applied in order |
| Object storage with a sweeper | Yes | **Simplified** | No object storage. The contract says the server keeps lesson material on disk (`UploadMaterial`, a stub); no sweeper |
| sqlc and squirrel | Yes | **Simplified** | Kysely with generated types |

## Offline and sync

| Area | Chassis | Verdict | AulaTria |
|---|---|---|---|
| PowerSync, sync rules, WAL replication, SQLite replica, SQLDelight | Yes | **Dropped** | Online only; Postgres is the only service |
| Command outbox, silent triggers, upload pump, optimistic overlay | Yes | **Dropped** | Screens wait for the server's answer |
| The idea: the client is a view, the server is the authority | Yes | **Copied** | Clients never decide results |

## Mobile and client

| Area | Chassis | Verdict | AulaTria |
|---|---|---|---|
| Kotlin Multiplatform + Compose Multiplatform | Yes | **Copied** | Android, iOS and wasmJs targets |
| Decompose with serializable configs | Yes | **Copied** | Declared in the build; no navigation code yet |
| MVI-lite: Screen, Component, State, Event per screen | Yes | **Copied** | Planned for `feature/` (empty today) |
| Read-only screens are thin: expose the query, no repository | Yes | **Copied** | The query is a cache `StateFlow` in `QueryClient` |
| Stateless `XxxContent(state)` plus previews | Yes | **Copied** | Design-system gallery first, then screens |
| `:core` / `:shared` split for JVM tests | Yes | **Dropped** | One `shared` module with packages |
| compose-cupertino | Yes | **Dropped** | Custom brand (Nunito, large rounded controls), not iOS styling. Material also dropped |
| Design tokens as named constants | Yes | **Copied** | `AulaTokens.kt` (Kotlin) and `tokens.css` (web) |
| Feature folders by group | Yes | **Simplified** | One folder per screen; grouping decided when the screens exist |
| Token storage in Keychain or Keystore; refresh in the HTTP layer | Yes | **Simplified** | Refresh in the HTTP layer is copied (`ConnectTransport` retries once on `TOKEN_EXPIRED`). Storage is plain preferences for now: SharedPreferences (Android), NSUserDefaults (iOS), localStorage (browser). **Keychain and Keystore are not used yet**: a gap to close before any real launch |
| PDF templates, photo picker, Google sign-in bridges | Yes | **Dropped** | Not in the product |

## What is new in AulaTria

Chassis has no realtime fan-out, no timed rounds, no LLM calls, no push, no geolocation and no multi-form-factor client. Those are designed fresh:

- **Realtime:** one streaming RPC and a fallback poll ([CONTRACT section 6](./CONTRACT.md#6-realtime-and-attendance)).
- **Timed rounds:** deadlines, with the server's clock for countdowns.
- **LLM and location:** adapters are not built yet ([ARCHITECTURE section 10](./ARCHITECTURE.md#10-risks-and-spike-order)).
- **Two form factors:** a web teacher client and a phone student client, with separate design systems that share token names ([design-system/web.md](./design-system/web.md), [design-system/mobile.md](./design-system/mobile.md)).
