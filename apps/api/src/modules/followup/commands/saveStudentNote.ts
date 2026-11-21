import type { MessageInitShape } from "@bufbuild/protobuf";
import type { followup } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `SaveStudentNote`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/followup/v1/followup.proto, rpc SaveStudentNote. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function saveStudentNote(a: Actor, _req: followup.SaveStudentNoteRequest): Promise<MessageInitShape<typeof followup.SaveStudentNoteResponseSchema>> {
  return notImplemented("SaveStudentNote");
}
