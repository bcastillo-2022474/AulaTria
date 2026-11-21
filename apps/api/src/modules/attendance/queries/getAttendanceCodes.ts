import type { attendance } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { ts } from "../../../shared/ids.js";
import { deriveCodes } from "../domain/attendanceRound.js";
import { ownedAttendanceRound } from "./visibility.js";

/**
 * READ: the two codes of the current steps (QR every 8 s, typable every 30 s), derived from the round's secret and the
 * server clock: nothing is stored. Only for an OPEN round of the teacher's own course (a closed one shows no codes).
 * The secret never leaves the server; only the derived codes do.
 */
export async function getAttendanceCodes(a: Actor, req: attendance.GetAttendanceCodesRequest) {
  const round = await ownedAttendanceRound(a, req.roundId);
  if (round.status !== "open") throw new AppError(ErrorCode.ATTENDANCE_ROUND_CLOSED);
  const c = deriveCodes(round.qr_secret, round.qr_step_seconds, round.manual_step_seconds, new Date());
  return { qrCode: c.qr.code, qrExpiresAt: ts(c.qr.expiresAt), manualCode: c.manual.code, manualExpiresAt: ts(c.manual.expiresAt) };
}
