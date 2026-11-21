import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import { db } from "../../../db/index.js";
import { generateQuizQuestion } from "../../../ai/quiz.js";
import { parseSlides } from "../../../ai/slides.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { Quiz } from "../domain/quiz.js";
import { chooseSlide, doubtRationale } from "../domain/source.js";
import { getQuiz } from "../queries/getQuiz.js";
import { findQuizSource } from "../queries/source.js";
import { QuizRepository } from "../repository.js";

/**
 * Drafts one question from the lesson material (the slide with most doubts, or the one asked for) with the AI adapter.
 * The model call runs OUTSIDE the transaction: it can take seconds. The lesson is checked twice: before the call
 * (fail early) and inside the transaction that inserts the draft (it may have ended meanwhile).
 */
export async function draftQuiz(a: Actor, req: quizzes.DraftQuizRequest): Promise<MessageInitShape<typeof quizzes.DraftQuizResponseSchema>> {
  const lesson = await new LessonRepository(db).findForTeacher(a.schoolId, a.userId, req.lessonId);
  if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
  lesson.assertLive();

  const source = await findQuizSource(a.schoolId, lesson.id);
  if (!source) throw new AppError(ErrorCode.QUIZ_NO_MATERIAL);
  const slide = chooseSlide(parseSlides(source.text), req.slideNumber, source.groups);
  const generated = await generateQuizQuestion({ sourceText: source.text, slideNumber: slide });

  const draft = await inTx(async (trx) => {
    const current = await new LessonRepository(trx).findForTeacher(a.schoolId, a.userId, req.lessonId);
    if (!current) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    current.assertLive();
    const quiz = Quiz.draft({
      schoolId: a.schoolId, lessonId: current.id, createdById: a.userId, materialId: source.materialId,
      slideNumber: slide || null, content: { ...generated, rationale: doubtRationale(source.groups, slide) }, now: new Date(),
    });
    await new QuizRepository(trx).insert(quiz);
    return quiz;
  });
  touch(a.schoolId, draft.lessonId);
  return { quiz: (await getQuiz(a, { quizId: draft.id })).quiz };
}
