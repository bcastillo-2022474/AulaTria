# Client RPC machinery (web and Compose)

> Contributors do not choose or wire any technology to talk to the API. Everything below exists; you call it.
> The contract is `proto/`; both clients are generated from it. Rules: [CLAUDE.md](../CLAUDE.md).

## The one picture

```
proto/ ──buf generate──┬─► packages/rpc (TS: messages, service descriptors, hints)  ─► apps/web
                       └─► apps/app/.../rpc (Kotlin: pbandk messages + generated clients, Query/Mutation descriptors, AulaApi) ─► apps/app
```
A screen asks for data with **one call**, gets **loading / error / data**, and a write **refreshes the right screens by itself**. Nobody writes HTTP, auth headers, retries, cache keys or invalidation.

## What is handled for you (both clients)

| Concern | How |
|---|---|
| Transport | Connect protocol over HTTP, binary protobuf (Kotlin) / connect-web (TS) |
| Auth | `Authorization: Bearer` + `X-School-Id` (tenant) attached to every call; session persisted (localStorage / SharedPreferences / NSUserDefaults) |
| Expired token | on `TOKEN_EXPIRED` the session is refreshed **once** (single flight) and the call retried; if the session is dead the user is signed out |
| Caching | keyed cache, request dedupe, stale data kept on failure, cleared on sign-out |
| Invalidation | from the proto option `invalidates` on each mutation (generated), never by hand |
| Polling | from the proto option `refetch_seconds` on each query (generated); also the fallback for realtime |
| Errors | domain code from the `Aula-Error-Code` header; Spanish message per code, **exhaustive** (adding a code to `errors.proto` breaks both clients' builds until a message is written) |
| Realtime | one stream per lesson keeps the `GetLessonSnapshot` cache fresh; WebSocket is the decided transport (stub), `WatchLesson` works today |
| Countdowns | deadlines + `serverTime`, never "seconds left" |

## Web (`apps/web/src/rpc/`)

| File | You use it for |
|---|---|
| `hooks.ts` | **`useAulaQuery(method, input)`**, **`useAulaMutation(method, opts)`**, **`useLessonStream(lessonId)`**, `errorMessage`, `errorCode` |
| `session.ts` | `useSession()` (who is signed in), `session.set/clear` after login/logout |
| `errors.ts` | `errorMessage(error)` (Spanish), `errorCode(error)` |
| `time.ts` | `useCountdown(deadline, serverTime)`, `formatClock` |
| `transport.tsx` | the plumbing (auth interceptor, refresh, global sign-out). **Do not edit.** |

```tsx
const list = useAulaQuery(CoursesService.method.listCourses);                       // typed request/response from the proto
const open = useAulaMutation(LessonsService.method.openRoom, { onSuccess: (r) => navigate(`/lessons/${r.lesson!.id}`) });
if (list.isPending) return <Text>Cargando…</Text>;
if (list.error) return <Banner tone="danger">{errorMessage(list.error)}</Banner>;
```
Typed hooks come from `@connectrpc/connect-query` (it reads the generated service descriptors); `hooks.ts` adds our hints on top.

## Compose: Android, iOS, browser (`apps/app/shared/src/commonMain/kotlin/org/aula/app/`)

| Piece | You use it for |
|---|---|
| `AppContainer` (`LocalAppContainer`) | the composition root (no DI framework): `container.api`, `container.queries`, `container.session` |
| `rpc/AulaApi` (generated) | `api.coursesQueries.listCourses()`, `api.coursesMutations.createCourse`, raw `api.identityClient.login(...)` |
| `query/QueryClient` | `watch(query)` → `Flow<QueryState>`; `Query.collectAsState()` in a composable |
| `query/Mutations.kt` | `MutationRunner` (components) / `rememberMutation` (composables): `run(request) { onSuccess }`, state Idle/Running/Success/Failure, double tap = one request |
| `realtime/` | `rememberLessonSnapshot(lessonId)`: the snapshot kept fresh by the stream, polling as fallback |
| `errors/ErrorMessages.kt` | `throwable.userMessage()`, `throwable.errorCode()` |
| `time/Countdown.kt` | `rememberCountdown(deadline, serverTime)`, `formatClock` |
| `session/Session.kt` | `container.session.me` (StateFlow), persisted through `platform/TokenStorage` (expect/actual ×3) |
| `rpc/ConnectTransport.kt` | the plumbing (unary + server streaming, refresh-and-retry). **Do not edit** unless fixing the transport. |

```kotlin
val state by container.api.coursesQueries.listCourses().collectAsState()          // QueryState: Loading | Success | Failure(error, staleData)
val create = rememberMutation(container.api.coursesMutations.createCourse)
create.run(CreateCourseRequest(name = "BD2")) { navigateToCourse(it.course!!.id) }  // invalidates ListCourses by itself
```

## Not verified yet (be honest in your PRs)
- **Streaming on iOS (Darwin engine) and in the browser build** is unverified: the Kotlin streaming code compiles for Android and wasm, but was only exercised against the API through the TypeScript client and `buf curl`. Polling covers the gap. First real iPhone run is a spike.
- The Kotlin transport has no automated test (deliberate); the first Android/emulator run is its first real exercise.
- Tokens are stored in plain platform preferences (not Keychain/Keystore): MVP-grade.

## Adding something to the machinery
You almost never do. A new RPC, error code, or hint is a **proto** change (`new-rpc`, `new-error-code`) and both clients pick it up on `mage proto:gen`. Touch `transport.tsx` / `ConnectTransport.kt` / `QueryClient.kt` only to fix a bug in the machinery itself, and say so in the PR.
