import type { MessageInitShape } from "@bufbuild/protobuf";
import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `OpenDoubtRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/doubts/v1/doubts.proto, rpc OpenDoubtRound. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function openDoubtRound(a: Actor, _req: doubts.OpenDoubtRoundRequest): Promise<MessageInitShape<typeof doubts.OpenDoubtRoundResponseSchema>> {
  return notImplemented("OpenDoubtRound");
}
