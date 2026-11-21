import type { ServiceImpl } from "@connectrpc/connect";
import type { quizzes } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { draftQuiz } from "./commands/draftQuiz.js";
import { regenerateQuiz } from "./commands/regenerateQuiz.js";
import { updateQuizTime } from "./commands/updateQuizTime.js";
import { launchQuiz } from "./commands/launchQuiz.js";
import { answerQuiz } from "./commands/answerQuiz.js";
import { closeQuiz } from "./commands/closeQuiz.js";
import { getQuiz } from "./queries/getQuiz.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const quizzesService: Partial<ServiceImpl<typeof quizzes.QuizService>> = {
  draftQuiz: (req, ctx) => draftQuiz(actor(ctx, "teacher"), req),
  regenerateQuiz: (req, ctx) => regenerateQuiz(actor(ctx, "teacher"), req),
  updateQuizTime: (req, ctx) => updateQuizTime(actor(ctx, "teacher"), req),
  launchQuiz: (req, ctx) => launchQuiz(actor(ctx, "teacher"), req),
  answerQuiz: (req, ctx) => answerQuiz(actor(ctx, "student"), req),
  closeQuiz: (req, ctx) => closeQuiz(actor(ctx, "teacher"), req),
  getQuiz: (req, ctx) => getQuiz(actor(ctx, "teacher", "student"), req),
};
