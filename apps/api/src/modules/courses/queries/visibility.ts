import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ-side access rule, as a query: the course, only if this actor may see it (the teacher who owns it, or an
 * enrolled student). "Does not exist" and "not yours" are the same answer on purpose.
 */
export async function visibleCourse(a: Actor, courseId: string) {
  let q = db.selectFrom("course").selectAll("course").where("course.school_id", "=", a.schoolId).where("course.id", "=", courseId);
  if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
  if (a.role === "student") {
    q = q.where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "course.id")
      .where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  }
  const c = await q.executeTakeFirst();
  if (!c) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
  return c;
}
