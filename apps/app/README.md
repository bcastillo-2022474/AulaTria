# Student app: navigation notes

`RootComponent` (feature/) is the only place that knows the flow. The platform entry creates the root `ComponentContext` once and passes it to `App`:
Android `defaultComponentContext()` (system back button + state restore), iOS a resumed `DefaultComponentContext`, browser `withWebHistory { }`.

## Browser build: real URLs

`/login`, `/courses`, `/lessons/<lessonId>`, `/lessons/<lessonId>/floor/<roundId>`, `/lessons/<lessonId>/quiz/<quizId>`.
Back/forward and reload work; signed out, the requested URL is kept and opened after login. Query parameters (`?api=http://host:3000`,
`?gallery`) are kept on every URL. The old `?go=floor/...` link is gone: use the real path.

**Hosting needs an SPA fallback**: every path without a file extension must return `index.html` (nginx `try_files $uri /index.html;`,
Netlify `/* /index.html 200`, S3/CloudFront: 404 -> `/index.html`). `/aulatria.js`, the `.wasm` files and `/composeResources/...` are
requested from the site root, so the app must be served from `/` (not from a sub-path). The dev server (`mage app:web`) already does it
(`shared/webpack.config.d/devServer.js`; set `AULA_DEV_PORT` to run a second one on another port).

## Android back button

Pops the screen; on the first screen (login/courses) it falls through and closes the app. Verify on a phone: log in, open a live
lesson, press back (returns to courses), press back again (app closes); rotate the phone on a lesson screen (stays on it).
