import type { MessageInitShape } from "@bufbuild/protobuf";
import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `SubmitDoubt`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/doubts/v1/doubts.proto, rpc SubmitDoubt. Roles: student.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function submitDoubt(a: Actor, _req: doubts.SubmitDoubtRequest): Promise<MessageInitShape<typeof doubts.SubmitDoubtResponseSchema>> {
  return notImplemented("SubmitDoubt");
}
