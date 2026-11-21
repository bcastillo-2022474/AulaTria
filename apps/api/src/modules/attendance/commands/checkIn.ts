import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { AttendanceRecord } from "../domain/attendanceRecord.js";
import { assertCoordinates, type ScanLocation } from "../domain/attendanceRound.js";
import { reasonToProto, statusToProto } from "../mappers.js";
import { AttendanceRepository } from "../repository.js";

/**
 * WRITE: a student scans the QR or types the code. Order of refusals: round not in this student's courses
 * (COURSE_NOT_ENROLLED / ATTENDANCE_ROUND_NOT_FOUND), round closed, wrong or expired code (ATTENDANCE_QR_INVALID).
 * A good code ALWAYS records attendance: the location only decides PRESENT vs NEEDS_REVIEW (never an error). The distance
 * is measured here from the coordinates; nothing derived is trusted from the client. One current record per student
 * and round: a rescan rewrites a pending one, repeats a PRESENT, and is refused once the teacher rejected it.
 */
export async function checkIn(a: Actor, req: attendance.CheckInRequest) {
  const now = new Date();
  const { round, record } = await inTx(async (trx) => {
    const repo = new AttendanceRepository(trx);
    await repo.lockRoundShared(a.schoolId, req.roundId);
    const round = await repo.findRoundForStudent(a.schoolId, a.userId, req.roundId);
    if (!round) {
      if (await repo.roundExists(a.schoolId, req.roundId)) throw new AppError(ErrorCode.COURSE_NOT_ENROLLED);
      throw new AppError(ErrorCode.ATTENDANCE_ROUND_NOT_FOUND);
    }
    round.assertOpen();
    if (!round.verifyCode(req.code, now)) throw new AppError(ErrorCode.ATTENDANCE_QR_INVALID);

    let location: ScanLocation = null;
    if (req.hasLocation) {
      assertCoordinates({ lat: req.lat, lng: req.lng });
      location = { lat: req.lat, lng: req.lng, accuracyM: Math.max(0, req.accuracyM) };
    }
    const judgement = round.judgePresence(location);

    const existing = await repo.findRecord(a.schoolId, round.id, a.userId);
    const record = existing ?? AttendanceRecord.fromScan({ schoolId: a.schoolId, roundId: round.id, studentId: a.userId, location, judgement, now });
    if (existing) existing.rescan({ location, judgement, now });
    await repo.saveRecord(record);
    return { round, record };
  });
  touch(a.schoolId, round.lessonId); // the teacher's counters move through WatchLesson
  return { status: statusToProto(record.status), reason: reasonToProto(record.reviewReason) };
}
