import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export type FloorTurnState = "queued" | "answering" | "answered" | "correct" | "incorrect" | "withdrawn";
export type TurnVerdict = "correct" | "incorrect";

/** The 0:15 countdown of the design. Advisory: it drives the clock, the teacher decides. */
export const ANSWER_SECONDS = 15;

/**
 * Used by RaiseHand, LowerHand, FinishMyTurn, ResolveTurn and the queue step. One student's place in a floor round.
 *
 *   queued --startAnswering--> answering --finish--> answered --resolve--> correct | incorrect
 *   queued --lower--> withdrawn
 *
 * Exactly one turn per round is answering at a time (FloorRound.startNextTurn enforces it).
 * One turn per (round, student): raising again is a no-op (the repository's insert does nothing on conflict).
 */
export class FloorTurn {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly roundId: string,
    readonly studentId: string,
    private _state: FloorTurnState,
    readonly raisedAt: Date,
    private _answeringSince: Date | null,
    private _answerDeadlineAt: Date | null,
    private _resolvedAt: Date | null,
  ) {}

  /** RaiseHand: a new queued turn. The queue order is raisedAt (server clock). */
  static raise(p: { schoolId: string; roundId: string; studentId: string; now: Date }): FloorTurn {
    return new FloorTurn(newId(), p.schoolId, p.roundId, p.studentId, "queued", p.now, null, null, null);
  }

  get state() { return this._state; }
  get answeringSince() { return this._answeringSince; }
  get answerDeadlineAt() { return this._answerDeadlineAt; }
  get resolvedAt() { return this._resolvedAt; }

  /** LowerHand ("Salir de la fila"). TODO(contributor): lowering while answering is not defined yet; only queued turns can be withdrawn. */
  lower(): void {
    if (this._state !== "queued") throw new Error("TODO(contributor): only a queued turn can be lowered; decide the rule for answering turns");
    this._state = "withdrawn";
  }

  /** Called by FloorRound.startNextTurn. */
  startAnswering(now: Date): void {
    if (this._state !== "queued") throw new Error("invariant: only a queued turn can start answering");
    this._state = "answering";
    this._answeringSince = now;
    this._answerDeadlineAt = new Date(now.getTime() + ANSWER_SECONDS * 1000);
  }

  /** FinishMyTurn ("Ya respondi"): the answering student says they are done; the teacher then decides. */
  finish(): void {
    if (this._state !== "answering") throw new AppError(ErrorCode.FLOOR_NOT_YOUR_TURN);
    this._state = "answered";
  }

  /** ResolveTurn ("Acerto" / "No acerto"). TODO(contributor): decide whether the teacher may resolve a turn still answering (not only answered). */
  resolve(verdict: TurnVerdict, now: Date): void {
    if (this._state !== "answered") throw new Error("TODO(contributor): resolving a turn that is not answered needs a rule (and maybe a code)");
    this._state = verdict;
    this._resolvedAt = now;
  }
}
