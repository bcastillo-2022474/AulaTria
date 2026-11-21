
import { buildSnapshot, forUser } from "./queries/getLessonSnapshot.js";

/**
 * In-process fan-out for WatchLesson: lessonId -> open streams. One API instance for the MVP;
 * Postgres LISTEN/NOTIFY is the upgrade path (only this file changes).
 *
 * Every change just calls `touch(lessonId)`: the snapshot is rebuilt (debounced) and pushed as a
 * `snapshot` event. The finer-grained events in the proto (pulse, round_*) are for clients that
 * want to patch instead of replace; the server can start emitting them without a contract change.
 */
type Snap = ReturnType<typeof forUser>;
const asEvent = (snapshot: Snap) => ({ event: { case: "snapshot" as const, value: snapshot } });
type Event = ReturnType<typeof asEvent>;

interface Sub { userId: string; role: string; schoolId: string; push(e: Event): void }
const subs = new Map<string, Set<Sub>>();
const timers = new Map<string, NodeJS.Timeout>();

export function connectedStudents(lessonId: string) {
  return new Set([...(subs.get(lessonId) ?? [])].filter((s) => s.role === "student").map((s) => s.userId)).size;
}

/** Opens a stream for one viewer. The returned iterator ends when `close()` is called. */
export function subscribe(lessonId: string, who: { userId: string; role: string; schoolId: string }) {
  const queue: Event[] = [];
  let wake: (() => void) | null = null;
  let closed = false;
  const sub: Sub = { ...who, push: (e) => { queue.push(e); wake?.(); } };
  (subs.get(lessonId) ?? subs.set(lessonId, new Set()).get(lessonId)!).add(sub);
  touch(who.schoolId, lessonId); // presence changed
  return {
    close() {
      if (closed) return;
      closed = true;
      subs.get(lessonId)?.delete(sub);
      touch(who.schoolId, lessonId);
      wake?.();
    },
    async *[Symbol.asyncIterator]() {
      while (!closed) {
        if (queue.length) yield queue.shift()!;
        else await new Promise<void>((r) => (wake = r));
      }
    },
  };
}

/** Something about this lesson changed. Coalesces bursts into one rebuild. */
export function touch(schoolId: string, lessonId: string) {
  if (timers.has(lessonId)) return;
  timers.set(lessonId, setTimeout(async () => {
    timers.delete(lessonId);
    const open = subs.get(lessonId);
    if (!open?.size) return;
    try {
      const built = await buildSnapshot(schoolId, lessonId);
      for (const s of open) s.push(asEvent(forUser(built, s)));
    } catch (e) {
      console.error("hub refresh failed", lessonId, e);
    }
  }, 120));
}

