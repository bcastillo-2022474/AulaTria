# AulaTria: Architecture

How the system is built today and why. The contract itself is in [CONTRACT.md](./CONTRACT.md); the schema is in [db_schema/db_schema.dbml](./db_schema/db_schema.dbml); the rules every change follows are in [CLAUDE.md](../CLAUDE.md).

## 1. Principles

1. **The order of work is the architecture.** DBML, then proto, then generate, then handler, then screen. Nobody writes a handler or a screen against a shape that is not in the contract.
2. **The server is the authority.** Clients are views. Quiz correctness, attendance status, turn order and counts are computed on the server. A student never receives data a student may not see: the handler projects it away.
3. **Tenant on every row.** `school_id` on every table, composite foreign keys, and every query scoped by the school of the authenticated actor (section 4).
4. **Online only.** Postgres is the only service. No offline mode, no queue, no broker.
5. **Prototype shortcuts, on purpose.** No DI container, no ports or interfaces, no fakes, no unit tests. Domain rules are plain functions that never touch I/O. Do not reintroduce what was dropped (see CLAUDE.md).

## 2. Stack and decisions

| Concern | Decision | Rejected, and why |
|---|---|---|
| Contract | **protobuf** in `proto/`, linted and breaking-checked by buf. The only hand-written description of the API | **OpenAPI + Zod**: two sources (the spec and the validators) that can disagree. A `.proto` compiles to TypeScript and Kotlin from one place |
| RPC protocol | **ConnectRPC**: unary and server-streaming calls over plain HTTP. `connect-node` on the server, `connect-web` in the browser, a small hand-written Kotlin transport (no Connect client supports Kotlin/Native) | **gRPC**: browsers need a gRPC-Web proxy. **REST**: we have no third-party consumers, so hand-routing buys nothing |
| Validation | **protovalidate** rules in the `.proto`, applied by an interceptor | **Zod on the server**: a second copy of every rule |
| Realtime | **One server-streaming RPC**, `LessonsService.WatchLesson` (section 6). Polling as the fallback | **WebSocket**: writes are already unary RPCs, so a bidirectional socket adds a second protocol with its own auth and tenant path. **SSE**: same objection, and the Connect stream already passes through the same interceptors and generated clients |
| Backend runtime | **TypeScript on Node 24**, `node:http` with `connectNodeAdapter` as the router, three interceptors (errors, validation, auth) as the middleware | **Express / Hono**: a router and middleware stack around an adapter that already routes. **Go** (Chassis' stack): one language for the server, the web client and the generator plugins |
| Database | **Postgres 17**, **Kysely** (typed SQL builder, types generated from the database) | An ORM that hides the SQL: the queries are the read side, and they should be visible |
| Schema | **DBML** is the design. `001_init.sql` is generated from it; later migrations are hand-written in the same change as the DBML | A schema that lives only in migrations drifts from its documentation (CLAUDE.md rule 3) |
| Tenancy | `school_id` + composite foreign keys + actor scoping + tenant header check | **Row-level security**: considered and dropped for the MVP (migration `002` says so). Revisit with the minors' data question (CONTRACT section 8) |
| Offline and sync | None. Online only | **PowerSync** and a SQLite replica (the Chassis design): needs `wal_level=logical` and a sync service. Out of MVP scope |
| Events and fan-out | **In-process hub** (`modules/lessons/hub.ts`) | **RabbitMQ with an outbox, Redis**: no consumer needs guaranteed delivery. Postgres `LISTEN/NOTIFY` is the upgrade path, and only `hub.ts` changes |
| Teacher and admin web | React 19, Vite, Tailwind v4, `@connectrpc/connect-query` with TanStack Query, `react-router` in the dependencies | Raw `fetch` per screen: no typed calls, no generated invalidation |
| Student app | Kotlin Multiplatform + Compose Multiplatform (Android, iOS, wasmJs), Ktor client, pbandk messages, Decompose (declared, not yet used) | **Connect's Kotlin client**: no Kotlin/Native support. **Material**: the designs are custom (see `design-system/mobile.md`) |
| Local infrastructure | Docker Compose for Postgres only | Anything more is not needed for the demo |
| Tasks | **mage** (Go task runner, as in Chassis). One entry point for contract, database, API, web and app | npm scripts alone: no single command spans proto, Kotlin and the database |

## 3. Backend

### 3.1 Layout

```
apps/api/src/
  main.ts              router: one line per service; interceptor order
  shared/
    errors.ts          AppError -> Connect error (one mapping table)
    context.ts         auth interceptor, actor(ctx, roles...), Actor type
    tx.ts              inTx(fn): one transaction per command
    ids.ts             UUIDv7 ids (minted by the server)
  db/                  Kysely instance and generated types
  modules/<module>/
    service.ts         the role table: RPC -> actor(ctx, roles) -> use case
    commands/          writes: one file per use case
    queries/           reads: one file per screen-shaped query
    domain/            pure rules (state machines), no I/O
    repository.ts      write-side persistence (a concrete class, no interface)
```

Reference modules: `identity`, `courses`, `lessons`. The others (`doubts`, `quizzes`, `floor`, `attendance`, `followup`, and the admin part of `identity`) have the contract and typed stubs that answer `unimplemented`. Their repositories are added together with each implementation.

### 3.2 CQRS, strictly

| | Writes (`commands/`) | Reads (`queries/`) |
|---|---|---|
| Loads | A domain entity, through a repository | Nothing shared: hardcoded SQL that returns the shape the screen needs |
| Changes | Through the entity's methods, which enforce the rules | Nothing |
| Persists | Through the repository, inside `inTx` | Never |
| Access rule | First line: `actor(ctx, "teacher")` in `service.ts`. Ownership is part of the lookup | Part of the `WHERE` clause (for example `visibleLesson`) |

`service.ts` is deliberately thin. It is the role table of the module and nothing else.

Abridged from the reference module (`apps/api/src/modules/lessons`):

```ts
// service.ts: the role table
openRoom: (req, ctx) => openRoom(actor(ctx, "teacher"), req),

// commands/openRoom.ts: write = entity + repository, one transaction
const lesson = await inTx(async (trx) => {
  const course = await new CourseRepository(trx).findForTeacher(a.schoolId, a.userId, req.courseId);
  if (!course) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
  const lessonsRepo = new LessonRepository(trx);
  const lesson = Lesson.open({ schoolId: a.schoolId, courseId: course.id, number: await lessonsRepo.nextNumber(a.schoolId, course.id), now: new Date() });
  await lessonsRepo.insert(lesson);
  return lesson;
});

// queries/visibility.ts: read = hardcoded SQL, the access rule is in the WHERE
if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
```

The lesson state machine (`domain/lesson.ts`) is the example of a rule: `start` only from `lobby`, `end` only from `live`, and each refusal is a specific `ErrorCode`.

### 3.3 Module status

| Module | RPCs | Implemented | Stubs |
|---|---|---|---|
| identity (+ admin) | 12 | 7 (Login, RefreshSession, Logout, GetMe, LookupJoinCode, RegisterStudent, RegisterDevice) | 5 (GetInvitation, AcceptInvitation, ListTeachers, InviteTeacher, RegenerateTeacherInvite) |
| courses | 5 | 5 | 0 |
| lessons | 11 | 8 (OpenRoom, CloseRoom, StartLesson, EndLesson, JoinRoom, GetLessonSnapshot, WatchLesson, SetPulse) | 3 (GetLessonReview, UploadMaterial, ListMaterials) |
| doubts, quizzes, floor, attendance, followup | 29 | 0 | 29 |

Full list with status: [CONTRACT.md section 4](./CONTRACT.md#4-rpc-catalogue).

## 4. Tenancy

- **Every table has `school_id`** except `school`, the root. Every foreign key is composite: `invitation.(school_id, user_id) > app_user.(school_id, id)`. A row cannot point at another school's row, even by mistake.
- **The actor comes from the token**: `sub`, `role` and the school (`sid`), set by the auth interceptor. Every query takes `a.schoolId` and filters by it on every table it touches, joins included. Inserts set it.
- **The client sends `X-School-Id`** with the token. A mismatch is refused with `TENANT_MISMATCH` before the handler runs.
- **Never take a tenant id from a request message.**
- **Public RPCs** (`Login`, `RefreshSession`, `GetInvitation`, `AcceptInvitation`, `LookupJoinCode`, `RegisterStudent`) run before a tenant is known. They send neither header and never read the actor.
- **No RLS.** Scoping is enforced in the application and by the composite keys. The reason and the revisit trigger are in migration `002` and CONTRACT section 8.

## 5. Errors

- **Domain codes** are the `ErrorCode` enum in `proto/aula/errors/v1/errors.proto`. The wire value is the enum name.
- **Throw `AppError(ErrorCode.X)`** from anywhere. The outermost interceptor, `errorInterceptor`, logs the raw error and maps it once.
- **The mapping table** in `shared/errors.ts` turns each code into a Connect status (for example `USER_EMAIL_TAKEN` becomes `already_exists`).
- **The code travels in the `Aula-Error-Code` response header.** The Connect status says the kind of failure; the code says which one.
- **Unknown errors and unmapped codes** become `internal` with the message `internal error`. Nothing leaks, and a new code stays invisible until someone maps it.
- **Clients branch on the code, never on the message.** The web app's `errorCode()` returns the code, and its Spanish messages in `apps/web/src/rpc/errors.ts` are exhaustive (adding a code stops the build until it has a message). The Kotlin app reads `ConnectException.domainCode`.
- **A refusal is not always an error.** An attendance scan outside the room is a valid `NEEDS_REVIEW` result that the teacher resolves (CONTRACT section 5).

## 6. Realtime

One RPC serves all live screens: `LessonsService.WatchLesson` (server streaming, Connect framing).

1. **First message: the full `LessonSnapshot`**: server time, counts, the pulse aggregate, the active round of each kind, and `my_pulse` for a student. Teacher-only data is absent for students.
2. **Then, one new snapshot per change.** The hub (`modules/lessons/hub.ts`) rebuilds the snapshot after every change, coalescing bursts into one rebuild 120 ms later, and pushes it to each viewer personalised for that viewer.
3. **Presence is the stream.** A student with an open stream counts as connected (`connected_count`).
4. **Today the server only emits `snapshot` events.** The finer `LessonEvent` variants (pulse, round opened and changed, and so on) are declared so clients can patch later. The server can start emitting them without a contract change.
5. **Fallback: polling.** `GetLessonSnapshot` has `refetch_seconds = 3`. A client that cannot stream still shows the right state, a few seconds late.
6. **Clients:** the web app's `useLessonStream` replaces the snapshot cache entry; the Kotlin app's `LessonRealtime` does the same and reconnects when the stream drops.

**Unverified:** streaming responses on iOS (the Darwin engine) and in the wasm build. Section 10 has the spike.

## 7. Clients

### 7.1 Teacher and admin: web

- **All 22 teacher and admin screens use the deep-purple "stage" surface** (`#3A1B96`). There is no light "paper" screen on web. Paper is only the student app.
- **Data access goes through `apps/web/src/rpc/hooks.ts` only:** `useAulaQuery` (polls at `refetch_seconds`), `useAulaMutation` (invalidates the queries named in `invalidates`), `useLessonStream`, and `errorCode`. Screens never talk HTTP and never invalidate by hand.
- **`transport.tsx` is written once:** it sends `Authorization` and `X-School-Id` on every call. On `TOKEN_EXPIRED` it refreshes the session once (single flight, even when many calls fail together) and retries. If the refresh fails, the session is cleared.
- **Routing:** `react-router` is a dependency, but the shell in `main.tsx` is a hash switch for now (`#/design-system` is the gallery).
- **Built so far:** the login screen and the design-system gallery. The login screen predates the hooks and calls connect-query's `useMutation` directly; new screens use `useAulaMutation`.

### 7.2 Student: Compose Multiplatform

- **Targets:** Android, iOS (Darwin) and `wasmJs`, the browser build for demos with no install. CI compiles wasmJs and Android on Linux and iOS on macOS.
- **Layers in `apps/app/shared/src/commonMain`:**

| Package | What it holds | Hand-written? |
|---|---|---|
| `rpc/` | `ConnectTransport` (unary and streaming, binary protobuf), generated clients and descriptors, `AulaApi` | Transport only; the rest is generated |
| `query/` | `QueryClient`: keyed cache, dedupe, invalidation, polling. "React Query for Compose" | Yes |
| `realtime/` | `LessonRealtime`: keeps the snapshot cache fresh from the stream | Yes |
| `session/` | `SessionHolder`: tokens, refresh through the transport | Yes |
| `designsystem/` | Tokens (`AulaTokens.kt`) and components, with a gallery | Yes |
| `feature/` | Screens: one folder per screen (Screen, Component, State, Event). Reference screens: `auth/login`, `courses/courses`; router in `RootComponent.kt` | Yes |

- **`AppContainer`** is the composition root (no DI framework). `App.kt` currently shows the design-system gallery.
- **Navigation:** Decompose is in the build, with serializable configs planned. No navigation code exists yet (section 10).
- **Binary protobuf on purpose:** the JSON codec of pbandk is experimental and throws on enum values it does not know, which would crash an old app build when the server adds a value.

## 8. From contract to code

```
proto/ ──buf generate──┬─> packages/rpc/src/gen      protobuf-es: messages + services (api, web)
                       ├─> packages/rpc/src/hints    refetch and invalidation tables (web)
                       └─> apps/app/.../rpc/         pbandk messages, service clients,
                                                     query and mutation descriptors, AulaApi (Kotlin)
```

`mage proto:gen` writes it all; `mage proto:check` (in CI) regenerates and fails if the committed output differs. Details in [CONTRACT.md section 2](./CONTRACT.md#2-generation).

## 9. Repo layout

See the repo map in the [README](../README.md#repo-map). Two things are not obvious from it:

- **`packages/rpc` is generated** and is the only TypeScript package both the API and the web app import.
- **`tools/protoc-gen-aula-kotlin` is our code.** It is TypeScript, runs through buf, and writes the Kotlin clients, the query layer and the web hints. Its rules fail the build on purpose (an `invalidates` name that is not an RPC is an error).

## 10. Risks and spike order

Ordered by what can block the demo, cheapest check first.

1. **Streaming on iOS (Darwin) and in the wasm build.** Unverified: the Ktor documentation does not say whether streaming bodies arrive incrementally there. Spike on a real iPhone and in Safari early. If it fails, nothing else changes: the polling fallback (`refetch_seconds = 3`) is already in the contract and the client.
2. **Compose for Web (wasm) is Beta, and pbandk's wasm support is experimental.** CI compiles the browser target from day one so it cannot rot, but treat the browser student app as demo-grade until it runs the live loop end to end.
3. **LLM provider for doubt grouping and quiz drafts.** Not chosen. `ANTHROPIC_API_KEY` appears in `apps/api/.env.example`, but no adapter exists in the repo yet (there is no `apps/api/src/ai/`). MVP priorities P3 and P4 need a deterministic fallback that works without a key.
4. **Attendance on each platform.** The teacher shows the QR and the typed code (no camera on the teacher side). The student's path in the MVP is the typed code; camera scanning is not in the MVP. Location comes from whatever each platform gives (permission prompts and accuracy differ between browsers, Android and iOS). The server-side distance check is specified in the contract but `checkIn` is still a stub.
5. **Push notifications.** Not built. `RegisterDevice` stores the push token; nothing sends a push yet, and `openRoom` carries a TODO for it.
6. **Student navigation.** Decompose is declared but unused. Decide the stack shape with the first student screen, not before.

## 11. Shortcuts we keep

- No DI container, no ports, no fakes, no tests beyond `mage api:smoke` and the drift check.
- `inTx(fn)` is the unit of work; there is no generic UoW abstraction.
- The fan-out is one in-process hub, not a message bus.
- What Chassis had that we dropped (offline, outbox, RabbitMQ, RLS, cupertino, the `:core` split) is listed in [CHASSIS-REVIEW.md](./CHASSIS-REVIEW.md).
