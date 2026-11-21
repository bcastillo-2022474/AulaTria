import type { MessageInitShape } from "@bufbuild/protobuf";
import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `CheckIn`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/attendance/v1/attendance.proto, rpc CheckIn. Roles: student.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function checkIn(a: Actor, _req: attendance.CheckInRequest): Promise<MessageInitShape<typeof attendance.CheckInResponseSchema>> {
  return notImplemented("CheckIn");
}
