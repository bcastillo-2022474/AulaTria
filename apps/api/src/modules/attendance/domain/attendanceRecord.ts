import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";
import type { PresenceJudgement, ReviewReason, ScanLocation } from "./attendanceRound.js";

export type AttendanceStatus = "present" | "needs_review" | "absent";
export type TeacherDecision = "accept" | "reject";

/**
 * Used by CheckIn and ResolveAttendance. One student's attendance in a round.
 *
 *   (scan) -> present | needs_review --teacher accept--> present
 *                                    --teacher reject--> absent
 *
 * absent is only written by a teacher's rejection (DBML). A student with no row is absent too (derived).
 * Rescanning rewrites the row while it is not final (DBML).
 */
export class AttendanceRecord {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly roundId: string,
    readonly studentId: string,
    private _status: AttendanceStatus,
    private _reviewReason: ReviewReason | null,
    private _scannedAt: Date,
    private _location: ScanLocation,
    private _distanceM: number | null,
    private _reviewedById: string | null,
    private _reviewedAt: Date | null,
  ) {}

  /** A scan, judged by AttendanceRound.judgePresence. */
  static fromScan(p: { schoolId: string; roundId: string; studentId: string; location: ScanLocation; judgement: PresenceJudgement; now: Date }): AttendanceRecord {
    return new AttendanceRecord(newId(), p.schoolId, p.roundId, p.studentId, p.judgement.status, p.judgement.reason, p.now, p.location, p.judgement.distanceM, null, null);
  }

  get status() { return this._status; }
  get reviewReason() { return this._reviewReason; }
  get scannedAt() { return this._scannedAt; }
  get location() { return this._location; }
  get distanceM() { return this._distanceM; }
  get reviewedById() { return this._reviewedById; }
  get reviewedAt() { return this._reviewedAt; }

  /**
   * A second scan by the same student. One current record per student and round, so:
   *   needs_review -> rewritten by the new scan ("Escanear de nuevo": it may become present);
   *   present      -> unchanged (a duplicate scan is idempotent and answers the same result);
   *   absent       -> the teacher already rejected it: ATTENDANCE_ALREADY_RESOLVED.
   */
  rescan(p: { location: ScanLocation; judgement: PresenceJudgement; now: Date }): void {
    if (this._status === "present") return;
    if (this._status === "absent") throw new AppError(ErrorCode.ATTENDANCE_ALREADY_RESOLVED);
    this._status = p.judgement.status;
    this._reviewReason = p.judgement.reason;
    this._scannedAt = p.now;
    this._location = p.location;
    this._distanceM = p.judgement.distanceM;
  }

  /**
   * "Aceptar" -> present, "Ausente" -> absent. Only a needs_review row is pending. Repeating the decision already taken
   * is a no-op (a double click); anything else on a decided or auto-accepted row is ATTENDANCE_ALREADY_RESOLVED.
   * Kept: review_reason, for audit.
   */
  resolve(decision: TeacherDecision, reviewerId: string, now: Date): void {
    const outcome = decision === "accept" ? "present" : "absent";
    if (this._status !== "needs_review") {
      if (this._reviewedById !== null && this._status === outcome) return;
      throw new AppError(ErrorCode.ATTENDANCE_ALREADY_RESOLVED);
    }
    this._status = outcome;
    this._reviewedById = reviewerId;
    this._reviewedAt = now;
  }
}
