import { isUniqueViolation } from "../../db/index.js";
import { AppError, ErrorCode } from "../../shared/errors.js";
import type { Executor } from "../../shared/tx.js";
import { Course, normalizeJoinCode } from "./domain/course.js";

/** WRITE-SIDE persistence of courses and enrollments. Concrete class, no interface. Reads do not use it. */
export class CourseRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher can only load (and so change) their own course. */
  async findForTeacher(schoolId: string, teacherId: string, courseId: string): Promise<Course | null> {
    const r = await this.db.selectFrom("course").selectAll().where("school_id", "=", schoolId).where("teacher_id", "=", teacherId).where("id", "=", courseId).executeTakeFirst();
    return r ? toCourse(r) : null;
  }

  async findById(schoolId: string, courseId: string): Promise<Course | null> {
    const r = await this.db.selectFrom("course").selectAll().where("school_id", "=", schoolId).where("id", "=", courseId).where("tp_status", "=", "active").executeTakeFirst();
    return r ? toCourse(r) : null;
  }

  /** Not tenant-scoped on purpose: join codes are how a student without an account finds their school. */
  async findByJoinCode(raw: string): Promise<Course | null> {
    const r = await this.db.selectFrom("course").selectAll().where("join_code", "=", normalizeJoinCode(raw)).where("tp_status", "=", "active").executeTakeFirst();
    return r ? toCourse(r) : null;
  }

  /** Throws the raw unique violation when the join code collides: the use case retries with another one. */
  async insert(c: Course): Promise<void> {
    await this.db.insertInto("course").values({
      id: c.id, school_id: c.schoolId, teacher_id: c.teacherId, name: c.name, code: c.code, join_code: c.joinCode,
      tp_status: "active", created_at: new Date(), updated_at: new Date(),
    }).execute();
  }

  async enroll(schoolId: string, courseId: string, studentId: string, at: Date): Promise<void> {
    try {
      await this.db.insertInto("enrollment").values({ school_id: schoolId, course_id: courseId, student_id: studentId, joined_at: at }).execute();
    } catch (e) {
      if (isUniqueViolation(e)) throw new AppError(ErrorCode.COURSE_ALREADY_ENROLLED);
      throw e;
    }
  }
}

type CourseRow = { id: string; school_id: string; teacher_id: string; name: string; code: string | null; join_code: string };
const toCourse = (r: CourseRow) => new Course(r.id, r.school_id, r.teacher_id, r.name, r.code, r.join_code);
