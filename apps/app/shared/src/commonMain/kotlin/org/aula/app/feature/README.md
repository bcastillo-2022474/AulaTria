# feature/

One folder per area, one folder per screen inside it: `feature/<area>/<screen>/` with exactly four files.

| File | Is | May import | Never imports |
|---|---|---|---|
| `<X>State.kt` | `data class` with everything the screen draws | stdlib | |
| `<X>Event.kt` | `sealed interface`, one case per user action | stdlib | |
| `<X>Component.kt` | state + logic (a Decompose component, the "ViewModel") | `AppContainer`, generated `rpc.*`, `query.*`, `errors.*`, coroutines, Decompose | `androidx.compose.*` |
| `<X>Screen.kt` | the View: `XScreen(component)` collects state, `XContent(state, ...)` is stateless and previewable | `designsystem.*`, Compose, `query.*` (`collectAsState`, `rememberMutation`) | `rpc.*` clients, Ktor |

A read-only screen has no State/Event: its component exposes the cached query and callbacks (see `courses/courses`).
Reference screens: `auth/login` (form + mutation) and `courses/courses` (read-only list). Router: `RootComponent.kt`.
Skill: `.claude/skills/new-mobile-screen`.
