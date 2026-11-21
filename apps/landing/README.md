# Landing

The public page at `https://aulatria.sudoersgt.dev/`: static HTML, no build step. Moved here from `github.com/ElDaVz/aulatria` (where it was published with GitHub Pages).
Served by Caddy from the production container (`deploy/Caddyfile`); the teacher app lives at `/docentes/` and the student app at `/estudiantes/`.
The waitlist forms post to formsubmit.co (the address is in the `action` attribute of each `<form>` and in `FORM_ENDPOINT`).
Edit `index.html`/`assets/` and merge to `master`: the deploy workflow publishes it with everything else. Preview locally: `python3 -m http.server -d apps/landing 8099`.
