import type { identity } from "@aula/rpc";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { CourseRepository } from "../../courses/repository.js";
import { User } from "../domain/user.js";
import { hashPassword } from "../passwords.js";
import { UserRepository } from "../repositories.js";
import { issueSession } from "../session.js";

/** public. Creates the student AND enrolls them in the course the code points at, atomically. The school comes from the course. */
export async function registerStudent(req: identity.RegisterStudentRequest) {
  return inTx(async (trx) => {
    const courses = new CourseRepository(trx);
    const course = await courses.findByJoinCode(req.joinCode);
    if (!course) throw new AppError(ErrorCode.COURSE_JOIN_CODE_NOT_FOUND);
    const student = User.registerStudent({ schoolId: course.schoolId, name: req.name, email: req.email, passwordHash: await hashPassword(req.password) });
    await new UserRepository(trx).insert(student);
    await courses.enroll(course.schoolId, course.id, student.id, new Date());
    return { session: await issueSession(student, trx) };
  });
}
