import type { MessageInitShape } from "@bufbuild/protobuf";
import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `CloseFloorRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/floor/v1/floor.proto, rpc CloseFloorRound. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function closeFloorRound(a: Actor, _req: floor.CloseFloorRoundRequest): Promise<MessageInitShape<typeof floor.CloseFloorRoundResponseSchema>> {
  return notImplemented("CloseFloorRound");
}
