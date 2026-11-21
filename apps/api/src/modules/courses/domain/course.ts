import { randomInt } from "node:crypto";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

// No 0/O/1/I: a join code gets read aloud and typed on a phone.
const ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

/** "Bases de Datos 2" -> "BAS-K7M2". Collisions are retried by the use case. */
export function generateJoinCode(courseName: string): string {
  const prefix = courseName.replace(/[^A-Za-z0-9]/g, "").slice(0, 3).toUpperCase() || "AUL";
  return `${prefix}-${Array.from({ length: 4 }, () => ALPHABET[randomInt(ALPHABET.length)]).join("")}`;
}

export const normalizeJoinCode = (raw: string) => raw.trim().toUpperCase();

/** A teacher's course. Pure: invariants only. */
export class Course {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly teacherId: string,
    readonly name: string,
    readonly code: string | null,
    readonly joinCode: string,
  ) {}

  static create(p: { schoolId: string; teacherId: string; name: string; code?: string; joinCode: string }): Course {
    const name = p.name.trim();
    if (!name || name.length > 100) throw new AppError(ErrorCode.MALFORMED_ARGUMENT, "course name");
    const code = p.code?.trim() || null;
    if (code && code.length > 20) throw new AppError(ErrorCode.MALFORMED_ARGUMENT, "course code");
    return new Course(newId(), p.schoolId, p.teacherId, name, code, p.joinCode);
  }

  /** The join code is the proof a student may enroll. */
  assertJoinCode(raw: string): void {
    if (normalizeJoinCode(raw) !== this.joinCode) throw new AppError(ErrorCode.COURSE_JOIN_CODE_NOT_FOUND);
  }
}
