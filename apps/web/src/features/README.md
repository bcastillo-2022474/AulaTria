# Screens (features)

- One folder per area (`auth/`, `courses/`, ...). One file per screen: `features/<area>/<ScreenName>Page.tsx`, named after the design (`Docente-C-Cursos` becomes `CoursesPage.tsx`).
- A screen uses only the design system (`../../design-system`) and the rpc hooks (`../../rpc/hooks`, `../../rpc/session`). Never import `@connectrpc/*` or a client, never write HTTP, never invalidate queries by hand.
- No raw colours, sizes or inline styles: design-system components and token utilities only. Layout utilities (flex, gap, grid) are fine.
- Identifiers in English, user-facing text in Spanish. Copy an existing screen (e.g. `courses/CoursesPage.tsx`) as the template.
- Add a route in `apps/web/src/app/routes.tsx`: import the page, add an entry in its area, wrap it in `<RequireAuth roles={[...]}>`. Links are `href="/path"`.
- Helpers used by one area live in that area's folder (e.g. `courses/format.ts`).

## Routing (real paths, refresh-safe, query strings)

- URLs are real paths: `/courses/123`, `/lessons/<id>/floor`, `/lessons/<id>/quiz/<quizId>`. The page is in the URL, so a refresh or a shared link opens the same screen; the session lives in localStorage.
- Links: `<Button href="/courses/new">`, `<CourseTile href={`/courses/${id}`}>`, or `navigate("/courses")` (react-router). Never `<a href>` by hand: the design-system components use `AppLink`, which navigates without a reload.
- Query parameters, when a screen has view state worth keeping in the URL (a tab, a filter): `const [params, setParams] = useSearchParams(); params.get("tab"); setParams({ tab: "past" })`. Put IDs in the path and view options in the query.
- Signed-out visitors are sent to `/login?next=<where they were>` and come back after signing in.
- Dev server (`mage web:dev`) already falls back to `index.html` for unknown paths. In PRODUCTION the web server must do the same, e.g. Caddy: `try_files {path} /index.html`; nginx: `try_files $uri /index.html;`; `vite preview` does it by itself.
- Served under a prefix (production: `/docentes/`)? Build with `VITE_BASE=/docentes/`. The router `basename` comes from it, so paths in code, links and `?next=` stay prefix-free; the host strips the prefix and falls back to `index.html`.
