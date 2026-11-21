import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `LaunchQuiz`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/quizzes/v1/quizzes.proto, rpc LaunchQuiz. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function launchQuiz(a: Actor, _req: quizzes.LaunchQuizRequest): Promise<MessageInitShape<typeof quizzes.LaunchQuizResponseSchema>> {
  return notImplemented("LaunchQuiz");
}
