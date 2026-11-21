import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { CourseRepository } from "../../courses/repository.js";
import { Lesson } from "../domain/lesson.js";
import { LessonRepository } from "../repository.js";
import { toLessonMessage } from "./lessonMessage.js";

export async function openRoom(a: Actor, req: lessons.OpenRoomRequest) {
  const lesson = await inTx(async (trx) => {
    const course = await new CourseRepository(trx).findForTeacher(a.schoolId, a.userId, req.courseId);
    if (!course) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
    const lessonsRepo = new LessonRepository(trx);
    const lesson = Lesson.open({ schoolId: a.schoolId, courseId: course.id, number: await lessonsRepo.nextNumber(a.schoolId, course.id), now: new Date() });
    await lessonsRepo.insert(lesson);
    return lesson;
  });
  // TODO(push): notify enrolled students (device table) once the push adapter exists. Until then the lobby shows live through the stream.
  return { lesson: toLessonMessage(lesson) };
}
