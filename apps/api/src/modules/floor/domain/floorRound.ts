import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";
import type { FloorTurn } from "./floorTurn.js";

export type FloorRoundStatus = "open" | "closed";

/** The design's "+0.8 s" rule and the 0:15 answer countdown are the same clock: see FloorTurn.startAnswering. */
export const FLOOR_PROMPT_MAX_LENGTH = 300;

/**
 * "Toma de la palabra". Pure: state and rules, no I/O.
 *   open --close--> closed
 * Written by OpenFloorRound, RaiseHand, LowerHand, FinishMyTurn, ResolveTurn, OpenFloorToAll, CloseFloorRound.
 */
export class FloorRound {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly lessonId: string,
    readonly prompt: string,
    private _status: FloorRoundStatus,
    private _openToAll: boolean,
    readonly openedAt: Date,
    private _closedAt: Date | null,
  ) {}

  static open(p: { schoolId: string; lessonId: string; prompt: string; now: Date }): FloorRound {
    const prompt = p.prompt.trim();
    if (prompt.length < 1 || prompt.length > FLOOR_PROMPT_MAX_LENGTH) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
    return new FloorRound(newId(), p.schoolId, p.lessonId, prompt, "open", false, p.now, null);
  }

  get status() { return this._status; }
  get isOpenToAll() { return this._openToAll; }
  get closedAt() { return this._closedAt; }

  /** "Abrir a todos": nobody is queued any more. TODO(contributor): decide what happens to turns still queued (answered in turn, or withdrawn?). */
  openToAll(): void {
    this.assertOpen();
    this._openToAll = true;
  }

  close(now: Date): void {
    this.assertOpen();
    this._status = "closed";
    this._closedAt = now;
  }

  /** Raising a hand (and everything that changes the queue) is refused once the round is closed. */
  assertAcceptsHands(): void {
    this.assertOpen();
  }

  /**
   * The queue moves on: if nobody is answering, the first queued turn (by raisedAt) starts answering.
   * Returns the turn that started, or null when someone is already answering or the queue is empty.
   * `turns` is every turn of this round (the command loads them through FloorRoundRepository.findTurns).
   */
  startNextTurn(turns: FloorTurn[], now: Date): FloorTurn | null {
    if (turns.some((t) => t.state === "answering")) return null;
    const next = turns
      .filter((t) => t.state === "queued")
      .sort((a, b) => a.raisedAt.getTime() - b.raisedAt.getTime())[0];
    if (!next) return null;
    next.startAnswering(now);
    return next;
  }

  private assertOpen(): void {
    if (this._status !== "open") throw new AppError(ErrorCode.FLOOR_ROUND_CLOSED);
  }
}
