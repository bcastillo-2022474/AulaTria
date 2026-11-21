import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { FloorTurn } from "../domain/floorTurn.js";
import { getFloorRound } from "../queries/getFloorRound.js";
import { lockedRoundForStudent } from "./loadRound.js";

/** Idempotent. The first to raise answers at once; the rest queue. A repeat (or a turn already resolved) changes nothing. */
export async function raiseHand(a: Actor, req: floor.RaiseHandRequest) {
  const now = new Date();
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForStudent(trx, a, req.roundId);
    round.assertAcceptsHands(); // ending the lesson closes its rounds: a closed round answers first
    const lesson = await new LessonRepository(trx).findForStudent(a.schoolId, a.userId, round.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.assertLive();
    await repo.insertTurn(FloorTurn.raise({ schoolId: a.schoolId, roundId: round.id, studentId: a.userId, now })); // false = already had a turn
    const started = round.startNextTurn(await repo.findTurns(a.schoolId, round.id), now);
    if (started) await repo.saveTurn(started);
    return round.lessonId;
  });
  touch(a.schoolId, lessonId);
  return getFloorRound(a, { roundId: req.roundId } as floor.GetFloorRoundRequest);
}
