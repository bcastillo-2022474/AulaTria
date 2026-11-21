import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetQuiz`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/quizzes/v1/quizzes.proto, rpc GetQuiz. Roles: teacher, student.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getQuiz(a: Actor, _req: quizzes.GetQuizRequest): Promise<MessageInitShape<typeof quizzes.GetQuizResponseSchema>> {
  return notImplemented("GetQuiz");
}
