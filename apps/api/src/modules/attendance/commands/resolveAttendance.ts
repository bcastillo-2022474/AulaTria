import { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { getAttendanceRound } from "../queries/getAttendanceRound.js";
import { AttendanceRepository } from "../repository.js";

/**
 * WRITE: "Aceptar" (present) / "Ausente" (absent) on a record waiting for review. Works while the round is open AND after
 * it closed (the review screen is Docente-AsistCerrada). A student with no record in this round is NOT_FOUND: the
 * teacher can only decide on what was scanned. Repeating the same decision is a no-op; a different one on an already
 * decided record is ATTENDANCE_ALREADY_RESOLVED.
 */
export async function resolveAttendance(a: Actor, req: attendance.ResolveAttendanceRequest) {
  const round = await inTx(async (trx) => {
    const repo = new AttendanceRepository(trx);
    await repo.lockRoundExclusive(a.schoolId, req.roundId);
    const round = await repo.findRoundForTeacher(a.schoolId, a.userId, req.roundId);
    if (!round) throw new AppError(ErrorCode.ATTENDANCE_ROUND_NOT_FOUND);
    const record = await repo.findRecord(a.schoolId, round.id, req.studentId);
    if (!record) throw new AppError(ErrorCode.NOT_FOUND);
    record.resolve(req.decision === attendance.Decision.ACCEPT ? "accept" : "reject", a.userId, new Date());
    await repo.saveRecord(record);
    return round;
  });
  touch(a.schoolId, round.lessonId); // needs_review_count / present_count change
  return getAttendanceRound(a, { roundId: round.id } as attendance.GetAttendanceRoundRequest);
}
