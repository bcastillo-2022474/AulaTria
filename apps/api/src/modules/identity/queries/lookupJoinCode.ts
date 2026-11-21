import type { identity } from "@aula/rpc";
import { db } from "../../../db/index.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/** public READ: what a course join code points at, before the student has an account (so: no tenant yet). */
export async function lookupJoinCode(req: identity.LookupJoinCodeRequest) {
  const c = await db.selectFrom("course").innerJoin("school", "school.id", "course.school_id")
    .select(["course.id", "course.name", "course.code", "course.school_id", "school.name as school_name"])
    .select((eb) => eb.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).whereRef("enrollment.course_id", "=", "course.id").as("student_count"))
    .where("course.join_code", "=", req.joinCode.trim().toUpperCase()).where("course.tp_status", "=", "active").executeTakeFirst();
  if (!c) throw new AppError(ErrorCode.COURSE_JOIN_CODE_NOT_FOUND);
  return { courseId: c.id, courseName: c.name, courseCode: c.code ?? "", schoolName: c.school_name, studentCount: Number(c.student_count ?? 0) };
}
