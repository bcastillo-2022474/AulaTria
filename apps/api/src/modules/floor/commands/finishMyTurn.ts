import type { MessageInitShape } from "@bufbuild/protobuf";
import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `FinishMyTurn`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/floor/v1/floor.proto, rpc FinishMyTurn. Roles: student.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function finishMyTurn(a: Actor, _req: floor.FinishMyTurnRequest): Promise<MessageInitShape<typeof floor.FinishMyTurnResponseSchema>> {
  return notImplemented("FinishMyTurn");
}
