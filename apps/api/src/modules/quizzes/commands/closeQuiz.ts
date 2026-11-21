import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { getQuiz } from "../queries/getQuiz.js";
import { QuizRepository } from "../repository.js";

/** "Mostrar resultado": live -> closed. The server never closes by itself; answers after closes_at are just refused. */
export async function closeQuiz(a: Actor, req: quizzes.CloseQuizRequest): Promise<MessageInitShape<typeof quizzes.CloseQuizResponseSchema>> {
  const quiz = await inTx(async (trx) => {
    const repo = new QuizRepository(trx);
    const current = await repo.findForTeacher(a.schoolId, a.userId, req.quizId);
    if (!current) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
    current.close(new Date());
    await repo.save(current);
    return current;
  });
  touch(a.schoolId, quiz.lessonId);
  return { quiz: (await getQuiz(a, { quizId: quiz.id })).quiz };
}
