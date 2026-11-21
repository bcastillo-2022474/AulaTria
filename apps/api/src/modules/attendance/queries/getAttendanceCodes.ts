import type { MessageInitShape } from "@bufbuild/protobuf";
import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `GetAttendanceCodes`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/attendance/v1/attendance.proto, rpc GetAttendanceCodes. Roles: teacher.
// READ side: one specialized, hardcoded query returning the response message. Do NOT use the repository or entities.
export async function getAttendanceCodes(a: Actor, _req: attendance.GetAttendanceCodesRequest): Promise<MessageInitShape<typeof attendance.GetAttendanceCodesResponseSchema>> {
  return notImplemented("GetAttendanceCodes");
}
