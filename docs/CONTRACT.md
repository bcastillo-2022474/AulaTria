# AulaTria: Contract

The contract is the protobuf source in [`proto/`](../proto). Everything a client or the server knows about the API is generated from it. Change a `.proto`, run `mage proto:gen`, commit the generated output. CI runs `mage proto:check` and fails if the committed output is stale.

Architecture decisions behind it (why protobuf, why ConnectRPC) are in [ARCHITECTURE.md](./ARCHITECTURE.md#2-stack-and-decisions).

## 1. Layout

```
proto/
  buf.yaml                  lint (STANDARD, with four exceptions), breaking (FILE), depends on protovalidate
  buf.gen.yaml              generators: protoc-gen-es, pbandk, protoc-gen-aula-kotlin (x2)
  buf.gen.tools.yaml        generates the options descriptor the Kotlin plugin reads (runs first)
  aula/
    options/v1/options.proto        the four hints (the only hand-authored metadata)
    errors/v1/errors.proto          ErrorCode enum: every domain refusal
    identity/v1/identity.proto      IdentityService (public and session RPCs), AdminService
    courses/v1/courses.proto        CoursesService
    lessons/v1/lessons.proto        LessonsService, including WatchLesson (stream)
    doubts/v1/doubts.proto          DoubtsService
    quizzes/v1/quizzes.proto        QuizService
    floor/v1/floor.proto            FloorService ("toma de la palabra")
    attendance/v1/attendance.proto  AttendanceService
    followup/v1/followup.proto      FollowupService (participation, private notes)
```

Packages are `aula.<area>.v1`. Each RPC's comment names who may call it (`teacher`, `student`, `public`), the domain errors it can return, and the screens that use it. Those comments are the spec for humans; the options are the spec for generators.

## 2. Generation

| Output | Written by | Used by |
|---|---|---|
| `packages/rpc/src/gen` | protoc-gen-es (protobuf-es v2): messages and service descriptors | `apps/api` (connect-node), `apps/web` (connect-query) |
| `packages/rpc/src/hints` | `tools/protoc-gen-aula-kotlin` with `lang=web` | `apps/web/src/rpc/hooks.ts`: refetch intervals and invalidation |
| `apps/app/shared/.../rpc/<area>/v1/*.kt` | pbandk: Kotlin messages | Kotlin app |
| `apps/app/shared/.../rpc/<area>/v1/*Client.kt`, `*Queries`, `*Mutations`, `AulaApi.kt` | `tools/protoc-gen-aula-kotlin` | Kotlin app: `QueryClient` and the screens |

Commands (from the repo root):

- `mage proto:gen`: lints with buf, regenerates everything. Needs `buf` on PATH, Java 21, and `.tools/protoc-gen-pbandk-0.16.0.jar`.
- `mage proto:check`: the drift check. Regenerates and fails if anything changed.

**Hand-authored metadata** (`proto/aula/options/v1/options.proto`):

| Option | On | Meaning |
|---|---|---|
| `invalidates` | mutation | Names of queries that are stale after this succeeds. The generated client invalidates them. A mutation with no `invalidates` and no `no_invalidation` prints a warning at codegen |
| `no_invalidation` | mutation | Deliberately invalidates nothing (login, check-in). Silences the warning |
| `refetch_seconds` | query | Poll interval while a screen watches the query. Also the fallback for the stream |
| `screens` | any | Documentation only: the docs/design screens that call it |
| `idempotency_level = NO_SIDE_EFFECTS` | RPC (standard protobuf, not ours) | Marks a **query**. Every other RPC is a **command**. A server-streaming RPC is a **stream** |

## 3. Conventions

- **Headers.** Every non-public RPC needs `Authorization: Bearer <token>` and `X-School-Id: <school>`. The school must match the token's school, else `TENANT_MISMATCH`. Public RPCs need neither.
- **Public RPCs:** `Login`, `RefreshSession`, `GetInvitation`, `AcceptInvitation`, `LookupJoinCode`, `RegisterStudent`.
- **The tenant is never in a message body.** The server takes it from the token.
- **Ids** are UUID strings, validated with `string.uuid = true`. The server mints them (UUIDv7).
- **Times** are `google.protobuf.Timestamp`. Anything time-boxed (a doubt round, a quiz, a floor turn, the QR code) is a **deadline** (`closes_at`, `qr_expires_at`), never "seconds left". Live snapshots carry `server_time`, so clients count down with the server's clock, not the device's (`apps/web/src/rpc/time.ts`).
- **Role projection.** One message shape per screen, and the server removes what the caller may not see. Examples: `CourseDetail.join_code` is teacher-only; a quiz's correct answer and insight are not sent to students while it is live; `Pulse` is an aggregate, never a per-student value.
- **Enums** start with `*_UNSPECIFIED = 0`, which is never sent. Enum fields in requests use `defined_only` and `not_in: [0]`.
- **Validation** uses protovalidate rules in the `.proto`. The server applies them before any handler runs (`createValidateInterceptor`), so a handler never sees a malformed request.
- **Naming.** The `.proto` names are PascalCase (`GetLessonSnapshot`). In TypeScript and Kotlin the generated methods are camelCase (`getLessonSnapshot`). This document uses the proto names.
- **Versioning.** Packages are `v1`. `buf breaking` (FILE rules) guards against incompatible changes. Adding enum values and fields is compatible; the Kotlin app must tolerate unknown enum values, which is why it uses binary protobuf.

## 4. RPC catalogue

Generated from the proto files (service, kind, roles, options and screens) and the API wiring (`apps/api/src/modules/*/service.ts`, and the `notImplemented` stubs in `commands/` and `queries/`). A stub answers `unimplemented`.

**Totals: 57 RPCs. 20 implemented, 37 stubs.** Stream: `WatchLesson` only.

| Service | RPC | Kind | Who | Invalidates | Refetch s | Screens (from proto) | Backend |
|---|---|---|---|---|---|---|---|
| Attendance | `OpenAttendanceRound` | command | teacher | `GetLessonSnapshot` | - | Docente-Live (Tomar asistencia) | stub |
| Attendance | `GetAttendanceCodes` | query | teacher | - | 4 | Docente-Asistencia (QR + the typable code) | stub |
| Attendance | `GetAttendanceRound` | query | teacher | - | 3 | Docente-Asistencia, Docente-AsistCerrada | stub |
| Attendance | `CheckIn` | command | student | none (`no_invalidation`) | - | Estudiante-Escanear -> Verificando -> Presente / PorRevisar | stub |
| Attendance | `CloseAttendanceRound` | command | teacher | `GetAttendanceRound`, `GetLessonSnapshot` | - | Docente-Asistencia (Cerrar asistencia) | stub |
| Attendance | `ResolveAttendance` | command | teacher | `GetAttendanceRound` | - | Docente-AsistCerrada | stub |
| Courses | `ListCourses` | query | teacher, student | - | 15 | Docente-Cursos, Docente-CursosVacio, Estudiante-Cursos | implemented |
| Courses | `CreateCourse` | command | teacher | `ListCourses` | - | Docente-CrearCurso, Docente-CursoCreado | implemented |
| Courses | `GetCourse` | query | teacher, student | - | - | Docente-Curso | implemented |
| Courses | `JoinCourse` | command | student | `ListCourses` | - | Estudiante-Confirmar, Estudiante-Unido | implemented |
| Courses | `ListCourseStudents` | query | teacher | - | - | _not designed_ | implemented |
| Doubts | `OpenDoubtRound` | command | teacher | `GetLessonSnapshot` | - | Docente-Live -> Docente-DudasAbierta | stub |
| Doubts | `ExtendDoubtRound` | command | teacher | `GetDoubtRound` | - | Docente-DudasAbierta (+30 s) | stub |
| Doubts | `CloseDoubtRound` | command | teacher | `GetDoubtRound`, `GetLessonSnapshot` | - | Docente-DudasAbierta (Cerrar ahora) | stub |
| Doubts | `SubmitDoubt` | command | student | `GetDoubtRound` | - | Estudiante-Dudas, Estudiante-DudaEnviada | stub |
| Doubts | `GetDoubtRound` | query | teacher, student | - | 2 | Docente-DudasAbierta, Docente-DudasResumen, Estudiante-DudaEnviada, Estudiante-DudaGrupo | stub |
| Doubts | `ListDoubts` | query | teacher | - | - | Docente-DudasResumen (Ver las dudas originales) | stub |
| Floor | `OpenFloorRound` | command | teacher | `GetLessonSnapshot` | - | Docente-Live (Dar la palabra) -> Docente-Palabra | stub |
| Floor | `RaiseHand` | command | student | `GetFloorRound` | - | Estudiante-Levantar | stub |
| Floor | `LowerHand` | command | student | `GetFloorRound` | - | Estudiante-Turno | stub |
| Floor | `FinishMyTurn` | command | student | `GetFloorRound` | - | Estudiante-TuTurno | stub |
| Floor | `ResolveTurn` | command | teacher | `GetFloorRound` | - | Docente-Palabra | stub |
| Floor | `OpenFloorToAll` | command | teacher | `GetFloorRound` | - | Docente-Palabra | stub |
| Floor | `CloseFloorRound` | command | teacher | `GetFloorRound`, `GetLessonSnapshot` | - | Docente-Palabra | stub |
| Floor | `GetFloorRound` | query | teacher, student | - | 2 | Docente-Palabra, Estudiante-Turno, Estudiante-TuTurno, Estudiante-ResultadoTurno | stub |
| Followup | `GetStudentParticipation` | query | teacher | - | - | Docente-Perfil | stub |
| Followup | `SaveStudentNote` | command | teacher | `GetStudentParticipation` | - | Docente-Perfil | stub |
| Identity | `Login` | command | public (no tenant) | none (`no_invalidation`) | - | Estudiante-Bienvenida (Ya tengo cuenta), every role | implemented |
| Identity | `RefreshSession` | command | public (no tenant) | none (`no_invalidation`) | - | - | implemented |
| Identity | `Logout` | command | any (authenticated) | none (`no_invalidation`) | - | - | implemented |
| Identity | `GetMe` | query | any (authenticated) | - | - | - | implemented |
| Identity | `GetInvitation` | query | public (no tenant) | - | - | Admin-Acceso, Docente-Invitacion | stub |
| Identity | `AcceptInvitation` | command | public (no tenant) | none (`no_invalidation`) | - | Admin-Acceso, Docente-Invitacion | stub |
| Identity | `LookupJoinCode` | query | public (no tenant) | - | - | Estudiante-Confirmar, Estudiante-Registro | implemented |
| Identity | `RegisterStudent` | command | public (no tenant) | none (`no_invalidation`) | - | Estudiante-Registro | implemented |
| Identity | `RegisterDevice` | command | teacher, student | none (`no_invalidation`) | - | - | implemented |
| Admin | `ListTeachers` | query | admin | - | - | Admin-Catedraticos, Admin-Lista | stub |
| Admin | `InviteTeacher` | command | admin | `ListTeachers` | - | Admin-Agregar | stub |
| Admin | `RegenerateTeacherInvite` | command | admin | `ListTeachers` | - | Admin-Lista | stub |
| Lessons | `OpenRoom` | command | teacher | `GetCourse`, `ListCourses` | - | Docente-Curso (Iniciar sala), Docente-SalaAbierta | implemented |
| Lessons | `CloseRoom` | command | teacher | `GetCourse`, `ListCourses` | - | Docente-SalaAbierta (Cerrar sala) | implemented |
| Lessons | `StartLesson` | command | teacher | `GetLessonSnapshot` | - | Docente-SalaAbierta (Empezar clase) | implemented |
| Lessons | `EndLesson` | command | teacher | `GetLessonSnapshot`, `GetCourse`, `ListCourses` | - | Docente-Live (Terminar clase) | implemented |
| Lessons | `JoinRoom` | command | student | `GetLessonSnapshot` | - | Estudiante-Cursos (Entrar) | implemented |
| Lessons | `GetLessonSnapshot` | query | teacher, student | - | 3 | Docente-Live, Docente-SalaAbierta, Estudiante-Home | implemented |
| Lessons | `WatchLesson` | stream | teacher, student | - | - | - | implemented |
| Lessons | `SetPulse` | command | student | `GetLessonSnapshot` | - | Estudiante-Home | implemented |
| Lessons | `GetLessonReview` | query | teacher | - | - | Docente-Pulso | stub |
| Lessons | `UploadMaterial` | command | teacher | `ListMaterials` | - | _not designed_ (Docente-SalaAbierta?) | stub |
| Lessons | `ListMaterials` | query | teacher | - | - | - | stub |
| Quizzes | `DraftQuiz` | command | teacher | none (`no_invalidation`) | - | Docente-Live (Lanzar quiz) -> Docente-QuizArmar | stub |
| Quizzes | `RegenerateQuiz` | command | teacher | `GetQuiz` | - | Docente-QuizArmar (Regenerar pregunta) | stub |
| Quizzes | `UpdateQuizTime` | command | teacher | `GetQuiz` | - | Docente-QuizArmar (Tiempo por pregunta) | stub |
| Quizzes | `LaunchQuiz` | command | teacher | `GetQuiz`, `GetLessonSnapshot` | - | Docente-QuizArmar -> Docente-QuizVivo | stub |
| Quizzes | `AnswerQuiz` | command | student | `GetQuiz` | - | Estudiante-Quiz | stub |
| Quizzes | `CloseQuiz` | command | teacher | `GetQuiz`, `GetLessonSnapshot` | - | Docente-QuizVivo (Mostrar resultado) | stub |
| Quizzes | `GetQuiz` | query | teacher, student | - | 2 | Docente-QuizVivo, Docente-QuizResultados, Estudiante-Quiz, Estudiante-QuizResultado | stub |

## 5. Errors

**How an error travels**

| Layer | What it carries |
|---|---|
| Connect status | The kind of failure: `not_found`, `already_exists`, `failed_precondition`, `permission_denied`, and so on |
| `Aula-Error-Code` header | The domain code, for example `COURSE_ALREADY_ENROLLED` |
| Message | Never read by clients |

The server maps each code to a Connect status in one table (`apps/api/src/shared/errors.ts`). A code with no row is reported as `internal` with no message. Unknown errors are `internal` too, and leak nothing.

**Codes** (`proto/aula/errors/v1/errors.proto`, 35 in total):

| Family | Codes |
|---|---|
| Shared | `MALFORMED_ARGUMENT`, `FORBIDDEN` (right user, wrong role), `UNAUTHENTICATED`, `TOKEN_EXPIRED` (refresh and retry once), `TENANT_MISMATCH`, `NOT_FOUND` (also used for "not yours") |
| Identity | `USER_INVALID_CREDENTIALS`, `USER_EMAIL_TAKEN`, `INVITATION_INVALID` |
| Courses | `COURSE_NOT_FOUND`, `COURSE_JOIN_CODE_NOT_FOUND`, `COURSE_ALREADY_ENROLLED`, `COURSE_NOT_ENROLLED` |
| Lessons | `LESSON_NOT_FOUND`, `LESSON_ALREADY_ACTIVE`, `LESSON_NOT_IN_LOBBY`, `LESSON_NOT_LIVE`, `MATERIAL_INVALID` |
| Doubts | `DOUBT_ROUND_NOT_FOUND`, `DOUBT_ROUND_ALREADY_OPEN`, `DOUBT_ROUND_CLOSED` |
| Floor | `FLOOR_ROUND_NOT_FOUND`, `FLOOR_ROUND_ALREADY_OPEN`, `FLOOR_ROUND_CLOSED`, `FLOOR_NOT_YOUR_TURN` |
| Quizzes | `QUIZ_NOT_FOUND`, `QUIZ_ALREADY_LIVE`, `QUIZ_NOT_DRAFT`, `QUIZ_CLOSED`, `QUIZ_ALREADY_ANSWERED`, `QUIZ_NO_MATERIAL` |
| Attendance | `ATTENDANCE_ROUND_NOT_FOUND`, `ATTENDANCE_ROUND_ALREADY_OPEN`, `ATTENDANCE_ROUND_CLOSED`, `ATTENDANCE_QR_INVALID` (wrong or expired code) |

**Rules**

- Codes are **specific**. `COURSE_ALREADY_ENROLLED` tells the client what to do; a generic `ALREADY_EXISTS` would not.
- A code is never a message. Screens switch on the code.
- The web app's Spanish messages for codes (`apps/web/src/rpc/errors.ts`) are exhaustive: adding a code stops the build until it has a message.
- An attendance check-in outside the room is **not** an error. It returns `NEEDS_REVIEW` (section 6.3).

## 6. Realtime and attendance

### 6.1 WatchLesson

`LessonsService.WatchLesson` is one server-streaming RPC. The first message is always a full `LessonSnapshot`. Each later message is a new full snapshot after a change, personalised for the viewer. The stream being open is the student's presence.

Clients that cannot stream poll `GetLessonSnapshot` every 3 seconds (`refetch_seconds`). Nothing else changes for them.

**Events.** `LessonEvent` is a `oneof` with these kinds. Today the server only emits `snapshot`; the others are declared so clients can patch instead of replace later.

| Event | Shape | Client action |
|---|---|---|
| `snapshot` | `LessonSnapshot` | Replace the snapshot cache entry |
| `lesson_status` | new `LessonStatus` | Patch (or refetch the snapshot) |
| `presence` | `joined_count`, `connected_count` | Patch |
| `pulse` | `Pulse` (aggregate) | Patch |
| `doubt_round_opened` / `_changed` / `_closed` / `_summary_ready` | round id, deadline, counts | Refetch the round by id |
| `floor_opened` / `floor_changed` / `floor_closed` | round id, prompt | Refetch `GetFloorRound` |
| `quiz_launched` / `quiz_progress` / `quiz_closed` | quiz id, deadline, counts | Refetch `GetQuiz` |
| `attendance_opened` / `attendance_progress` / `attendance_closed` | round id, counts | Refetch the round |

### 6.2 Who may watch

`WatchLesson` and `GetLessonSnapshot` use the same visibility rule: a teacher who owns the course, or a student enrolled in it. Anyone else gets `LESSON_NOT_FOUND`, which is the same answer a missing lesson gives.

### 6.3 Attendance

The teacher's screen shows **two** codes, both from `GetAttendanceCodes`:

- **`qr_code`** rotates every 8 seconds, so a photo of the screen is useless.
- **`manual_code`** is short (for example `K7M2QX`) and rotates about every 30 seconds. The previous code stays valid for one step, so nobody loses a race while typing. Students use it when the camera is not an option.

`CheckIn` accepts either. The student's location decides the result:

| Situation | Result |
|---|---|
| Inside the round's radius (default 50 m, around the teacher's position when the round opened) | `PRESENT` |
| Outside the radius | `NEEDS_REVIEW`, reason `TOO_FAR`, with the distance recorded |
| No location permission | `NEEDS_REVIEW`, reason `NO_LOCATION` |
| Location too imprecise | `NEEDS_REVIEW`, reason `LOW_ACCURACY` |
| Wrong or expired code | Error `ATTENDANCE_QR_INVALID` |

