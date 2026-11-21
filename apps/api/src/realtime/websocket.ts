import type { Server } from "node:http";

/**
 * WebSocket realtime endpoint: `ws(s)://<api>/realtime/lessons` (contract: the TRANSPORT DECISION block in
 * proto/aula/lessons/v1/lessons.proto, message RealtimeHello).
 *
 * STUB. TODO(contributor), in this order (skill `touch-realtime`):
 *   1. `pnpm --filter @aula/api add ws @types/ws`, then `new WebSocketServer({ noServer: true })` and handle `server.on("upgrade")`
 *      for the path `/realtime/lessons` only (leave every other upgrade alone).
 *   2. Wait for the FIRST binary frame, decode it with `fromBinary(lessons.RealtimeHelloSchema, ...)`; anything else: ignore.
 *   3. Authenticate exactly like the HTTP interceptor (`verifyAccessToken` from shared/context.ts, tenant must equal the
 *      token's school) and check `visibleLesson(actor, lessonId)`. Close with 4401 / 4403 / 4404 on failure.
 *   4. `subscribe(lessonId, actor)` from modules/lessons/hub.ts (the SAME hub WatchLesson uses), and for each item send
 *      `toBinary(lessons.LessonEventSchema, create(lessons.LessonEventSchema, item))` as a binary frame. First frame = snapshot.
 *   5. On `close`/`error` call `sub.close()` (that is what updates connected_count). Heartbeat ping every 15 s.
 * Do NOT duplicate any rule from queries/watchLesson.ts: extract what both need into a shared function.
 */
export function attachLessonSocket(_server: Server): void {
  // intentionally empty until implemented
}
