import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export type LessonStatus = "lobby" | "live" | "ended" | "cancelled";

/**
 * One live class ("Clase 12"). Pure: the state machine and its rules, no I/O.
 *
 *   (none) --open--> lobby --start--> live --end--> ended
 *                      \--cancel--> cancelled
 *
 * "At most one lobby/live lesson per course" is also a partial unique index (migration 002): the rule lives
 * here for the clear error and in the DB for the race.
 */
export class Lesson {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly courseId: string,
    readonly number: number,
    private _status: LessonStatus,
    readonly roomOpenedAt: Date,
    private _startedAt: Date | null,
    private _endedAt: Date | null,
  ) {}

  static open(p: { schoolId: string; courseId: string; number: number; now: Date }): Lesson {
    return new Lesson(newId(), p.schoolId, p.courseId, p.number, "lobby", p.now, null, null);
  }

  get status() { return this._status; }
  get startedAt() { return this._startedAt; }
  get endedAt() { return this._endedAt; }

  start(now: Date): void {
    if (this._status !== "lobby") throw new AppError(ErrorCode.LESSON_NOT_IN_LOBBY);
    this._status = "live"; this._startedAt = now;
  }

  cancel(): void {
    if (this._status !== "lobby") throw new AppError(ErrorCode.LESSON_NOT_IN_LOBBY);
    this._status = "cancelled";
  }

  end(now: Date): void {
    if (this._status !== "live") throw new AppError(ErrorCode.LESSON_NOT_LIVE);
    this._status = "ended"; this._endedAt = now;
  }

  /** Students may enter the room while it is in the lobby or live. */
  assertAcceptsParticipants(): void {
    if (this._status !== "lobby" && this._status !== "live") throw new AppError(ErrorCode.LESSON_NOT_LIVE);
  }

  /** Interactions (pulse, rounds) only exist while the class is live. */
  assertLive(): void {
    if (this._status !== "live") throw new AppError(ErrorCode.LESSON_NOT_LIVE);
  }
}
