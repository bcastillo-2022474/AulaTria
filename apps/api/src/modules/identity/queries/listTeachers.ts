import type { MessageInitShape } from "@bufbuild/protobuf";
import type { identity } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `ListTeachers`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/identity/v1/identity.proto, rpc ListTeachers. Roles: admin.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function listTeachers(a: Actor, _req: identity.ListTeachersRequest): Promise<MessageInitShape<typeof identity.ListTeachersResponseSchema>> {
  return notImplemented("ListTeachers");
}
