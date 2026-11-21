---
name: new-rpc
description: Add or change a backend operation (an RPC) in AulaTria end to end - proto contract, generated code, API handler with tenant scoping and authz, realtime nudge. Use for any new feature that reads or writes data, e.g. OpenDoubtRound or GetQuiz.
---

# New RPC (the unit of backend work)

Reference implementations: `apps/api/src/modules/lessons` (entity + repository + commands + queries + realtime), `courses` (role-scoped reads, join codes), `identity` (public RPCs, sessions). Read the one closest to what you are building. Contract examples: `proto/aula/lessons/v1/lessons.proto`.

## 0. Decide the shape
- **One RPC per business fact, not per screen.** A screen that needs three things calls three RPCs (queries are cheap and cached).
- **Query or command?** Query = reads only → `option idempotency_level = NO_SIDE_EFFECTS` (that is what makes it a cached `Query` on both clients). Everything else is a command (mutation).
- **Who may call it?** Exactly the roles in the proto comment (`// teacher, student`) and in the handler's first line. Public RPCs (login, join-code lookup) are the only ones with no `actor()`.
- **Does it change what a live class shows?** Then it ends with `touch(...)`, see skill `touch-realtime`.

## 1. Contract: `proto/aula/<module>/v1/<module>.proto`
- Messages named `<Rpc>Request` / `<Rpc>Response` (buf lint STANDARD). Never reuse another RPC's messages. Responses wrap the entity (`message CreateCourseResponse { CourseDetail course = 1; }`) so adding fields later is compatible.
- **Validate syntactically in the proto** with protovalidate: ids `[(buf.validate.field).string.uuid = true]`, string `min_len/max_len`, ints `gte/lte`, enums `defined_only` + `not_in: [0]`. Semantic rules (round closed, not enrolled) are the handler's.
- **Never take the tenant, the actor or a role in a request message.** They come from the token.
- Role-projected data: ONE message, with comments saying `// teacher only` / `// student only`; the handler fills what the caller may see. Do not make two parallel DTOs "because they look alike", and never send the student a teacher-only field (correct answers while live, counts, other students' pulse).
- Hints (the only hand-authored metadata, read by the generators):
  - commands: `option (aula.options.v1.invalidates) = "ListCourses";` for every query that becomes stale (repeat the option per query; names are RPC names). If it truly invalidates nothing: `option (aula.options.v1.no_invalidation) = true;`. The generator warns otherwise.
  - queries that should poll while watched: `option (aula.options.v1.refetch_seconds) = N;` (live things only; 2-4 s).
  - `option (aula.options.v1.screens) = "Docente-Live"` naming the design screens that call it.
- Document raised error codes in the RPC comment. New code → skill `new-error-code`.
- Enums: `X_UNSPECIFIED = 0` first, values prefixed with the enum name. New enum values are additive; never renumber.
- `mage proto:gen`. It must print no warnings. Commit the generated output with the change (CI runs `proto:check`).

## 2. Implement: CQRS, strictly (`apps/api/src/modules/<module>/`)
Every RPC already has a **stub** in `commands/<rpc>.ts` or `queries/<rpc>.ts` (it throws `notImplemented`) and a line in `service.ts` carrying the roles. A brand-new RPC: add the file and the `service.ts` line yourself. Layout of a module:

```
domain/<entity>.ts      pure entities: state machine + invariants, NO I/O (see lessons/domain/lesson.ts)
repository.ts           WRITE-side persistence: concrete classes, no interface (see lessons/repository.ts)
commands/<rpc>.ts       WRITES: load entity via repository -> entity method -> save -> touch(realtime)
queries/<rpc>.ts        READS: one specialized, hardcoded query -> response message. No repository, no entity.
service.ts              thin role table: `openRoom: (req, ctx) => openRoom(actor(ctx, "teacher"), req)`
```

### Command (write) pattern
```ts
export async function openRoom(a: Actor, req: lessons.OpenRoomRequest) {
  const lesson = await inTx(async (trx) => {                      // one transaction per command; repos are built INSIDE it
    const course = await new CourseRepository(trx).findForTeacher(a.schoolId, a.userId, req.courseId); // ownership is part of the load
    if (!course) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
    const repo = new LessonRepository(trx);
    const lesson = Lesson.open({ schoolId: a.schoolId, courseId: course.id, number: await repo.nextNumber(a.schoolId, course.id), now: new Date() });
    await repo.insert(lesson);                                     // races become domain errors inside the repository
    return lesson;
  });
  touch(a.schoolId, lesson.id);                                    // live screens refresh (skill touch-realtime), AFTER commit
  return { lesson: toLessonMessage(lesson) };
}
```
- The **entity** enforces the rules (`lesson.start(now)` throws `LESSON_NOT_IN_LOBBY`). Rules never live in the use case's `if`s when they belong to the entity. Entities never import the DB.
- The **repository** only loads and saves entities (`findForTeacher`, `insert`, `save`, small appenders). Every method takes `schoolId` first, filters `school_id` on every table, and sets it on inserts. Do **not** add list/count/search/aggregate methods: that is a read.
- Add a repository method only when a command needs it; do not create generic CRUD.
- Multi-aggregate writes share the transaction (`new CourseRepository(trx)` + `new LessonRepository(trx)`); a parent's repository may own its cascade (`LessonRepository.closeOpenRounds`).

### Query (read) pattern
```ts
export async function listCourses(a: Actor) {
  const rows = await db.selectFrom("course").select([...]).where("course.school_id", "=", a.schoolId)   // tenant on EVERY table touched
    .where("course.teacher_id", "=", a.userId).execute();                                              // ownership as a filter, not a pre-check
  return { courses: rows.map(...) };                                                                   // build the proto message directly
}
```
- The SQL is **hardcoded in the use case file**, shaped like the screen. Joins use `.onRef("x.school_id", "=", "y.school_id")`. Counts/percentages/series are computed here, in SQL.
- **Never** call a repository or build an entity in a query. A read that needs the "can this actor see it" rule uses the module's `queries/visibility.ts` helper (a query, not a repository).
- Ownership is a filter: zero rows = not found; "does not exist" and "not yours" are the same answer. No existence pre-check followed by a second query.
- Role projection: fill only what the caller may see; a student never gets teacher-only fields (correct answer while live, counts, others' pulse).

### Both
- Authz lives in `service.ts` (`actor(ctx, "teacher")`): the first and only role check, in one auditable table.
- Timestamps via `ts()`/`tsOrUndef()`, ids via `newId()` (UUIDv7), enums mapped explicitly, never return DB rows.
- Time-boxed things store a deadline (`closes_at`); late writes are refused by the entity (`..._CLOSED`); clients count down using `serverTime`.
- AI calls go in `apps/api/src/ai/` behind one function each with a deterministic fallback, so a demo never depends on the network.
- Domain refusals are `AppError(ErrorCode.X)`; new code -> skill `new-error-code`. No `console.log`, no logging secrets/PII.
- Intentional shortcuts, do NOT add: interfaces for repositories, DI, use-case classes, a generic repository, tests.

## 3. Verify (see skill `verify`)
`mage proto:check api:check`, restart `mage api:dev`, and add the call to `apps/api/scripts/smoke.ts` when it is a new core flow (auth/tenant/role/realtime assertions use the generated TS client). Check the unhappy paths: wrong role, other school's id, closed round.

## Never
Edit generated code; trust ids from the client without scoping them by tenant AND ownership; return teacher-only fields to students; add a second way to call the same thing; leave a mutation without `invalidates`/`no_invalidation`.
