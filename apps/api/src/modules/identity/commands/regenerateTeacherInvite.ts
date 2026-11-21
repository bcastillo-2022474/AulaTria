import type { MessageInitShape } from "@bufbuild/protobuf";
import type { identity } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `RegenerateTeacherInvite`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/identity/v1/identity.proto, rpc RegenerateTeacherInvite. Roles: admin.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function regenerateTeacherInvite(a: Actor, _req: identity.RegenerateTeacherInviteRequest): Promise<MessageInitShape<typeof identity.RegenerateTeacherInviteResponseSchema>> {
  return notImplemented("RegenerateTeacherInvite");
}
