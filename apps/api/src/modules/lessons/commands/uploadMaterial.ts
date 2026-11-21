import type { MessageInitShape } from "@bufbuild/protobuf";
import type { lessons } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `UploadMaterial`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/lessons/v1/lessons.proto, rpc UploadMaterial. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function uploadMaterial(a: Actor, _req: lessons.UploadMaterialRequest): Promise<MessageInitShape<typeof lessons.UploadMaterialResponseSchema>> {
  return notImplemented("UploadMaterial");
}
