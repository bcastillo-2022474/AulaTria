import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export const DOUBT_MAX_LENGTH = 500;

/**
 * Used by SubmitDoubt. A student's doubt in one round. One per student per round: submitting again while open
 * edits it (the repository upserts on (round_id, student_id)).
 * Pure: no I/O.
 */
export class Doubt {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly roundId: string,
    readonly studentId: string,
    private _text: string,
    readonly createdAt: Date,
    private _editedAt: Date | null,
    private _groupId: string | null,
  ) {}

  /** A first submission. Text rules: 1..500 characters after trimming. */
  static submit(p: { schoolId: string; roundId: string; studentId: string; text: string; now: Date }): Doubt {
    return new Doubt(newId(), p.schoolId, p.roundId, p.studentId, cleanText(p.text), p.now, null, null);
  }

  get text() { return this._text; }
  get editedAt() { return this._editedAt; }
  get groupId() { return this._groupId; }

  /** A re-submit while the round is open. The round's state is checked by the command (DoubtRound.assertAcceptsDoubts). */
  replaceText(text: string, now: Date): void {
    this._text = cleanText(text);
    this._editedAt = now;
  }

  /** Set by the grouping step once it has decided which topic this doubt belongs to. */
  assignGroup(groupId: string): void {
    this._groupId = groupId;
  }
}

function cleanText(text: string): string {
  const t = text.trim();
  // Characters, as protovalidate counts them (code points): an emoji is one, not two UTF-16 units.
  const length = Array.from(t).length;
  if (length < 1 || length > DOUBT_MAX_LENGTH) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
  return t;
}
