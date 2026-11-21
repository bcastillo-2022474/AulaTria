import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { lockedRoundForStudent } from "./loadRound.js";

/** "Ya respondi": the answering student waits for the verdict. FLOOR_NOT_YOUR_TURN for anyone else. */
export async function finishMyTurn(a: Actor, req: floor.FinishMyTurnRequest) {
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForStudent(trx, a, req.roundId);
    round.assertAcceptsHands();
    const turn = await repo.findTurn(a.schoolId, round.id, a.userId);
    if (!turn) throw new AppError(ErrorCode.FLOOR_NOT_YOUR_TURN);
    turn.finish(); // the entity enforces answering -> answered
    await repo.saveTurn(turn);
    return round.lessonId;
  });
  touch(a.schoolId, lessonId);
  return {};
}
