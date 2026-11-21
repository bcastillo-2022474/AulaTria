import type { Executor } from "../../shared/tx.js";
import { Quiz, type QuizOption, type QuizStatus } from "./domain/quiz.js";
import { QuizAnswer } from "./domain/quizAnswer.js";
import { AppError, ErrorCode } from "../../shared/errors.js";

/**
 * WRITE-SIDE persistence of quizzes and their answers. Concrete class, no interface. Reads (GetQuiz, the counts
 * and the insight) are specialized queries in queries/.
 * Used by: DraftQuiz, RegenerateQuiz, UpdateQuizTime, LaunchQuiz, CloseQuiz, AnswerQuiz.
 */
export class QuizRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only loads quizzes of lessons in their own courses. */
  async findForTeacher(schoolId: string, teacherId: string, quizId: string): Promise<Quiz | null> {
    const r = await this.db.selectFrom("quiz")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "quiz.lesson_id").onRef("lesson.school_id", "=", "quiz.school_id"))
      .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("quiz").where("quiz.school_id", "=", schoolId).where("quiz.id", "=", quizId).where("course.teacher_id", "=", teacherId)
      .executeTakeFirst();
    return r ? toQuiz(r) : null;
  }

  /** A student may load quizzes of lessons in courses they are enrolled in. */
  async findForStudent(schoolId: string, studentId: string, quizId: string): Promise<Quiz | null> {
    const r = await this.db.selectFrom("quiz")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "quiz.lesson_id").onRef("lesson.school_id", "=", "quiz.school_id"))
      .selectAll("quiz").where("quiz.school_id", "=", schoolId).where("quiz.id", "=", quizId)
      .where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
        .where("enrollment.school_id", "=", schoolId).where("enrollment.student_id", "=", studentId)))
      .executeTakeFirst();
    return r ? toQuiz(r) : null;
  }

  /** The quiz that is live in a lesson, if any. LaunchQuiz uses it for QUIZ_ALREADY_LIVE. */
  async findLiveInLesson(schoolId: string, lessonId: string): Promise<Quiz | null> {
    const r = await this.db.selectFrom("quiz").selectAll().where("school_id", "=", schoolId).where("lesson_id", "=", lessonId)
      .where("status", "=", "live").executeTakeFirst();
    return r ? toQuiz(r) : null;
  }

  async insert(q: Quiz): Promise<void> {
    await this.db.insertInto("quiz").values({
      id: q.id, school_id: q.schoolId, lesson_id: q.lessonId, created_by_id: q.createdById, material_id: q.materialId,
      slide_number: q.slideNumber, question: q.content.question, options: JSON.stringify(q.options), // jsonb: stringify, or pg writes an array literal
      correct_label: q.correctLabel, rationale: q.content.rationale, time_limit_seconds: q.timeLimitSeconds, status: q.status,
      created_at: q.createdAt, launched_at: q.launchedAt, closes_at: q.closesAt, closed_at: q.closedAt,
    }).execute();
  }

  /** Persists the state and content the entity moved to. */
  async save(q: Quiz): Promise<void> {
    await this.db.updateTable("quiz").set({
      question: q.content.question, options: JSON.stringify(q.options), correct_label: q.correctLabel, rationale: q.content.rationale,
      time_limit_seconds: q.timeLimitSeconds, status: q.status, launched_at: q.launchedAt, closes_at: q.closesAt, closed_at: q.closedAt,
    }).where("school_id", "=", q.schoolId).where("id", "=", q.id).execute();
  }

  /** First answer wins: the primary key (quiz_id, student_id) turns a second answer into QUIZ_ALREADY_ANSWERED. */
  async insertAnswer(a: QuizAnswer): Promise<void> {
    const res = await this.db.insertInto("quiz_answer").values({
      school_id: a.schoolId, quiz_id: a.quizId, student_id: a.studentId, option_label: a.label, answered_at: a.answeredAt,
    }).onConflict((oc) => oc.columns(["quiz_id", "student_id"]).doNothing()).executeTakeFirst();
    if (Number(res.numInsertedOrUpdatedRows ?? 0) === 0) throw new AppError(ErrorCode.QUIZ_ALREADY_ANSWERED);
  }
}

type QuizRow = {
  id: string; school_id: string; lesson_id: string; created_by_id: string; material_id: string | null; slide_number: number | null;
  question: string; options: unknown; correct_label: string; rationale: string | null; time_limit_seconds: number; status: QuizStatus;
  created_at: Date; launched_at: Date | null; closes_at: Date | null; closed_at: Date | null;
};
const toQuiz = (r: QuizRow) => new Quiz(
  r.id, r.school_id, r.lesson_id, r.created_by_id, r.material_id, r.slide_number,
  { question: r.question, options: r.options as QuizOption[], correctLabel: r.correct_label, rationale: r.rationale },
  r.time_limit_seconds, r.status, r.created_at, r.launched_at, r.closes_at, r.closed_at,
);
