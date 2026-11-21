# AulaTria — MVP plan (for the judges' demo)

> Goal: show a working product to judges, fast. The DB is seeded, so we only build the flows that are
> **the demo**. Everything is online; nothing is offline.

## The story we are selling
*"Your class has more to say"*: the teacher **sees understanding in real time** (not at the exam), students take part with one tap,
and the app **turns confusion into action** (grouped doubts, a quiz drafted from the slides, a review of where the class got lost).

Two devices side by side is the whole pitch: teacher on a laptop (web), student on a phone (or, with zero install, the student app in a browser tab: `mage app:web`).

## Priority order (and why)
Ranked by *judge impact ÷ cost*, with dependencies respected. Each tier is demoable on its own, so we can cut from the bottom.

| # | Flow | Why it is here | Cost | Risk |
|---|---|---|---|---|
| **P0** | Foundation: design systems, auth, course lists, lesson lifecycle, live snapshot stream, seed | Everything depends on it. **Done in the skeleton** (see status below) | — | low |
| **P1** | **The live loop**: teacher "Iniciar sala" → students see **EN VIVO AHORA** and join (counter ticks up) → "Empezar clase" → students tap **Voy bien / Me perdí** → the teacher's number and bar move **live** | The single most visual moment of the pitch, and it exercises the hardest tech (realtime). If only one thing works, it is this. Screens: Docente-Cursos, -Curso, -SalaAbierta, -Live (pulse part); Estudiante-Cursos, -Home | M | medium (streaming on the demo device; polling is the fallback) |
| **P2** | **Review of a past class** (Docente-Pulso, Docente-Perfil): pulse timeline with quiz/doubt markers, "EN ESTE MOMENTO: 9 of 38 got lost on slide 27", per-student participation + private note | Read-only over **seeded** lesson 11, so it works even if a live flow glitches: it is our safety net and it shows the *insight*, not just the gadgets. Needs only `GetLessonReview`, `ListCourseStudents`, `GetStudentParticipation`, `SaveStudentNote` | M | low |
| **P3** | **Doubt round + AI grouping**: teacher opens 30 s round → students write doubts → close → "3 dudas principales", crossed with the slides | The "intelligence" moment. Falls back to a deterministic grouping when no LLM key/network, so it cannot fail on stage. Screens: Docente-Live→DudasAbierta→DudasResumen; Estudiante-Dudas, -DudaEnviada, -DudaGrupo | M | medium (LLM) |
| **P4** | **Live quiz**: one click drafts a question from the PDF (the seeded lesson-11 material) → launch → bars fill live → result "48 % acertó" + insight | Second "AI" wow and a second realtime moment. Screens: QuizArmar, QuizVivo, QuizResultados; Estudiante-Quiz, -QuizResultado | M | medium (LLM) |
| **P5** | **Floor** ("toma de la palabra"): raise a hand, queue with +0,8 s offsets, "Acertó / No acertó · siguiente" | Fun, tactile, and a race the audience understands. Mostly state machine | M | low |
| **P6** | **Attendance with QR**: rotating code + student scan | Impressive but camera/location APIs differ per platform. **MVP version: the student types the code** shown on the teacher's screen (QR rendering on the teacher, no camera scan); location judged by code only unless a browser/phone provides it | L | high (platform APIs) |
| **P7** | Onboarding: student registers with a join code, teacher creates a course, admin invites teachers | Seeded, so skip live; show the registration screen once if time allows (join code → account) since it explains multi-school | S–M | low |

**Cut line if time runs out: after P4.** P5–P7 are contract-complete; they simply have no screens.
**Do not build:** recording, activity assignment/extra points, per-school branding, push notifications (the lobby shows live through the stream; a push is a later nicety).

## Demo script (≈5 min)
1. Teacher (web) logs in → courses. Phone/browser student logs in (Marta).
2. **Iniciar sala** → student's card flips to EN VIVO AHORA → **Entrar** → teacher's "24 de 38 se han unido" ticks. **Empezar clase.**
3. Student taps **Me perdí** (a few other seeded students can be driven by a small script) → teacher's % drops live.
4. **Preguntar dudas** (30 s) → students type → **3 dudas principales**, slide-referenced. (P3)
5. **Lanzar quiz** → drafted from the PDF → launch → live bars → "48 % acertó". (P4)
6. **Terminar clase** → open last week's review: the pulse dips at minute 31, markers show the doubt round and the quiz exactly there. Open a student's profile. (P2)
7. Close with the architecture slide: one contract (proto), one tenant-safe API, the same design tokens on web and phone.

Rehearsal rules: `mage db:seed` right before; keep API + web + browser-student on one machine as the fallback; have a 60 s screen recording of the happy path.

## Parallel work after the design systems
The contract exists for everything above (`proto/`), so lanes do not wait for each other:
- **Backend lane** (skill `new-rpc`): implement handlers in this order: review/followup (P2), doubts (P3), quiz (P4), floor (P5), attendance (P6). Each is an independent module.
- **Web lane** (skill `new-web-screen`): Docente-* screens by priority, from `apps/web/src/features/`.
- **Mobile lane** (skill `new-mobile-screen`): Estudiante-* screens by priority.
A lane may stub against the generated client and the seed while the handler is pending (an RPC with no handler answers `unimplemented`).

## Status of the skeleton (what exists today)
| Area | State |
|---|---|
| DBML (22 tables, tenant on every table, composite FKs) | complete; `mage db:sql` generates migration 001 |
| Contract (`proto/`, 9 services, ~55 RPCs, error codes, hints) | complete for all flows |
| API handlers | **implemented**: identity (Login, RefreshSession, Logout, GetMe, LookupJoinCode, RegisterStudent, RegisterDevice), courses (all 5), lessons (OpenRoom, CloseRoom, StartLesson, EndLesson, JoinRoom, GetLessonSnapshot, **WatchLesson stream**, SetPulse). **Contract only** (answer `unimplemented`): invitations/admin, review, materials, doubts, quizzes, floor, attendance, followup |
| Seed | school, admin, 3 teachers, 38 students, 2 courses, 11 past lessons with pulse/attendance/doubts/quiz/floor/notes + material text |
| Smoke test | auth, tenant header, error codes, role scoping, realtime stream (`mage api:smoke`) |
| Web | design system + gallery, typed data hooks (generated invalidation), login |
| Mobile (Compose: Android, iOS, browser) | design system + gallery, generated clients + query layer, Connect transport with streaming, session, container |
| CI | `.github/workflows/ci.yml` (+ `mage fast` / `mage check` locally) |

## Open decisions / things I am not sure about
- **Ktor streaming on iOS (Darwin) and wasm is unverified**: the docs do not say. Android/OkHttp and the web (connect-web) path are the safe ones. Polling covers the gap; spike it on a real iPhone/Safari early.
- Compose for Web (wasm) was **Beta** at last check; pbandk's wasm support is "experimental". It compiles in CI from day one so it cannot rot, but treat the browser student app as demo-grade until proven.
- QR scanning (camera) is not in the MVP; location is optional.
- LLM provider/key for doubt grouping and quiz drafts is not chosen (the adapter in `apps/api/src/ai/` does not exist yet: whoever builds it must include a deterministic fallback).
