import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { subscribe } from "../hub.js";
import { buildSnapshot, forUser } from "./getLessonSnapshot.js";
import { visibleLesson } from "./visibility.js";

/**
 * READ, server-streaming. The first message is the snapshot, then one per change (the hub rebuilds and pushes).
 * The stream being open IS the student's presence (connected_count).
 */
const KEEPALIVE_MS = 30_000;

export async function* watchLesson(a: Actor, req: lessons.WatchLessonRequest, signal: AbortSignal) {
  await visibleLesson(a, req.lessonId);
  const sub = subscribe(req.lessonId, a);
  signal.addEventListener("abort", () => sub.close());
  try {
    yield { event: { case: "snapshot" as const, value: forUser(await buildSnapshot(a.schoolId, req.lessonId), a) } };
    // Keepalive: a proxy (Railway) closes a response that carries no data for 5 minutes. Quiet lessons are common, so
    // every KEEPALIVE_MS without an event the current snapshot is sent again (clients replace their cache: harmless).
    const it = sub[Symbol.asyncIterator]();
    let pending = it.next();
    for (;;) {
      let timer: NodeJS.Timeout | undefined;
      const tick = new Promise<"tick">((r) => { timer = setTimeout(() => r("tick"), KEEPALIVE_MS); });
      const got = await Promise.race([pending, tick]);
      clearTimeout(timer);
      if (got === "tick") {
        yield { event: { case: "snapshot" as const, value: forUser(await buildSnapshot(a.schoolId, req.lessonId), a) } };
        continue;
      }
      if (got.done) break;
      yield got.value;
      pending = it.next();
    }
  } finally {
    sub.close();
  }
}
