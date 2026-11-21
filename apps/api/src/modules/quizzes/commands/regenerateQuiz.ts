import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import { generateQuizQuestion, type GeneratedQuestion } from "../../../ai/quiz.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { db } from "../../../db/index.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { doubtRationale } from "../domain/source.js";
import { getQuiz } from "../queries/getQuiz.js";
import { findQuizSource } from "../queries/source.js";
import { QuizRepository } from "../repository.js";

/** Same slide, same material, a different question: the model is asked again up to twice if it repeats the question. */
export async function regenerateQuiz(a: Actor, req: quizzes.RegenerateQuizRequest): Promise<MessageInitShape<typeof quizzes.RegenerateQuizResponseSchema>> {
  const current = await new QuizRepository(db).findForTeacher(a.schoolId, a.userId, req.quizId);
  if (!current) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
  current.assertDraft(); // before the model call: a quiz that is no longer a draft is refused without spending it

  const source = current.materialId ? await findQuizSource(a.schoolId, current.lessonId, current.materialId) : null;
  if (!source) throw new AppError(ErrorCode.QUIZ_NO_MATERIAL);
  const slide = current.slideNumber ?? 0;
  const previous = current.content.question;

  let generated: GeneratedQuestion = await generateQuizQuestion({ sourceText: source.text, slideNumber: slide, avoidQuestion: previous });
  for (let retry = 0; retry < 2 && sameText(generated.question, previous); retry++) {
    generated = await generateQuizQuestion({ sourceText: source.text, slideNumber: slide, avoidQuestion: previous });
  }
  const content = { ...generated, rationale: doubtRationale(source.groups, slide) };

  const quiz = await inTx(async (trx) => {
    const repo = new QuizRepository(trx);
    const fresh = await repo.findForTeacher(a.schoolId, a.userId, req.quizId);
    if (!fresh) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
    fresh.regenerate(content);
    await repo.save(fresh);
    return fresh;
  });
  touch(a.schoolId, quiz.lessonId);
  return { quiz: (await getQuiz(a, { quizId: quiz.id })).quiz };
}

const sameText = (x: string, y: string) => x.trim().toLowerCase() === y.trim().toLowerCase();
