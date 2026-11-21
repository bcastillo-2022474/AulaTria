import type { courses } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { tsOrUndef } from "../../../shared/ids.js";
import { lessonStatusToProto } from "./lessonStatus.js";
import { visibleCourse } from "./visibility.js";

/** READ. The course page: join code (teacher only), roster size, the live lesson and the past ones. */
export async function getCourse(a: Actor, req: Pick<courses.GetCourseRequest, "courseId">) {
  const c = await visibleCourse(a, req.courseId);
  const studentCount = await db.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("course_id", "=", c.id).executeTakeFirstOrThrow();
  const live = await db.selectFrom("lesson").select(["id", "number", "status"]).where("school_id", "=", a.schoolId).where("course_id", "=", c.id).where("status", "in", ["lobby", "live"]).executeTakeFirst();
  const past = await db.selectFrom("lesson").select(["id", "number", "started_at", "room_opened_at"])
    .select((eb) => eb.selectFrom("lesson_participant").select((e) => e.fn.countAll().as("n")).whereRef("lesson_participant.lesson_id", "=", "lesson.id").where("lesson_participant.school_id", "=", a.schoolId).as("attendees"))
    .where("school_id", "=", a.schoolId).where("course_id", "=", c.id).where("status", "=", "ended").orderBy("number", "desc").execute();
  return {
    course: {
      id: c.id, name: c.name, code: c.code ?? "", joinCode: a.role === "teacher" ? c.join_code : "", studentCount: Number(studentCount.n),
      liveLesson: live ? { id: live.id, number: live.number, status: lessonStatusToProto(live.status) } : undefined,
      lessons: past.map((l) => ({ id: l.id, number: l.number, date: tsOrUndef(l.started_at ?? l.room_opened_at), attendeeCount: Number(l.attendees ?? 0) })),
    },
  };
}
