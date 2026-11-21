/**
 * The ONLY data-access layer of the web app. Screens use these four hooks and never a raw client:
 *
 *   useAulaQuery(method, input)     typed query (connect-query) + `refetch_seconds` polling from the proto
 *   useAulaMutation(method)         typed mutation + automatic invalidation from the proto `invalidates` option
 *   useLessonStream(lessonId)       the realtime stream, patching the lesson snapshot cache
 *   errorMessage(error) / errorCode(error)   Spanish sentence / domain error code (ErrorCode enum name) of a failed call
 *
 * The hints are GENERATED (packages/rpc/src/hints); forgetting an invalidation is a proto change, not a screen change.
 */
import { useEffect } from "react";
import { ConnectError, createClient } from "@connectrpc/connect";
import {
  createConnectQueryKey, useMutation, useQuery, useTransport,
  type UseMutationOptions, type UseQueryOptions,
} from "@connectrpc/connect-query";
import { useQueryClient } from "@tanstack/react-query";
import { create, type DescMessage, type DescMethodUnary, type MessageInitShape, type MessageShape } from "@bufbuild/protobuf";
import { attendance, courses, doubts, floor, followup, identity, invalidatesHints, lessons, quizzes, refetchHints } from "@aula/rpc";

const services = [
  identity.IdentityService, identity.AdminService, courses.CoursesService, lessons.LessonsService, doubts.DoubtsService,
  quizzes.QuizService, floor.FloorService, attendance.AttendanceService, followup.FollowupService,
];
const methodByName = new Map<string, DescMethodUnary>();
for (const s of services) for (const m of s.methods) if (m.methodKind === "unary") methodByName.set(`${s.typeName}/${m.name}`, m as DescMethodUnary);
const nameOf = (m: { parent: { typeName: string }; name: string }) => `${m.parent.typeName}/${m.name}`;

export function useAulaQuery<I extends DescMessage, O extends DescMessage>(
  method: DescMethodUnary<I, O>,
  input?: MessageInitShape<I>,
  options?: Partial<UseQueryOptions<O>>,
) {
  const every = refetchHints[nameOf(method)];
  return useQuery<I, O>(method, input, { refetchInterval: every ? every * 1000 : false, ...options } as UseQueryOptions<O>);
}

export function useAulaMutation<I extends DescMessage, O extends DescMessage>(
  method: DescMethodUnary<I, O>,
  options?: UseMutationOptions<I, O>,
) {
  const queryClient = useQueryClient();
  return useMutation(method, {
    ...options,
    onSuccess: async (...args) => {
      for (const name of invalidatesHints[nameOf(method)] ?? []) {
        const target = methodByName.get(name);
        if (target) await queryClient.invalidateQueries({ queryKey: createConnectQueryKey({ schema: target as DescMethodUnary<DescMessage, DescMessage>, cardinality: undefined }) });
      }
      return options?.onSuccess?.(...args);
    },
  });
}

/**
 * Keeps GetLessonSnapshot fresh from the realtime stream while a lesson screen is mounted. The server
 * pushes a full snapshot on every change, so the cache is simply replaced. If the stream drops, the
 * query's own polling (refetch_seconds) keeps the screen correct; this hook then reconnects.
 */
export function useLessonStream(lessonId: string | undefined) {
  const transport = useTransport();
  const queryClient = useQueryClient();
  useEffect(() => {
    if (!lessonId) return;
    const ac = new AbortController();
    const client = createClient(lessons.LessonsService, transport);
    (async () => {
      while (!ac.signal.aborted) {
        try {
          for await (const ev of client.watchLesson({ lessonId }, { signal: ac.signal })) {
            if (ev.event.case === "snapshot") {
              queryClient.setQueryData(
                createConnectQueryKey({ schema: lessons.LessonsService.method.getLessonSnapshot, input: { lessonId }, cardinality: "finite" }),
                create(lessons.GetLessonSnapshotResponseSchema, { snapshot: ev.event.value }),
              );
            }
          }
        } catch (e) {
          if (ac.signal.aborted) return;
          console.warn("lesson stream dropped, retrying", e);
        }
        await new Promise((r) => setTimeout(r, 1500));
      }
    })();
    return () => ac.abort();
  }, [lessonId, transport, queryClient]);
}



export { errorCode, errorMessage } from "./errors";
