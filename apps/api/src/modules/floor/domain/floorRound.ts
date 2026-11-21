import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";
import type { FloorTurn, TurnVerdict } from "./floorTurn.js";

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

  /**
   * "Abrir a todos". Decision: it only sets the flag the screens show. The round stays open, the queue is left
   * exactly as it is (queued students keep their places and are still answered in order), and nobody is moved.
   */
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

  /**
   * ResolveTurn: the verdict on the current responder. Returns the turns that changed, in the order they must be
   * saved (the resolved one first: the database allows one "answering" turn per round at any moment).
   *   correct   -> the turn is correct and the round CLOSES (the question was answered).
   *   incorrect -> the turn is incorrect and the next queued student answers; with nobody waiting the round stays
   *                open so new hands can be raised.
   */
  judge(turns: FloorTurn[], verdict: TurnVerdict, now: Date): FloorTurn[] {
    this.assertOpen();
    const responder = turns.find((t) => t.isResponder);
    if (!responder) throw new AppError(ErrorCode.FLOOR_NOT_YOUR_TURN);
    responder.resolve(verdict, now);
    if (verdict === "correct") {
      this.close(now);
      return [responder];
    }
    const next = this.startNextTurn(turns, now);
    return next ? [responder, next] : [responder];
  }

  private assertOpen(): void {
    if (this._status !== "open") throw new AppError(ErrorCode.FLOOR_ROUND_CLOSED);
  }
}
