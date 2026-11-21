---
name: verify
description: What to run before saying a change in AulaTria is done - contract drift, typechecks, builds, smoke test. Use at the end of every task, and when something "should work" but you have not run it.
---

# Verify

Never say "done" or "works" for something you did not run. State exactly what you ran and what you could not.

| You changed | Run | Proves |
|---|---|---|
| a `.proto` | `mage proto:check` | lints (buf), regenerates TS + Kotlin, fails if committed generated code was stale or hints are inconsistent |
| DBML / migration | `mage db:reset db:migrate db:types db:seed` | schema applies from zero, types regenerate, seed still runs |
| `apps/api` | `mage api:check` (+ restart `api:dev`) | typecheck |
| a flow of the API | `mage db:seed api:smoke` with the API running | auth, tenant enforcement, error codes, role scoping, realtime stream, through the generated TS client |
| `apps/web` | `mage web:check` | typecheck + production build |
| `apps/app` | `mage app:check` | wasmJs + Android compile (iOS compiles only on a Mac / CI) |
| anything | `mage fast` | contract + api + web in one go (what CI runs minus Gradle) |

Checklist before reporting:
1. Generated files are committed with the change and `mage proto:check` is clean (no warnings).
2. Tenant: every new query filters by `school_id` from the actor; the smoke test still passes with the wrong `X-School-Id` refused.
3. Roles: the new RPC refuses the wrong role; a student never gets teacher-only fields.
4. UI: if a screen changed, it was opened in a browser (web: `mage web:dev`, design system at `/#/design-system`; student: `mage app:web`) and compared with `docs/design/<Screen>.dc.html` (serve with `python3 -m http.server` in `docs/design`). If you could not look at it, say so.
5. Realtime changes: checked with two sessions (teacher + student) against `mage api:dev`, not just typechecked.
6. No raw hex/px colours outside token files; no `any`; no `console.log` left.

Known limits to mention honestly when relevant: iOS cannot be compiled on Linux; Ktor streaming on iOS/wasm is unverified (polling covers it); there are no automated tests by design (this prototype's safety net is the contract, typechecks and the smoke script).
