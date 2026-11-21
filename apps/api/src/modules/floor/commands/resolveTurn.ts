import type { MessageInitShape } from "@bufbuild/protobuf";
import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `ResolveTurn`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/floor/v1/floor.proto, rpc ResolveTurn. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function resolveTurn(a: Actor, _req: floor.ResolveTurnRequest): Promise<MessageInitShape<typeof floor.ResolveTurnResponseSchema>> {
  return notImplemented("ResolveTurn");
}
