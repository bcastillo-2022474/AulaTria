import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ-side access rule, as a query: the round, only if the teacher owns the course of its lesson. "Does not exist",
 * "not yours" and "another school's" are the same answer on purpose. Attendance reads are teacher-only (a student's
 * only operation is CheckIn), so there is no student branch.
 */
export async function ownedAttendanceRound(a: Actor, roundId: string) {
  const r = await db.selectFrom("attendance_round")
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "attendance_round.lesson_id").onRef("lesson.school_id", "=", "attendance_round.school_id"))
    .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["attendance_round.id", "attendance_round.lesson_id", "attendance_round.status", "attendance_round.qr_secret",
      "attendance_round.qr_step_seconds", "attendance_round.manual_step_seconds", "lesson.course_id"])
    .where("attendance_round.school_id", "=", a.schoolId).where("attendance_round.id", "=", roundId)
    .where("course.teacher_id", "=", a.userId).executeTakeFirst();
  if (!r) throw new AppError(ErrorCode.ATTENDANCE_ROUND_NOT_FOUND);
  return r;
}
