import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ-side access rule, as a query: the round, only if the actor may see it (the teacher owns the course of its lesson,
 * or the student is enrolled in it). "Does not exist", "not yours" and "another school's" are the same answer on purpose.
 */
export async function visibleDoubtRound(a: Actor, roundId: string) {
  let q = db.selectFrom("doubt_round")
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "doubt_round.lesson_id").onRef("lesson.school_id", "=", "doubt_round.school_id"))
    .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["doubt_round.id", "doubt_round.lesson_id", "doubt_round.status", "doubt_round.summary_status", "doubt_round.opened_at",
      "doubt_round.closes_at", "doubt_round.closed_at", "lesson.course_id"])
    .where("doubt_round.school_id", "=", a.schoolId).where("doubt_round.id", "=", roundId);
  if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
  if (a.role === "student") {
    q = q.where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
      .where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  }
  const r = await q.executeTakeFirst();
  if (!r) throw new AppError(ErrorCode.DOUBT_ROUND_NOT_FOUND);
  return r;
}
