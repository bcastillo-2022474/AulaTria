import type { MessageInitShape } from "@bufbuild/protobuf";
import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `CloseAttendanceRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/attendance/v1/attendance.proto, rpc CloseAttendanceRound. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function closeAttendanceRound(a: Actor, _req: attendance.CloseAttendanceRoundRequest): Promise<MessageInitShape<typeof attendance.CloseAttendanceRoundResponseSchema>> {
  return notImplemented("CloseAttendanceRound");
}
