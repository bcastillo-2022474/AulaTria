import type { MessageInitShape } from "@bufbuild/protobuf";
import type { followup } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetStudentParticipation`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/followup/v1/followup.proto, rpc GetStudentParticipation. Roles: teacher.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getStudentParticipation(a: Actor, _req: followup.GetStudentParticipationRequest): Promise<MessageInitShape<typeof followup.GetStudentParticipationResponseSchema>> {
  return notImplemented("GetStudentParticipation");
}
