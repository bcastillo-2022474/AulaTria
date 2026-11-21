import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { DoubtRound } from "../domain/doubtRound.js";
import { scheduleExpiry } from "../expiry.js";
import { getDoubtRound } from "../queries/getDoubtRound.js";
import { DoubtRoundRepository } from "../repository.js";

/** WRITE: the teacher opens a doubt round (default 30 s) in their own live lesson. At most one open round per lesson. */
export async function openDoubtRound(a: Actor, req: doubts.OpenDoubtRoundRequest) {
  const now = new Date();
  const round = await inTx(async (trx) => {
    const lesson = await new LessonRepository(trx).findForTeacher(a.schoolId, a.userId, req.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.assertLive();
    const repo = new DoubtRoundRepository(trx);
    // A round whose time ran out but is still marked open must not block the next one: settle it in this transaction.
    // A round that is really still open is refused by the partial unique index (DOUBT_ROUND_ALREADY_OPEN).
    const previous = await repo.findOpenForLesson(a.schoolId, lesson.id);
    if (previous?.expire(now)) await repo.saveRound(previous);
    const round = DoubtRound.open({ schoolId: a.schoolId, lessonId: lesson.id, durationSeconds: req.durationSeconds, now });
    await repo.insertRound(round);
    return round;
  });
  scheduleExpiry(a.schoolId, round);
  touch(a.schoolId, round.lessonId);
  return getDoubtRound(a, { roundId: round.id } as doubts.GetDoubtRoundRequest);
}
