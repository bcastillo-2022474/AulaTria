import type { MessageInitShape } from "@bufbuild/protobuf";
import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetDoubtRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/doubts/v1/doubts.proto, rpc GetDoubtRound. Roles: teacher, student.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getDoubtRound(a: Actor, _req: doubts.GetDoubtRoundRequest): Promise<MessageInitShape<typeof doubts.GetDoubtRoundResponseSchema>> {
  return notImplemented("GetDoubtRound");
}
