import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `DraftQuiz`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/quizzes/v1/quizzes.proto, rpc DraftQuiz. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function draftQuiz(a: Actor, _req: quizzes.DraftQuizRequest): Promise<MessageInitShape<typeof quizzes.DraftQuizResponseSchema>> {
  return notImplemented("DraftQuiz");
}
