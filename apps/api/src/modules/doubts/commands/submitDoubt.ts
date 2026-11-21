import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { ts } from "../../../shared/ids.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { Doubt } from "../domain/doubt.js";
import { DoubtRoundRepository } from "../repository.js";

/**
 * WRITE: a student writes their doubt in an open round, or edits it (one per student per round: submitting again
 * updates the same row, keeping its id). Refused once the round is closed or past closes_at (DOUBT_ROUND_CLOSED).
 * Concurrent double taps cannot duplicate: the unique (round_id, student_id) makes the second one an update.
 */
export async function submitDoubt(a: Actor, req: doubts.SubmitDoubtRequest) {
  const now = new Date();
  const { round, doubt } = await inTx(async (trx) => {
    const repo = new DoubtRoundRepository(trx);
    await repo.lockRoundShared(a.schoolId, req.roundId);
    // Enrollment is part of the lookup. A round that does not exist in THIS school is NOT_FOUND (also another school's);
    // one that exists but belongs to a course the student is not in is COURSE_NOT_ENROLLED, as the contract documents.
    const round = await repo.findForStudent(a.schoolId, a.userId, req.roundId);
    if (!round) {
      if (await repo.findById(a.schoolId, req.roundId)) throw new AppError(ErrorCode.COURSE_NOT_ENROLLED);
      throw new AppError(ErrorCode.DOUBT_ROUND_NOT_FOUND);
    }
    round.assertAcceptsDoubts(now);
    const existing = await repo.findDoubt(a.schoolId, round.id, a.userId);
    const doubt = existing ?? Doubt.submit({ schoolId: a.schoolId, roundId: round.id, studentId: a.userId, text: req.text, now });
    if (existing) existing.replaceText(req.text, now);
    await repo.upsertDoubt(doubt);
    return { round, doubt };
  });
  touch(a.schoolId, round.lessonId); // the count (first submission) and the student's my_submitted live in the snapshot
  return { doubt: { text: doubt.text, editableUntil: ts(round.closesAt) } };
}
