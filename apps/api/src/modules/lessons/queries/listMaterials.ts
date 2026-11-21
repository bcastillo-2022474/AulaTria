import type { MessageInitShape } from "@bufbuild/protobuf";
import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `ListMaterials`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/lessons/v1/lessons.proto, rpc ListMaterials. Roles: teacher.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function listMaterials(a: Actor, _req: lessons.ListMaterialsRequest): Promise<MessageInitShape<typeof lessons.ListMaterialsResponseSchema>> {
  return notImplemented("ListMaterials");
}
