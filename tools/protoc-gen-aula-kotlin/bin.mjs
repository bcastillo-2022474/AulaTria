#!/usr/bin/env node
// buf runs plugins as executables: load tsx so the plugin can be plain TypeScript.
import { register } from "tsx/esm/api";
register();
await import("./plugin.ts");
