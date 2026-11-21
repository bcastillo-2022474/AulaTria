import type { ServiceImpl } from "@connectrpc/connect";
import type { attendance } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { openAttendanceRound } from "./commands/openAttendanceRound.js";
import { getAttendanceCodes } from "./queries/getAttendanceCodes.js";
import { getAttendanceRound } from "./queries/getAttendanceRound.js";
import { checkIn } from "./commands/checkIn.js";
import { closeAttendanceRound } from "./commands/closeAttendanceRound.js";
import { resolveAttendance } from "./commands/resolveAttendance.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const attendanceService: Partial<ServiceImpl<typeof attendance.AttendanceService>> = {
  openAttendanceRound: (req, ctx) => openAttendanceRound(actor(ctx, "teacher"), req),
  getAttendanceCodes: (req, ctx) => getAttendanceCodes(actor(ctx, "teacher"), req),
  getAttendanceRound: (req, ctx) => getAttendanceRound(actor(ctx, "teacher"), req),
  checkIn: (req, ctx) => checkIn(actor(ctx, "student"), req),
  closeAttendanceRound: (req, ctx) => closeAttendanceRound(actor(ctx, "teacher"), req),
  resolveAttendance: (req, ctx) => resolveAttendance(actor(ctx, "teacher"), req),
};
