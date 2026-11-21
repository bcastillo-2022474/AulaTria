import type { lessons } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { touch } from "../hub.js";
import { LessonRepository } from "../repository.js";

export async function joinRoom(a: Actor, req: lessons.JoinRoomRequest) {
  const repo = new LessonRepository(db);
  const lesson = await repo.findForStudent(a.schoolId, a.userId, req.lessonId);
  if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
  lesson.assertAcceptsParticipants();
  await repo.addParticipant(a.schoolId, lesson.id, a.userId, new Date());
  touch(a.schoolId, lesson.id);
  return {};
}
