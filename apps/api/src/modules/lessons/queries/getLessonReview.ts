import type { MessageInitShape } from "@bufbuild/protobuf";
import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetLessonReview`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/lessons/v1/lessons.proto, rpc GetLessonReview. Roles: teacher.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getLessonReview(a: Actor, _req: lessons.GetLessonReviewRequest): Promise<MessageInitShape<typeof lessons.GetLessonReviewResponseSchema>> {
  return notImplemented("GetLessonReview");
}
