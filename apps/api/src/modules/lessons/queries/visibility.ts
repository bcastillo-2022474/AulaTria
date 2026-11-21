import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/** READ-side access rule: the lesson, only if the actor may see it (teacher owns the course / student is enrolled). */
export async function visibleLesson(a: Actor, lessonId: string) {
  let q = db.selectFrom("lesson").innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["lesson.id", "lesson.number", "lesson.status", "lesson.started_at", "lesson.course_id", "course.name as course_name"])
    .where("lesson.school_id", "=", a.schoolId).where("lesson.id", "=", lessonId);
  if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
  if (a.role === "student") {
    q = q.where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
      .where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  }
  const l = await q.executeTakeFirst();
  if (!l) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
  return l;
}
