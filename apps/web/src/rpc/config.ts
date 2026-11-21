/**
 * Where the API is.
 *   - Production (gateway): "/api" on the page's own origin, no CORS (set VITE_API_URL=/api at build time).
 *   - Dev default: the SAME host the page was opened from, port 3000 (localhost:5173 -> localhost:3000, 10.0.0.5:5173 -> 10.0.0.5:3000).
 * Override with VITE_API_URL (apps/web/.env.local) for anything else. A relative value is resolved against the page.
 */
const rawApi: string = import.meta.env.VITE_API_URL ?? `${location.protocol}//${location.hostname}:3000`;
export const API_URL: string = rawApi.startsWith("/") ? `${location.origin}${rawApi}` : rawApi;

/**
 * Where the STUDENT app lives (its browser build). Default: same host as this page, port 8080. A student who signs in here
 * is offered a link to it instead of a dead end. Override with VITE_STUDENT_APP_URL (production: https://estudiantes.aulatria.sudoersgt.dev).
 */
export const STUDENT_APP_URL: string = import.meta.env.VITE_STUDENT_APP_URL ?? `${location.protocol}//${location.hostname}:8080`;
