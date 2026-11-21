import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import type { DoubtRound } from "../domain/doubtRound.js";
import { DoubtRoundRepository } from "../repository.js";

/**
 * Shared by ExtendDoubtRound and CloseDoubtRound: a teacher changes the state of a round they own.
 * The round is locked first, then loaded through the ownership lookup (not yours = DOUBT_ROUND_NOT_FOUND).
 * If its deadline already passed, the expiry is persisted and pushed FIRST (the transaction commits it) and only then
 * does the caller get DOUBT_ROUND_CLOSED, so a refusal never undoes the close.
 */
export async function changeRoundAsTeacher(a: Actor, roundId: string, change: (round: DoubtRound, now: Date) => void) {
  const now = new Date();
  const { round, expired } = await inTx(async (trx) => {
    const repo = new DoubtRoundRepository(trx);
    await repo.lockRound(a.schoolId, roundId);
    const round = await repo.findForTeacher(a.schoolId, a.userId, roundId);
    if (!round) throw new AppError(ErrorCode.DOUBT_ROUND_NOT_FOUND);
    if (round.expire(now)) {
      await repo.saveRound(round);
      return { round, expired: true };
    }
    change(round, now); // the entity refuses a round that is already closed
    await repo.saveRound(round);
    return { round, expired: false };
  });
  touch(a.schoolId, round.lessonId);
  if (expired) throw new AppError(ErrorCode.DOUBT_ROUND_CLOSED);
  return round;
}
