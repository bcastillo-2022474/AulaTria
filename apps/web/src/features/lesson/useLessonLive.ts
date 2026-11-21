import { lessons } from "@aula/rpc";
import { useAulaQuery, useLessonStream } from "../../rpc/hooks";

/**
 * THE way a live-class screen reads "the class right now": the cached `GetLessonSnapshot` (polled every few seconds as
 * a fallback) kept fresh by the WatchLesson stream while the screen is mounted. When the snapshot's `revision` moves,
 * round queries (floor, quiz...) are refetched immediately (done inside useLessonStream).
 *
 *   const { snapshot } = useLessonLive(lessonId);   // snapshot is undefined until the first response
 */
export function useLessonLive(lessonId: string) {
  useLessonStream(lessonId);
  const query = useAulaQuery(lessons.LessonsService.method.getLessonSnapshot, { lessonId });
  return { snapshot: query.data?.snapshot, query };
}
