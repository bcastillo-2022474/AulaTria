import type { lessons } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { touch } from "../hub.js";
import { LessonRepository } from "../repository.js";
import { toLessonMessage } from "./lessonMessage.js";

export async function closeRoom(a: Actor, req: lessons.CloseRoomRequest) {
  const repo = new LessonRepository(db);
  const lesson = await repo.findForTeacher(a.schoolId, a.userId, req.lessonId);
  if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
  lesson.cancel(); // the entity enforces the state machine
  await repo.save(lesson);
  touch(a.schoolId, lesson.id);
  return { lesson: toLessonMessage(lesson) };
}
