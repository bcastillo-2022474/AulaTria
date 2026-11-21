import type { MessageInitShape } from "@bufbuild/protobuf";
import type { identity } from "@aula/rpc";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `AcceptInvitation`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/identity/v1/identity.proto, rpc AcceptInvitation. Roles: public.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function acceptInvitation(_req: identity.AcceptInvitationRequest): Promise<MessageInitShape<typeof identity.AcceptInvitationResponseSchema>> {
  return notImplemented("AcceptInvitation");
}
