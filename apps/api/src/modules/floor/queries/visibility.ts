import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ-side access rule, as a query: the round, only if the actor may see it (teacher owns the course of its lesson,
 * or the student is enrolled in it). "Does not exist" and "not yours" are the same answer on purpose.
 */
export async function visibleFloorRound(a: Actor, roundId: string) {
  let q = db.selectFrom("floor_round")
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "floor_round.lesson_id").onRef("lesson.school_id", "=", "floor_round.school_id"))
    .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["floor_round.id", "floor_round.lesson_id", "floor_round.prompt", "floor_round.status", "floor_round.open_to_all", "floor_round.opened_at"])
    .where("floor_round.school_id", "=", a.schoolId).where("floor_round.id", "=", roundId);
  if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
  if (a.role === "student") {
    q = q.where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
      .where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  }
  const r = await q.executeTakeFirst();
  if (!r) throw new AppError(ErrorCode.FLOOR_ROUND_NOT_FOUND);
  return r;
}
