# Screens (features)

- One folder per area (`auth/`, `courses/`, ...). One file per screen: `features/<area>/<ScreenName>Page.tsx`, named after the design (`Docente-C-Cursos` becomes `CoursesPage.tsx`).
- A screen uses only the design system (`../../design-system`) and the rpc hooks (`../../rpc/hooks`, `../../rpc/session`). Never import `@connectrpc/*` or a client, never write HTTP, never invalidate queries by hand.
- No raw colours, sizes or inline styles: design-system components and token utilities only. Layout utilities (flex, gap, grid) are fine.
- Identifiers in English, user-facing text in Spanish. Copy an existing screen (e.g. `courses/CoursesPage.tsx`) as the template.
- Add a route in `apps/web/src/app/routes.tsx` (hash router): import the page, add an entry in its area, wrap it in `<RequireAuth roles={[...]}>`. Links are `href="#/path"`.
- Helpers used by one area live in that area's folder (e.g. `courses/format.ts`).
