import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { getAttendanceRound } from "../queries/getAttendanceRound.js";
import { AttendanceRepository } from "../repository.js";

/**
 * WRITE: the teacher closes their round. The exclusive lock waits for the check-ins in flight, so no scan lands after
 * the close. Closing twice is ATTENDANCE_ROUND_CLOSED. The review list stays available for ResolveAttendance.
 */
export async function closeAttendanceRound(a: Actor, req: attendance.CloseAttendanceRoundRequest) {
  const round = await inTx(async (trx) => {
    const repo = new AttendanceRepository(trx);
    await repo.lockRoundExclusive(a.schoolId, req.roundId);
    const round = await repo.findRoundForTeacher(a.schoolId, a.userId, req.roundId);
    if (!round) throw new AppError(ErrorCode.ATTENDANCE_ROUND_NOT_FOUND);
    round.close(new Date());
    await repo.saveRound(round);
    return round;
  });
  touch(a.schoolId, round.lessonId);
  return getAttendanceRound(a, { roundId: round.id } as attendance.GetAttendanceRoundRequest);
}
