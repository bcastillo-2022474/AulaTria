import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export type DoubtRoundStatus = "open" | "closed";
export type SummaryStatus = "pending" | "ready" | "failed";

/** The design's default: "Ronda de dudas (30 s)". 0 from the API means this. */
const DEFAULT_DURATION_SECONDS = 30;

/**
 * "Ronda de dudas". Pure: state and rules, no I/O.
 * Written by OpenDoubtRound, ExtendDoubtRound, CloseDoubtRound and the grouping step.
 *
 *   (none) --open--> open --close--> closed (summary pending) --grouping--> ready | failed
 */
export class DoubtRound {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly lessonId: string,
    private _status: DoubtRoundStatus,
    private _summaryStatus: SummaryStatus,
    readonly openedAt: Date,
    private _closesAt: Date,
    private _closedAt: Date | null,
  ) {}

  static open(p: { schoolId: string; lessonId: string; durationSeconds: number; now: Date }): DoubtRound {
    const seconds = p.durationSeconds > 0 ? p.durationSeconds : DEFAULT_DURATION_SECONDS;
    return new DoubtRound(newId(), p.schoolId, p.lessonId, "open", "pending", p.now, new Date(p.now.getTime() + seconds * 1000), null);
  }

  get status() { return this._status; }
  get summaryStatus() { return this._summaryStatus; }
  get closesAt() { return this._closesAt; }
  get closedAt() { return this._closedAt; }

  /**
   * The server is the authority on the clock: a round is OPEN only until closes_at, whether or not anyone pressed
   * "Cerrar ahora" and whether or not the expiry has been persisted yet.
   */
  isOpen(now: Date): boolean {
    return this._status === "open" && now.getTime() < this._closesAt.getTime();
  }

  /**
   * The deadline passed and nobody closed the round: closes it AT the deadline. Returns true only for the call that
   * made the transition (false when it is already closed or not yet due), so it never happens twice.
   */
  expire(now: Date): boolean {
    if (this._status !== "open" || now.getTime() < this._closesAt.getTime()) return false;
    this._status = "closed";
    this._closedAt = this._closesAt;
    return true;
  }

  /** "+30 s": moves the deadline the clients count down to. 0 = 30. An expired round cannot be extended. */
  extend(seconds: number, now: Date): void {
    this.assertOpen(now);
    const s = seconds > 0 ? seconds : DEFAULT_DURATION_SECONDS;
    this._closesAt = new Date(this._closesAt.getTime() + s * 1000);
  }

  /** Explicit close. Grouping starts after this (summary_status stays pending until the grouping step finishes). */
  close(now: Date): void {
    this.assertOpen(now);
    this._status = "closed";
    this._closedAt = now;
  }

  /** A student may submit or edit a doubt only while the round is open (and not past its deadline). */
  assertAcceptsDoubts(now: Date): void {
    this.assertOpen(now);
  }

  /** Grouping finished. Only from pending. */
  markSummaryReady(): void {
    this.assertSummaryPending();
    this._summaryStatus = "ready";
  }

  /** Grouping failed; the original doubts stay. TODO(contributor): decide whether a failed summary can be retried. */
  markSummaryFailed(): void {
    this.assertSummaryPending();
    this._summaryStatus = "failed";
  }

  private assertOpen(now: Date): void {
    if (!this.isOpen(now)) throw new AppError(ErrorCode.DOUBT_ROUND_CLOSED);
  }

  private assertSummaryPending(): void {
    if (this._summaryStatus !== "pending") throw new Error("invariant: the summary of this doubt round is already finished");
  }
}
