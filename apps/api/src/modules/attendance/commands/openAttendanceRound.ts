import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { AttendanceRound } from "../domain/attendanceRound.js";
import { getAttendanceRound } from "../queries/getAttendanceRound.js";
import { AttendanceRepository } from "../repository.js";

/**
 * WRITE: the teacher opens attendance in their own live lesson. lat/lng is where the teacher stands (centre of the
 * allowed area); without it (has_location=false) the round is judged by code only. At most one open round per lesson
 * (the partial unique index raises ATTENDANCE_ROUND_ALREADY_OPEN). The tenant and the teacher come from the token.
 */
export async function openAttendanceRound(a: Actor, req: attendance.OpenAttendanceRoundRequest) {
  const round = await inTx(async (trx) => {
    const lesson = await new LessonRepository(trx).findForTeacher(a.schoolId, a.userId, req.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.assertLive();
    const round = AttendanceRound.open({
      schoolId: a.schoolId, lessonId: lesson.id, room: req.hasLocation ? { lat: req.lat, lng: req.lng } : null, now: new Date(),
    });
    await new AttendanceRepository(trx).insertRound(round);
    return round;
  });
  touch(a.schoolId, round.lessonId); // the live snapshot now carries active_attendance_round
  return getAttendanceRound(a, { roundId: round.id } as attendance.GetAttendanceRoundRequest);
}
