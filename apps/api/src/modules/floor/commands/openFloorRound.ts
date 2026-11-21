import type { MessageInitShape } from "@bufbuild/protobuf";
import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `OpenFloorRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/floor/v1/floor.proto, rpc OpenFloorRound. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function openFloorRound(a: Actor, _req: floor.OpenFloorRoundRequest): Promise<MessageInitShape<typeof floor.OpenFloorRoundResponseSchema>> {
  return notImplemented("OpenFloorRound");
}
