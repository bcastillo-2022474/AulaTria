import type { MessageInitShape } from "@bufbuild/protobuf";
import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetAttendanceRound`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/attendance/v1/attendance.proto, rpc GetAttendanceRound. Roles: teacher.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getAttendanceRound(a: Actor, _req: attendance.GetAttendanceRoundRequest): Promise<MessageInitShape<typeof attendance.GetAttendanceRoundResponseSchema>> {
  return notImplemented("GetAttendanceRound");
}
