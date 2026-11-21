import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

// VITE_BASE: URL prefix the app is served under (production: /docentes/). Default "/" for dev.
export default defineConfig({ base: process.env.VITE_BASE ?? "/", plugins: [react(), tailwindcss()], // host: true = listen on every interface (IPv4 and IPv6), so other devices on the network can open it too.
  server: { port: 5173, host: true } });
