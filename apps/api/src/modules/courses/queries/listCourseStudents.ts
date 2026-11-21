import type { courses } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { visibleCourse } from "./visibility.js";

/** READ. Roster with attendance rate (lessons attended / lessons held), computed in SQL. */
export async function listCourseStudents(a: Actor, req: courses.ListCourseStudentsRequest) {
  await visibleCourse(a, req.courseId);
  const held = await db.selectFrom("lesson").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("course_id", "=", req.courseId).where("status", "=", "ended").executeTakeFirstOrThrow();
  const rows = await db.selectFrom("enrollment").innerJoin("app_user", (j) => j.onRef("app_user.id", "=", "enrollment.student_id").onRef("app_user.school_id", "=", "enrollment.school_id"))
    .select(["app_user.id", "app_user.name"])
    .select((eb) => eb.selectFrom("lesson_participant").innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "lesson_participant.lesson_id").onRef("lesson.school_id", "=", "lesson_participant.school_id"))
      .select((e) => e.fn.countAll().as("n")).whereRef("lesson_participant.student_id", "=", "app_user.id").where("lesson.course_id", "=", req.courseId).where("lesson.school_id", "=", a.schoolId).as("attended"))
    .where("enrollment.school_id", "=", a.schoolId).where("enrollment.course_id", "=", req.courseId).orderBy("app_user.name").execute();
  const total = Number(held.n);
  return { students: rows.map((s) => ({ id: s.id, name: s.name, attendanceRate: total ? Math.min(1, Number(s.attended ?? 0) / total) : 0 })) };
}
