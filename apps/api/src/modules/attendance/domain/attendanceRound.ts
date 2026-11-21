import { createHmac, randomBytes } from "node:crypto";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export type AttendanceRoundStatus = "open" | "closed";
export type ReviewReason = "too_far" | "no_location" | "low_accuracy";
export type AttendanceCodeKind = "qr" | "manual";
export type RoomLocation = { lat: number; lng: number };
export type DerivedCodes = { qr: { code: string; expiresAt: Date }; manual: { code: string; expiresAt: Date } };
export type ScanLocation = { lat: number; lng: number; accuracyM: number } | null;
export type PresenceJudgement = { status: "present" | "needs_review"; reason: ReviewReason | null; distanceM: number | null };

export const QR_STEP_SECONDS = 8;
export const MANUAL_STEP_SECONDS = 30;
export const DEFAULT_RADIUS_M = 50;
/** TODO(contributor): the accuracy limit is a guess (the proto only says "too imprecise"). Confirm it with the product. */
export const MAX_ACCURACY_M = 50;
export const QR_CODE_LENGTH = 16;
export const MANUAL_CODE_LENGTH = 6;
/** No 0/O/1/I/L: a typed code must not be misread. */
const MANUAL_ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";

/**
 * Used by OpenAttendanceRound, CloseAttendanceRound, CheckIn (verifyCode, judgePresence). Attendance round: the rules and the two derived codes, no I/O.
 *
 *   open --close--> closed
 *
 * Codes are stateless: both are HMAC(qr_secret, step) so verification needs no row. The location is the proof of
 * presence: the server measures the distance (haversine) and never trusts a distance from the client.
 */
export class AttendanceRound {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly lessonId: string,
    private _status: AttendanceRoundStatus,
    readonly qrSecret: string,
    readonly qrStepSeconds: number,
    readonly manualStepSeconds: number,
    /** Where the teacher stood when opening. null = opened without location: judged by code only (migration 004). */
    readonly room: RoomLocation | null,
    readonly radiusM: number,
    readonly openedAt: Date,
    private _closedAt: Date | null,
  ) {}

  /**
   * OpenAttendance. The room location is where the teacher stands. null (has_location=false) opens a round judged by
   * code only: every scan then lands as needs_review / no_location. Coordinates are validated here (MALFORMED_ARGUMENT).
   */
  static open(p: { schoolId: string; lessonId: string; room: RoomLocation | null; radiusM?: number; now: Date }): AttendanceRound {
    if (p.room) assertCoordinates(p.room);
    const secret = randomBytes(32).toString("hex");
    return new AttendanceRound(newId(), p.schoolId, p.lessonId, "open", secret, QR_STEP_SECONDS, MANUAL_STEP_SECONDS, p.room, p.radiusM ?? DEFAULT_RADIUS_M, p.now, null);
  }

  get status() { return this._status; }
  get closedAt() { return this._closedAt; }

  /** The QR payload and the typable code, each with the time it stops being valid. Shown on the teacher's screen. */
  codes(now: Date): DerivedCodes {
    return deriveCodes(this.qrSecret, this.qrStepSeconds, this.manualStepSeconds, now);
  }

  /**
   * Is this code a valid proof of presence right now? QR: current step only. Manual: current step and the previous
   * one, so nobody loses a race while typing. Returns which kind matched, or null (then the command raises
   * ATTENDANCE_QR_INVALID). Does not check the round is open: the command does that first.
   */
  verifyCode(code: string, now: Date): AttendanceCodeKind | null {
    const c = code.trim().toUpperCase();
    if (c === deriveQrCode(this.qrSecret, stepOf(now, this.qrStepSeconds))) return "qr";
    const manualStep = stepOf(now, this.manualStepSeconds);
    if (c.length === MANUAL_CODE_LENGTH && (c === deriveManualCode(this.qrSecret, manualStep) || c === deriveManualCode(this.qrSecret, manualStep - 1))) return "manual";
    return null;
  }

  /**
   * Decides presence from a scan's location. Order: no location -> needs_review/no_location; too imprecise ->
   * needs_review/low_accuracy (the distance is still computed, for the audit); inside the radius -> present; otherwise
   * needs_review/too_far. The distance is reported in every case where a location exists.
   */
  judgePresence(location: ScanLocation): PresenceJudgement {
    // No scan location, or a round opened without the teacher's location: nothing to measure against.
    if (!location || !this.room) return { status: "needs_review", reason: "no_location", distanceM: null };
    const distanceM = Math.round(haversineM(location, this.room));
    if (location.accuracyM > MAX_ACCURACY_M) return { status: "needs_review", reason: "low_accuracy", distanceM };
    if (distanceM <= this.radiusM) return { status: "present", reason: null, distanceM };
    return { status: "needs_review", reason: "too_far", distanceM };
  }

  /** CloseAttendanceRound. */
  close(now: Date): void {
    if (this._status !== "open") throw new AppError(ErrorCode.ATTENDANCE_ROUND_CLOSED);
    this._status = "closed";
    this._closedAt = now;
  }

  /** A scan or a teacher decision is refused once the round is closed. */
  assertOpen(): void {
    if (this._status !== "open") throw new AppError(ErrorCode.ATTENDANCE_ROUND_CLOSED);
  }
}