Those `NEEDS_REVIEW` outcomes are valid results: the student sees "Por revisar", and the teacher accepts or marks absent with `ResolveAttendance`. The distance is computed on the server from the coordinates, never trusted from the client. The check itself is specified but still a stub (`checkIn`).

## 7. How clients consume the contract

| Need | Teacher and admin web | Student app (Kotlin) |
|---|---|---|
| Read | `useAulaQuery(Service.method.X, input)` | `api.<area>Queries.<rpc>(request)`, a `Query` watched through `QueryClient` |
| Write | `useAulaMutation(Service.method.X)`: invalidates what the proto says | `api.<area>Mutations` (generated): same rule |
| Polling | `refetch_seconds` becomes `refetchInterval` | `refetchEveryMs` on the query |
| Live stream | `useLessonStream(lessonId)`: replaces the snapshot cache entry | `LessonRealtime`: same, reconnects when the stream drops |
| Errors | `errorCode(error)`, plus the Spanish message | `ConnectException.domainCode` |
| Session | `transport.tsx`: attaches the headers, refreshes once on `TOKEN_EXPIRED` | `ConnectTransport` with `SessionHeaders`: same behaviour |
| Calls | Generated connect-query descriptors | Generated `<area>Client` over `RpcTransport` (unary: binary protobuf POST; stream: Connect envelopes) |

