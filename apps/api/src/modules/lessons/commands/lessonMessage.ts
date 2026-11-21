import { lessonStatusToProto } from "../../courses/queries/lessonStatus.js";
import type { Lesson } from "../domain/lesson.js";

/** Entity -> the `Lesson` proto message returned by every lifecycle command. */
export const toLessonMessage = (l: Lesson) => ({ id: l.id, courseId: l.courseId, number: l.number, status: lessonStatusToProto(l.status) });
