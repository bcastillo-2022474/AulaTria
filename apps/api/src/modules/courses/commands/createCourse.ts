import type { courses } from "@aula/rpc";
import { db, isUniqueViolation } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { Course, generateJoinCode } from "../domain/course.js";
import { CourseRepository } from "../repository.js";
import { getCourse } from "../queries/getCourse.js";

export async function createCourse(a: Actor, req: courses.CreateCourseRequest) {
  const repo = new CourseRepository(db);
  for (let attempt = 0; attempt < 5; attempt++) {
    const course = Course.create({ schoolId: a.schoolId, teacherId: a.userId, name: req.name, code: req.code, joinCode: generateJoinCode(req.name) });
    try {
      await repo.insert(course);
      return getCourse(a, { courseId: course.id } as courses.GetCourseRequest); // the response is a READ of what was just written
    } catch (e) {
      if (!isUniqueViolation(e)) throw e; // join-code collision: draw another
    }
  }
  throw new AppError(ErrorCode.NOT_FOUND, "could not allocate a join code");
}
