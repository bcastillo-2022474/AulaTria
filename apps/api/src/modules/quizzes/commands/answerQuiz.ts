import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { QuizRepository } from "../repository.js";

/**
 * First answer wins. A student must be enrolled (QUIZ_NOT_FOUND otherwise); the entity refuses a draft (QUIZ_NOT_LIVE),
 * a closed quiz or a late answer (QUIZ_CLOSED); the repository refuses a second answer (QUIZ_ALREADY_ANSWERED).
 */
export async function answerQuiz(a: Actor, req: quizzes.AnswerQuizRequest): Promise<MessageInitShape<typeof quizzes.AnswerQuizResponseSchema>> {
  const lessonId = await inTx(async (trx) => {
    const repo = new QuizRepository(trx);
    const quiz = await repo.findForStudent(a.schoolId, a.userId, req.quizId);
    if (!quiz) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
    await repo.insertAnswer(quiz.acceptAnswer({ studentId: a.userId, label: req.label, now: new Date() }));
    return quiz.lessonId;
  });
  touch(a.schoolId, lessonId);
  return {};
}
