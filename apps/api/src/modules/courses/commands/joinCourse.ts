import type { courses } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { CourseRepository } from "../repository.js";

export async function joinCourse(a: Actor, req: courses.JoinCourseRequest) {
  const repo = new CourseRepository(db);
  const course = await repo.findById(a.schoolId, req.courseId);
  if (!course) throw new AppError(ErrorCode.COURSE_JOIN_CODE_NOT_FOUND);
  course.assertJoinCode(req.joinCode);
  await repo.enroll(a.schoolId, course.id, a.userId, new Date());
  return {};
}
