import type { MessageInitShape } from "@bufbuild/protobuf";
import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { notImplemented } from "../../../shared/errors.js";

// TODO(contributor): implement `ResolveAttendance`. Use the skill `new-rpc` (and CLAUDE.md).
// Contract: proto/aula/attendance/v1/attendance.proto, rpc ResolveAttendance. Roles: teacher.
// WRITE side: load a domain entity through the repository, call its method, save, touch(realtime). No SQL here.
export async function resolveAttendance(a: Actor, _req: attendance.ResolveAttendanceRequest): Promise<MessageInitShape<typeof attendance.ResolveAttendanceResponseSchema>> {
  return notImplemented("ResolveAttendance");
}
