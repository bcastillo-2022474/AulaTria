import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { lessonStatusToProto } from "./lessonStatus.js";

/** READ. Teacher: courses they own. Student: courses they are enrolled in. One query, with the live lesson and the roster size. */
export async function listCourses(a: Actor) {
  let q = db.selectFrom("course").select(["course.id", "course.name", "course.code"])
    .select((eb) => eb.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).whereRef("enrollment.course_id", "=", "course.id").where("enrollment.school_id", "=", a.schoolId).as("student_count"))
    .select((eb) => eb.selectFrom("lesson").select("lesson.id").whereRef("lesson.course_id", "=", "course.id").where("lesson.school_id", "=", a.schoolId).where("lesson.status", "in", ["lobby", "live"]).limit(1).as("live_id"))
    .select((eb) => eb.selectFrom("lesson").select("lesson.number").whereRef("lesson.course_id", "=", "course.id").where("lesson.school_id", "=", a.schoolId).where("lesson.status", "in", ["lobby", "live"]).limit(1).as("live_number"))
    .select((eb) => eb.selectFrom("lesson").select("lesson.status").whereRef("lesson.course_id", "=", "course.id").where("lesson.school_id", "=", a.schoolId).where("lesson.status", "in", ["lobby", "live"]).limit(1).as("live_status"))
    .where("course.school_id", "=", a.schoolId).where("course.tp_status", "=", "active").orderBy("course.name");
  q = a.role === "teacher"
    ? q.where("course.teacher_id", "=", a.userId)
    : q.where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "course.id")
        .where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  const rows = await q.execute();
  return {
    courses: rows.map((c) => ({
      id: c.id, name: c.name, code: c.code ?? "", studentCount: Number(c.student_count ?? 0),
      liveLesson: c.live_id ? { id: c.live_id, number: c.live_number!, status: lessonStatusToProto(c.live_status!) } : undefined,
    })),
  };
}
