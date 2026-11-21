import type { MessageInitShape } from "@bufbuild/protobuf";
import type { followup } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { CourseRepository } from "../../courses/repository.js";
import { StudentNote } from "../domain/studentNote.js";
import { StudentNoteRepository } from "../repository.js";

/** WRITE: create or replace the teacher's private note on an enrolled student of their own course. Nothing live changes. */
export async function saveStudentNote(a: Actor, req: followup.SaveStudentNoteRequest): Promise<MessageInitShape<typeof followup.SaveStudentNoteResponseSchema>> {
  await inTx(async (trx) => {
    const course = await new CourseRepository(trx).findForTeacher(a.schoolId, a.userId, req.courseId);
    if (!course) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
    const enrolled = await trx.selectFrom("enrollment").select("student_id")
      .where("school_id", "=", a.schoolId).where("course_id", "=", course.id).where("student_id", "=", req.studentId).executeTakeFirst();
    if (!enrolled) throw new AppError(ErrorCode.COURSE_NOT_ENROLLED);
    const repo = new StudentNoteRepository(trx);
    const now = new Date();
    const existing = await repo.findOne(a.schoolId, a.userId, course.id, req.studentId);
    if (existing) existing.replaceBody(req.body, now);
    await repo.upsert(existing ?? StudentNote.write({ schoolId: a.schoolId, courseId: course.id, studentId: req.studentId, body: req.body, now }));
  });
  return {};
}
