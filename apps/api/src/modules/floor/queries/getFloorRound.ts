import type { MessageInitShape } from "@bufbuild/protobuf";
import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetFloorRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/floor/v1/floor.proto, rpc GetFloorRound. Roles: teacher, student.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getFloorRound(a: Actor, _req: floor.GetFloorRoundRequest): Promise<MessageInitShape<typeof floor.GetFloorRoundResponseSchema>> {
  return notImplemented("GetFloorRound");
}
