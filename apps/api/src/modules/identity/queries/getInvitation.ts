import type { MessageInitShape } from "@bufbuild/protobuf";
import type { identity } from "@aula/rpc";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetInvitation`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/identity/v1/identity.proto, rpc GetInvitation. Roles: public.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getInvitation(_req: identity.GetInvitationRequest): Promise<MessageInitShape<typeof identity.GetInvitationResponseSchema>> {
  return notImplemented("GetInvitation");
}
