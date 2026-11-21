import { lessons } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { touch } from "../hub.js";
import { LessonRepository } from "../repository.js";

export async function setPulse(a: Actor, req: lessons.SetPulseRequest) {
  const repo = new LessonRepository(db);
  const lesson = await repo.findForStudent(a.schoolId, a.userId, req.lessonId);
  if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
  lesson.assertLive();
  await repo.appendPulse(a.schoolId, lesson.id, a.userId, req.state === lessons.PulseState.OK ? "ok" : "lost", new Date());
  touch(a.schoolId, lesson.id);
  return {};
}
