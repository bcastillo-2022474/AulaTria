import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

/**
 * Used by SaveStudentNote. "Nota del catedratico": the teacher's private note on a student in one course. One per (course, student).
 * Private: students never receive it. Pure: no I/O.
 */
export class StudentNote {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly courseId: string,
    readonly studentId: string,
    private _body: string,
    private _updatedAt: Date,
  ) {}

  /** First note for a student in a course. TODO(contributor): is there a maximum length for a note? None is in the DBML. */
  static write(p: { schoolId: string; courseId: string; studentId: string; body: string; now: Date }): StudentNote {
    return new StudentNote(newId(), p.schoolId, p.courseId, p.studentId, cleanBody(p.body), p.now);
  }

  get body() { return this._body; }
  get updatedAt() { return this._updatedAt; }

  /** Replaces the body of an existing note. */
  replaceBody(body: string, now: Date): void {
    this._body = cleanBody(body);
    this._updatedAt = now;
  }
}

function cleanBody(body: string): string {
  const b = body.trim();
  if (b.length === 0) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
  return b;
}
