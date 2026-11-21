import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ-side access rule: the quiz, only if the actor may see it. A teacher sees their course's quizzes in any status;
 * a student sees quizzes of courses they are enrolled in, but never a DRAFT. Not visible = QUIZ_NOT_FOUND.
 */
export async function visibleQuiz(a: Actor, quizId: string) {
  let q = db.selectFrom("quiz")
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "quiz.lesson_id").onRef("lesson.school_id", "=", "quiz.school_id"))
    .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select([
      "quiz.id", "quiz.lesson_id", "quiz.status", "quiz.question", "quiz.options", "quiz.correct_label", "quiz.rationale",
      "quiz.slide_number", "quiz.time_limit_seconds", "quiz.closes_at", "lesson.course_id",
    ])
    .where("quiz.school_id", "=", a.schoolId).where("quiz.id", "=", quizId);
  if (a.role === "teacher") q = q.where("course.teacher_id", "=", a.userId);
  if (a.role === "student") {
    q = q.where("quiz.status", "!=", "draft").where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id")
      .whereRef("enrollment.course_id", "=", "lesson.course_id").where("enrollment.school_id", "=", a.schoolId).where("enrollment.student_id", "=", a.userId)));
  }
  const row = await q.executeTakeFirst();
  if (!row) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
  return row;
}
