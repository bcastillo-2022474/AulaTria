import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../hub.js";
import { LessonRepository } from "../repository.js";
import { toLessonMessage } from "./lessonMessage.js";

export async function endLesson(a: Actor, req: lessons.EndLessonRequest) {
  const now = new Date();
  const lesson = await inTx(async (trx) => {
    const repo = new LessonRepository(trx);
    const lesson = await repo.findForTeacher(a.schoolId, a.userId, req.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.end(now);
    await repo.save(lesson);
    await repo.closeOpenRounds(a.schoolId, lesson.id, now); // ending the class ends whatever is open in it
    return lesson;
  });
  touch(a.schoolId, lesson.id);
  return { lesson: toLessonMessage(lesson) };
}
