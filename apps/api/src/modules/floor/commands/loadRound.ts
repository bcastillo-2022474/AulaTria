import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import type { Executor } from "../../../shared/tx.js";
import { FloorRoundRepository } from "../repository.js";

/**
 * Write-side load of a round the actor may change. The round is locked first (the queue of one round is changed by
 * one command at a time), then loaded through the ownership/enrollment lookup. Not visible = FLOOR_ROUND_NOT_FOUND.
 */
export async function lockedRoundForTeacher(trx: Executor, a: Actor, roundId: string) {
  const repo = new FloorRoundRepository(trx);
  await repo.lockRound(a.schoolId, roundId);
  const round = await repo.findForTeacher(a.schoolId, a.userId, roundId);
  if (!round) throw new AppError(ErrorCode.FLOOR_ROUND_NOT_FOUND);
  return { repo, round };
}

export async function lockedRoundForStudent(trx: Executor, a: Actor, roundId: string) {
  const repo = new FloorRoundRepository(trx);
  await repo.lockRound(a.schoolId, roundId);
  const round = await repo.findForStudent(a.schoolId, a.userId, roundId);
  if (!round) throw new AppError(ErrorCode.FLOOR_ROUND_NOT_FOUND);
  return { repo, round };
}
