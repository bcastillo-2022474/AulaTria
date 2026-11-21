import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { FloorRound } from "../domain/floorRound.js";
import { FloorRoundRepository } from "../repository.js";
import { getFloorRound } from "../queries/getFloorRound.js";

export async function openFloorRound(a: Actor, req: floor.OpenFloorRoundRequest) {
  const round = await inTx(async (trx) => {
    const lesson = await new LessonRepository(trx).findForTeacher(a.schoolId, a.userId, req.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.assertLive();
    const round = FloorRound.open({ schoolId: a.schoolId, lessonId: lesson.id, prompt: req.prompt, now: new Date() });
    await new FloorRoundRepository(trx).insertRound(round); // a second open round is refused by the repository
    return round;
  });
  touch(a.schoolId, round.lessonId);
  return getFloorRound(a, { roundId: round.id } as floor.GetFloorRoundRequest);
}
