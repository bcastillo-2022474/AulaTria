---
name: new-web-screen
description: Build a teacher/admin screen in the AulaTria web app (React + Tailwind + typed connect-query hooks), faithful to docs/design. Use for any Docente-* or Admin-* screen.
---

# New web screen (teacher / admin, 1280x800)

Reference screens (copy their shape): `apps/web/src/features/auth/LoginPage.tsx` (form + mutation), `features/courses/CoursesPage.tsx` (read-only list), `CoursePage.tsx` (read + mutation + navigation). Routes: `apps/web/src/app/routes.tsx`. Conventions: `apps/web/src/features/README.md`. Design system: `docs/design-system/web.md`, gallery at `/#/design-system` (`mage web:dev`).

## 0. Before writing
1. Open the original: `cd docs/design && python3 -m http.server 8099` → `Docente-C-<Name>.dc.html` / `Admin-C-<Name>.dc.html`. **Every teacher/admin screen uses the deep-purple "stage" surface** (`<Screen>` default).
2. RPCs for the screen: `docs/UI-FLOWS.md`. All exist in the contract; a stub answers `unimplemented` until the backend lane lands (skill `new-rpc`).
3. Create `apps/web/src/features/<area>/<Name>Page.tsx` (+ small private components next to it only if reused in that file's folder). Add the route in `routes.tsx` under `// ROUTES`, behind `RequireAuth` with the right `roles`.

## 1. Compose from the design system only
`Screen`, `Content`, `AppBar`, `Heading`, `Text`, `Button`, `Card`, `Field`, `Badge`, `ActionTile`, `CourseTile`, `Meter`, `Countdown`, `OptionRow`, `QueueRow`, `PulseChart`... (see `design-system/index.ts`). **No raw hex, no `style={{}}`, no new CSS file.** Tailwind layout utilities (flex/grid/gap) are fine; colours/sizes/radii come from token utilities. Missing component → skill `change-design-system` first, gallery before the screen.

## 2. Data: only `src/rpc/hooks.ts`
```tsx
const courses = useAulaQuery(CoursesService.method.listCourses);          // typed from the proto
const open = useAulaMutation(LessonsService.method.openRoom, { onSuccess: (r) => navigate(`/lessons/${r.lesson!.id}/room`) });
```
- `import { courses, lessons } from "@aula/rpc"` (generated). Never import `@connectrpc/*` in a screen and never call a client directly.
- Queries poll where the proto says `refetch_seconds`; mutations **invalidate automatically** from the proto `invalidates`. Never call `invalidateQueries` yourself.
- Render **loading, error (with retry), empty and data**. Errors: `errorMessage(error)` (Spanish, exhaustive over `ErrorCode`); branch on `errorCode(error)` for behaviour.
- Live class screens: call `useLessonStream(lessonId)` once, then read `useAulaQuery(LessonsService.method.getLessonSnapshot, { lessonId })`; the stream keeps that cache fresh (polls as a fallback). Deadlines: `useCountdown(deadline, snapshot.serverTime)` from `rpc/time.ts`.
- Session/role: `useSession()`; the transport attaches `Authorization` + `X-School-Id` and refreshes expired tokens for you.
- A student must never be shown on web: `RequireAuth roles={["teacher","admin"]}`.

## 3. Rules
Spanish UI text copied from the original; English identifiers; no `any`; accessible markup (buttons are `<button>`, labelled fields). One screen = one component file; extract only what a second screen needs into `design-system/` (via `change-design-system`).

## 4. Verify (skill `verify`)
`mage web:check` (tsc + build). Then **look at it**: `mage web:dev`, log in as `ana.morales@sanmarcos.edu.gt / demo1234` (API running: `mage db:up db:migrate db:seed api:dev`), compare with the original at 1280x800. Say plainly if you could not look.