/**
 * The codes of the current steps. Pure (secret + steps + clock), so the read side (GetAttendanceCodes) calls it with
 * the row's values without building an entity. AttendanceRound.codes() is the same thing for the write side.
 */
export function deriveCodes(secret: string, qrStepSeconds: number, manualStepSeconds: number, now: Date): DerivedCodes {
  const qrStep = stepOf(now, qrStepSeconds);
  const manualStep = stepOf(now, manualStepSeconds);
  return {
    qr: { code: deriveQrCode(secret, qrStep), expiresAt: stepEnd(qrStep, qrStepSeconds) },
    manual: { code: deriveManualCode(secret, manualStep), expiresAt: stepEnd(manualStep, manualStepSeconds) },
  };
}

/** A latitude/longitude from a client must be finite and on the globe; anything else is a malformed request. */
export function assertCoordinates(c: RoomLocation): void {
  if (!Number.isFinite(c.lat) || !Number.isFinite(c.lng) || Math.abs(c.lat) > 90 || Math.abs(c.lng) > 180) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
}

const stepOf = (now: Date, stepSeconds: number) => Math.floor(now.getTime() / 1000 / stepSeconds);
const stepEnd = (step: number, stepSeconds: number) => new Date((step + 1) * stepSeconds * 1000);

/** QR code: 16 hex characters from HMAC-SHA256(secret, "qr:<step>"). */
export function deriveQrCode(secret: string, step: number): string {
  return createHmac("sha256", secret).update(`qr:${step}`).digest("hex").slice(0, QR_CODE_LENGTH).toUpperCase();
}

/** Typable code: 6 characters from HMAC-SHA256(secret, "manual:<step>"), each mapped into MANUAL_ALPHABET. */
export function deriveManualCode(secret: string, step: number): string {
  const digest = createHmac("sha256", secret).update(`manual:${step}`).digest();
  let out = "";
  for (let i = 0; i < MANUAL_CODE_LENGTH; i++) out += MANUAL_ALPHABET[digest[i] % MANUAL_ALPHABET.length];
  return out;
}

/** Great-circle distance in metres (haversine). Pure. */
export function haversineM(a: RoomLocation, b: RoomLocation): number {
  const R = 6_371_000;
  const toRad = (d: number) => (d * Math.PI) / 180;
  const dLat = toRad(b.lat - a.lat);
  const dLng = toRad(b.lng - a.lng);
  const h = Math.sin(dLat / 2) ** 2 + Math.cos(toRad(a.lat)) * Math.cos(toRad(b.lat)) * Math.sin(dLng / 2) ** 2;
  return 2 * R * Math.asin(Math.min(1, Math.sqrt(h)));
}
