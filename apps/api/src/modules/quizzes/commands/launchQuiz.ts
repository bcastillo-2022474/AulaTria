import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { LessonRepository } from "../../lessons/repository.js";
import { getQuiz } from "../queries/getQuiz.js";
import { QuizRepository } from "../repository.js";

/**
 * "Lanzar quiz": draft -> live, with closes_at = now + the time limit. The lesson must be live, and no other quiz of
 * the lesson may be live (QUIZ_ALREADY_LIVE; the partial unique index one_live_quiz covers a race in save()).
 */
export async function launchQuiz(a: Actor, req: quizzes.LaunchQuizRequest): Promise<MessageInitShape<typeof quizzes.LaunchQuizResponseSchema>> {
  const quiz = await inTx(async (trx) => {
    const repo = new QuizRepository(trx);
    const current = await repo.findForTeacher(a.schoolId, a.userId, req.quizId);
    if (!current) throw new AppError(ErrorCode.QUIZ_NOT_FOUND);
    const lesson = await new LessonRepository(trx).findForTeacher(a.schoolId, a.userId, current.lessonId);
    if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);
    lesson.assertLive();
    current.launch(new Date()); // refuses a quiz that is not a draft (QUIZ_NOT_DRAFT)
    if (await repo.findLiveInLesson(a.schoolId, current.lessonId)) throw new AppError(ErrorCode.QUIZ_ALREADY_LIVE);
    await repo.save(current);
    return current;
  });
  touch(a.schoolId, quiz.lessonId);
  return { quiz: (await getQuiz(a, { quizId: quiz.id })).quiz };
}
