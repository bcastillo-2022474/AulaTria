import { newId } from "../../../shared/ids.js";

/**
 * Used by the grouping step (after CloseDoubtRound). A topic several doubts were grouped into ("3 dudas principales"). Written by the grouping step after a round
 * closes. student_count is derived (never stored): it is the number of doubts pointing at the group.
 * Pure: no I/O.
 */
export class DoubtGroup {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly roundId: string,
    /** 1 = most students. */
    readonly rank: number,
    readonly title: string,
    /** null = unknown (the proto says 0). */
    readonly slideNumber: number | null,
    readonly sampleQuote: string | null,
  ) {}

  /** TODO(contributor): the grouping step decides title, rank order and slide cross-reference (LLM output). Confirm validation rules for it. */
  static create(p: { schoolId: string; roundId: string; rank: number; title: string; slideNumber: number | null; sampleQuote: string | null }): DoubtGroup {
    return new DoubtGroup(newId(), p.schoolId, p.roundId, p.rank, p.title, p.slideNumber, p.sampleQuote);
  }
}
