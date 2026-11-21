# Documentation: AulaTria

Read product first, then the architecture and the contract. The build order is the reverse of the stack: **DBML → proto → generated code → handler → screen.** The rules every contributor follows are in [CLAUDE.md](../CLAUDE.md).

## Reading order

| Document | What it covers |
|---|---|
| [PRODUCT.md](./PRODUCT.md) | What AulaTria is, who uses it, and the principles the design follows |
| [MVP.md](./MVP.md) | The demo: priorities P0 to P7, the demo script, what is built today, open decisions |
| [ARCHITECTURE.md](./ARCHITECTURE.md) | Stack and the decisions behind it (with rejected alternatives), backend layout, tenancy, errors, realtime, clients, risks |
| [CONTRACT.md](./CONTRACT.md) | The protobuf contract: layout, conventions, RPC catalogue with status, errors, realtime, how clients consume it, open questions |
| [UI-FLOWS.md](./UI-FLOWS.md) | Every screen mapped to the RPCs it calls, and the lesson state machine |
| [CHASSIS-REVIEW.md](./CHASSIS-REVIEW.md) | What we copied, simplified and dropped from the Chassis reference repo (`~/work/chassis`) |
| [DECISIONS.md](./DECISIONS.md) | Decisiones tomadas, propuestas y preguntas abiertas (transporte en vivo, Git, LLM, materiales, menores, tests, admin) |
| [CLIENT-RPC.md](./CLIENT-RPC.md) | How both clients talk to the API (web hooks, Compose query layer, auth, errors, realtime): everything is built, you only call it |

## Reference material

| Path | What it is |
|---|---|
| [db_schema/db_schema.dbml](./db_schema/db_schema.dbml) | Persistence. Written first; the source of truth for tables (22 tables; every one except the `school` root carries `school_id`) |
| [design-system/web.md](./design-system/web.md) | Web design system (teacher and admin, 1280x800): tokens, components, rules |
| [design-system/mobile.md](./design-system/mobile.md) | Mobile design system (student, 390x844): tokens, components, rules |
| [design/](./design/README.md) | The design export: 42 HTML artboards (22 teacher and admin, 20 student) and `canvas.json`. View with `python3 -m http.server` in that folder |
| `brand/` | Logos (to be added) |

## Contributor skills

The skills in [`.claude/skills/`](../.claude/skills) walk through each kind of change: `new-table`, `new-rpc`, `new-error-code`, `new-web-screen`, `new-mobile-screen`, `change-design-system`, `touch-realtime` and `verify`. CLAUDE.md's table says which one to use for each change.
