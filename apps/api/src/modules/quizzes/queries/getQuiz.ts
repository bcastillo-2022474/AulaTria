import type { MessageInitShape } from "@bufbuild/protobuf";
import { quizzes } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { tsOrUndef } from "../../../shared/ids.js";
import { QUIZ_LABELS } from "../domain/quiz.js";
import { visibleQuiz } from "./visibility.js";

const STATUS = {
  draft: quizzes.QuizStatus.DRAFT,
  live: quizzes.QuizStatus.LIVE,
  closed: quizzes.QuizStatus.CLOSED,
} as const;

/**
 * READ: one quiz, projected for the caller. The student projection is a SECURITY rule: while the quiz is live a
 * student gets no correct answer, no counts, no rationale and no insight. Teacher-only fields are left out of the
 * student's message, not just empty.
 */
export async function getQuiz(a: Actor, req: { quizId: string }): Promise<MessageInitShape<typeof quizzes.GetQuizResponseSchema>> {
  const q = await visibleQuiz(a, req.quizId);
  const options = q.options as { label: string; text: string }[];
  const closed = q.status === "closed";

  const perLabel = await db.selectFrom("quiz_answer").select("option_label").select((eb) => eb.fn.countAll().as("n"))
    .where("school_id", "=", a.schoolId).where("quiz_id", "=", q.id).groupBy("option_label").execute();
  const countsByLabel: Record<string, number> = Object.fromEntries(QUIZ_LABELS.map((l) => [l, 0]));
  for (const r of perLabel) countsByLabel[r.option_label] = Number(r.n);
  const answered = Object.values(countsByLabel).reduce((sum, n) => sum + n, 0);

  const enrolled = await db.selectFrom("enrollment").select((eb) => eb.fn.countAll().as("n"))
    .where("school_id", "=", a.schoolId).where("course_id", "=", q.course_id).executeTakeFirstOrThrow();

  const base = {
    id: q.id,
    lessonId: q.lesson_id,
    status: STATUS[q.status],
    question: q.question,
    options,
    slideNumber: q.slide_number ?? 0,
    timeLimitSeconds: q.time_limit_seconds,
    closesAt: tsOrUndef(q.closes_at),
    answeredCount: answered,
    enrolledCount: Number(enrolled.n),
  };
  const correctPercent = answered ? Math.round((countsByLabel[q.correct_label] / answered) * 100) : 0;

  if (a.role === "teacher") {
    return {
      quiz: {
        ...base,
        rationale: q.rationale ?? "",
        correctLabel: q.correct_label,
        countsByLabel,
        ...(closed ? { correctPercent, insight: insightFor(options, countsByLabel, q.correct_label, answered, q.slide_number) } : {}),
      },
    };
  }

  const mine = await db.selectFrom("quiz_answer").select("option_label")
    .where("school_id", "=", a.schoolId).where("quiz_id", "=", q.id).where("student_id", "=", a.userId).executeTakeFirst();
  return {
    quiz: {
      ...base,
      myLabel: mine?.option_label ?? "",
      ...(closed ? { correctLabel: q.correct_label, correctPercent } : {}),
    },
  };
}

/**
 * Deterministic, no AI: the most chosen WRONG option, with the count, then a hint to review the slide. Empty when
 * nobody picked a wrong option (or nobody answered).
 *   "6 de 29 eligieron «Se mantiene igual». Conviene repasar la diapositiva 22."
 */
function insightFor(options: { label: string; text: string }[], counts: Record<string, number>, correct: string, answered: number, slide: number | null): string {
  if (!answered) return "";
  const wrong = options.filter((o) => o.label !== correct).map((o) => ({ text: o.text, n: counts[o.label] ?? 0 })).filter((w) => w.n > 0);
  if (!wrong.length) return "";
  const top = wrong.reduce((best, w) => (w.n > best.n ? w : best));
  const hint = slide ? `Conviene repasar la diapositiva ${slide}.` : "Conviene repasar el tema de la clase.";
  return `${top.n} de ${answered} eligieron «${top.text}». ${hint}`;
}
