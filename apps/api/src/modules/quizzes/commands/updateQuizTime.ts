import type { MessageInitShape } from "@bufbuild/protobuf";
import type { quizzes } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `UpdateQuizTime`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/quizzes/v1/quizzes.proto, rpc UpdateQuizTime. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function updateQuizTime(a: Actor, _req: quizzes.UpdateQuizTimeRequest): Promise<MessageInitShape<typeof quizzes.UpdateQuizTimeResponseSchema>> {
  return notImplemented("UpdateQuizTime");
}