Screens never build a URL, never set a header, and never invalidate a cache key by hand.

## 8. Open questions

Each has a default, so work is not blocked; each needs an owner decision before the feature ships.

1. **Streaming on iOS (Darwin) and in the wasm build.** Unverified. Default: polling fallback. Spike first (ARCHITECTURE section 10).
2. **LLM provider for doubt grouping and quiz drafts.** Not chosen, and no adapter exists in the repo yet. Default: a deterministic fallback, so the demo cannot fail on a missing key.
3. **Camera scanning of the QR.** Not in the MVP. Default: the student types the manual code. Location is only what each platform provides.
4. **Lesson material upload.** `UploadMaterial` and `ListMaterials` are stubs and no screen is designed for them. The quiz and doubt grouping depend on the material. Default: upload from the room screen (Docente-SalaAbierta), to be designed.
5. **Student roster.** `ListCourseStudents` has no screen yet ("roster screen not designed yet").
6. **Recording, activities, extra points.** No RPCs. Out of v1 (see [MVP.md](./MVP.md)).
7. **Round timers.** Default: no server-side timer. Clients show the countdown and block input at the deadline. The server refuses late writes with the round's `CLOSED` code.
8. **Minors' data.** Students are likely under 18: retention, consent, and whether location may be stored are open. The answer decides whether we add row-level security and a deletion job (ARCHITECTURE section 4).
9. **Push notifications.** `RegisterDevice` stores the push token. Nothing sends pushes yet, and the open-room push is a TODO.
10. **One school per email.** Assumed globally unique. Confirm before the second school is onboarded.
