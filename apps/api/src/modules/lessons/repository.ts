import { isUniqueViolation } from "../../db/index.js";
import { AppError, ErrorCode } from "../../shared/errors.js";
import { newId } from "../../shared/ids.js";
import type { Executor } from "../../shared/tx.js";
import { Lesson, type LessonStatus } from "./domain/lesson.js";

/**
 * WRITE-SIDE persistence of lessons (and what hangs off them: participants, pulse). Concrete class, no interface.
 * Reads (the snapshot, the review) do NOT come through here: they are specialized queries in queries/.
 */
export class LessonRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher can only load (so only change) lessons of their own courses. */
  async findForTeacher(schoolId: string, teacherId: string, lessonId: string): Promise<Lesson | null> {
    const r = await this.db.selectFrom("lesson").innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("lesson").where("lesson.school_id", "=", schoolId).where("lesson.id", "=", lessonId).where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toLesson(r) : null;
  }

  /** A student may load the lessons of courses they are enrolled in. */
  async findForStudent(schoolId: string, studentId: string, lessonId: string): Promise<Lesson | null> {
    const r = await this.db.selectFrom("lesson").selectAll("lesson").where("lesson.school_id", "=", schoolId).where("lesson.id", "=", lessonId)
      .where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
        .where("enrollment.school_id", "=", schoolId).where("enrollment.student_id", "=", studentId))).executeTakeFirst();
    return r ? toLesson(r) : null;
  }

  async nextNumber(schoolId: string, courseId: string): Promise<number> {
    const r = await this.db.selectFrom("lesson").select((e) => e.fn.max("number").as("n")).where("school_id", "=", schoolId).where("course_id", "=", courseId).executeTakeFirst();
    return (r?.n ?? 0) + 1;
  }

  async insert(l: Lesson): Promise<void> {
    try {
      await this.db.insertInto("lesson").values({ id: l.id, school_id: l.schoolId, course_id: l.courseId, number: l.number, status: l.status, room_opened_at: l.roomOpenedAt }).execute();
    } catch (e) {
      if (isUniqueViolation(e)) throw new AppError(ErrorCode.LESSON_ALREADY_ACTIVE); // the partial unique index
      throw e;
    }
  }

  /** Persists the state the entity moved to. */
  async save(l: Lesson): Promise<void> {
    await this.db.updateTable("lesson").set({ status: l.status, started_at: l.startedAt, ended_at: l.endedAt }).where("school_id", "=", l.schoolId).where("id", "=", l.id).execute();
  }

  /**
   * Ending a lesson ends what is open inside it, in the same transaction (a cascade owned by the parent's
   * repository, like Chassis deleting a client deletes its vehicles).
   */
  async closeOpenRounds(schoolId: string, lessonId: string, at: Date): Promise<void> {
    await this.db.updateTable("doubt_round").set({ status: "closed", closed_at: at }).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").execute();
    await this.db.updateTable("floor_round").set({ status: "closed", closed_at: at }).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").execute();
    await this.db.updateTable("quiz").set({ status: "closed", closed_at: at }).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "live").execute();
    await this.db.updateTable("attendance_round").set({ status: "closed", closed_at: at }).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").execute();
  }

  async addParticipant(schoolId: string, lessonId: string, studentId: string, at: Date): Promise<void> {
    await this.db.insertInto("lesson_participant").values({ school_id: schoolId, lesson_id: lessonId, student_id: studentId, joined_at: at })
      .onConflict((oc) => oc.columns(["lesson_id", "student_id"]).doNothing()).execute();
  }

  /** Append-only: a student's current pulse is their latest event. */
  async appendPulse(schoolId: string, lessonId: string, studentId: string, state: "ok" | "lost", at: Date): Promise<void> {
    await this.db.insertInto("pulse_event").values({ id: newId(), school_id: schoolId, lesson_id: lessonId, student_id: studentId, state, created_at: at }).execute();
  }
}

type LessonRow = { id: string; school_id: string; course_id: string; number: number; status: LessonStatus; room_opened_at: Date; started_at: Date | null; ended_at: Date | null };
const toLesson = (r: LessonRow) => new Lesson(r.id, r.school_id, r.course_id, r.number, r.status, r.room_opened_at, r.started_at, r.ended_at);
