import type { MessageInitShape } from "@bufbuild/protobuf";
import type { identity } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `InviteTeacher`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/identity/v1/identity.proto, rpc InviteTeacher. Roles: admin.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function inviteTeacher(a: Actor, _req: identity.InviteTeacherRequest): Promise<MessageInitShape<typeof identity.InviteTeacherResponseSchema>> {
  return notImplemented("InviteTeacher");
}
