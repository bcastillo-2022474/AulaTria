import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({ plugins: [react(), tailwindcss()], // host: true = listen on every interface (IPv4 and IPv6), so other devices on the network can open it too.
  server: { port: 5173, host: true } });
