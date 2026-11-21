---
name: new-mobile-screen
description: Build a student (phone) screen in the AulaTria Compose Multiplatform app - Component/State/Event/Screen, data through the generated query layer, navigation wiring, design-system fidelity. Use for any Estudiante-* screen from docs/design.
---

# New mobile screen (Compose Multiplatform: Android + iOS + browser)

Reference screens (copy their shape): `feature/auth/login` (a form + a call) and `feature/courses/courses` (read-only server data). Router: `feature/RootComponent.kt`. Machinery docs: `apps/app/shared/.../feature/README.md`. Everything is under `apps/app/shared/src/commonMain/kotlin/org/aula/app/`.

## 0. Before writing
1. Open the original: `cd docs/design && python3 -m http.server 8099` → `Estudiante-C-<Name>.dc.html`. The screen must look like it, **composed only from `designsystem/` components**. If a component is missing, follow skill `change-design-system` first. Never write colours/sizes in a screen.
2. Find the RPCs in `docs/UI-FLOWS.md` (screen → operations). They are all in the contract already (`proto/`). If one is missing → skill `new-rpc` first. If it is only a stub, you may build against it: the call answers `unimplemented` until the backend lane lands.
3. Decide the group (`auth`, `courses`, `live`, `attendance`...) and the screen folder: `feature/<group>/<screen>/`, package `org.aula.app.feature.<group>.<screen>`, lowercase one word.

## 1. The four files
**`<X>State.kt`**: `data class` with everything the screen draws and nothing else (form fields, `loading`, `error: String?` as a Spanish sentence). A read-only screen has none.
**`<X>Event.kt`**: `sealed interface`, one case per user action: `OnFooChange(value)`, `FooClick`. 
**`<X>Component.kt`**: state + logic. **Never imports `androidx.compose.*`.** Constructor: `componentContext`, `container: AppContainer`, navigation callbacks (`onDone: () -> Unit`). One public `onEvent`; handlers private.
**`<X>Screen.kt`**: `XScreen(component)` collects state and maps taps to events; `XContent(state, onEvent)` is stateless and holds ALL the layout. Imports `designsystem.*` and Compose; **never** `rpc.*` clients or Ktor.

## 2. Data: never talk HTTP
- **Read**: `container.queries.watch(container.api.<service>Queries.<rpc>(Request(...)))` returns `Flow<QueryState<Response>>` (Loading / Success(data, isRefetching) / Failure(error, staleData)). In a composable: `query.collectAsState()`. The cache dedupes, keeps stale data on failure, and polls where the proto says `refetch_seconds`. Render all three states (loading text, error with `error.userMessage()`, empty state, data).
- **Write**: `MutationRunner(container.queries, container.api.<service>Mutations.<rpc>, scope)` in a component, or `rememberMutation(...)` in a composable. `run(request) { onSuccess }`. The queries named in the proto `invalidates` refresh by themselves: **never invalidate by hand**. A double tap is one request (the runner ignores it while running).
- **Direct client calls** (`container.api.<service>Client.<rpc>(...)`) only for flows that are not cacheable (login, which also calls `container.session.start(session)`).
- **Live class**: `rememberLessonSnapshot(lessonId)` (realtime/RememberLessonSnapshot.kt) gives the snapshot kept fresh by the stream; it already falls back to polling. A student's pulse tap is `setPulse` mutation (no invalidation needed, the stream pushes).
- **Deadlines**: `rememberCountdown(deadline, snapshot.serverTime)` (time/Countdown.kt), never "seconds left" from the server.
- **Errors**: show `throwable.userMessage()` (Spanish, exhaustive over `ErrorCode`), never the server's message. Branch on `throwable.errorCode()` for behaviour (e.g. `QUIZ_ALREADY_ANSWERED` → show the result).
- Session: `container.session.me` (name, role, schoolName). Tenant headers are attached for you.

## 3. Wire navigation (four edits, the compiler fails if you skip one)
In `RootComponent.kt`: a `Config` (`@Serializable`, `data object` or `data class` with ids), a `Child` wrapping exactly ONE component, a case in `child()`, and a case in `RootContent.kt`. Screens **never navigate themselves**: they receive callbacks and `RootComponent` decides. Always `navigation.pushNew(...)`, never `push` (double tap crash). Ids travel in the Config.

## 4. Rules
- Student targets: min 56dp touch height (the DS buttons already are). One-handed, big, glanceable.
- Copy text from the original design (Spanish); identifiers in English.
- Works on **Android, iOS and browser**: only `commonMain` APIs. Platform needs (location, camera, storage) go behind `expect/actual` in `platform/` with an actual for each of the three.
- No Material, no Compose-only types in components, no globals, no `GlobalScope`: use the component's scope and cancel it on destroy (`lifecycle.doOnDestroy`).
- No tests (deliberate). Previews/gallery: add your `XContent` with sample state to `designsystem/DesignSystemGallery`'s screens only if it is a new visual pattern.

## 5. Verify (skill `verify`)
`mage app:check` (wasm + Android). Look at it: `mage app:web` (browser, `?gallery` shows the design system) or Android emulator; compare with the original board. iOS compiles only on a Mac/CI (`app-ios` job): say so, never claim iOS verified.
