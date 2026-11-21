import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { subscribe } from "../hub.js";
import { buildSnapshot, forUser } from "./getLessonSnapshot.js";
import { visibleLesson } from "./visibility.js";

/**
 * READ, server-streaming. The first message is the snapshot, then one per change (the hub rebuilds and pushes).
 * The stream being open IS the student's presence (connected_count).
 */
export async function* watchLesson(a: Actor, req: lessons.WatchLessonRequest, signal: AbortSignal) {
  await visibleLesson(a, req.lessonId);
  const sub = subscribe(req.lessonId, a);
  signal.addEventListener("abort", () => sub.close());
  try {
    yield { event: { case: "snapshot" as const, value: forUser(await buildSnapshot(a.schoolId, req.lessonId), a) } };
    for await (const e of sub) yield e;
  } finally {
    sub.close();
  }
}
