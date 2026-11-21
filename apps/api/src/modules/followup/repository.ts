import type { Executor } from "../../shared/tx.js";
import { StudentNote } from "./domain/studentNote.js";

/**
 * WRITE-SIDE persistence of the teacher's student notes. Concrete class, no interface. The participation history
 * shown next to a note is derived, so it is a read (queries/getStudentParticipation.ts), not here.
 * Used by: SaveStudentNote.
 */
export class StudentNoteRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only sees notes of their own courses. */
  async findOne(schoolId: string, teacherId: string, courseId: string, studentId: string): Promise<StudentNote | null> {
    const r = await this.db.selectFrom("student_note")
      .innerJoin("course", (j) => j.onRef("course.id", "=", "student_note.course_id").onRef("course.school_id", "=", "student_note.school_id"))
      .selectAll("student_note").where("student_note.school_id", "=", schoolId).where("student_note.course_id", "=", courseId)
      .where("student_note.student_id", "=", studentId).where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toNote(r) : null;
  }

  /**
   * Insert, or replace the body of the existing note (unique (course_id, student_id)).
   * The caller must have verified that the teacher owns the course (the command loads it through CourseRepository).
   */
  async upsert(note: StudentNote): Promise<void> {
    await this.db.insertInto("student_note").values({
      id: note.id, school_id: note.schoolId, course_id: note.courseId, student_id: note.studentId, body: note.body, updated_at: note.updatedAt,
    }).onConflict((oc) => oc.columns(["course_id", "student_id"]).doUpdateSet({ body: note.body, updated_at: note.updatedAt })).execute();
  }
}

type NoteRow = { id: string; school_id: string; course_id: string; student_id: string; body: string; updated_at: Date };
const toNote = (r: NoteRow) => new StudentNote(r.id, r.school_id, r.course_id, r.student_id, r.body, r.updated_at);
