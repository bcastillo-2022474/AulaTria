import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { getQuiz } from "../queries/getQuiz.js";
import { QuizRepository } from "../repository.js";

/** Time per question. Draft: sets the limit. Live: also moves closes_at (rule in Quiz.changeTime). Closed: refused. */
export async function updateQuizTime(a: Actor, req: quizzes.UpdateQuizTimeRequest): Promise<MessageInitShape<typeof quizzes.UpdateQuizTimeResponseSchema>> {
  const quiz = await inTx(async (trx) => {
    const repo = new QuizRepository(trx);
    const current = await repo.findForTeacher(a.schoolId, a.userId, req.quizId);
    if (!current) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
    current.changeTime(req.timeLimitSeconds, new Date());
    await repo.save(current);
    return current;
  });
  touch(a.schoolId, quiz.lessonId);
  return { quiz: (await getQuiz(a, { quizId: quiz.id })).quiz };
}
