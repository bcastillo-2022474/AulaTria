import { courses } from "@aula/rpc";

export const lessonStatusToProto = (s: string) =>
  ({ lobby: courses.LessonStatus.LOBBY, live: courses.LessonStatus.LIVE, ended: courses.LessonStatus.ENDED, cancelled: courses.LessonStatus.CANCELLED })[s]!;
