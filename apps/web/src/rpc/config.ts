/**
 * Where the API is. Default: the SAME host the page was opened from, port 3000. So opening the web app as
 * http://localhost:5173 talks to localhost:3000, and opening it from another device as http://10.0.0.5:5173 talks to
 * 10.0.0.5:3000. Override with VITE_API_URL (apps/web/.env.local) for anything else.
 */
export const API_URL: string = import.meta.env.VITE_API_URL ?? `${location.protocol}//${location.hostname}:3000`;
