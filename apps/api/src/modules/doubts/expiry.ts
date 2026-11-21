import { inTx } from "../../shared/tx.js";
import { touch } from "../lessons/hub.js";
import type { DoubtRound } from "./domain/doubtRound.js";
import { DoubtRoundRepository } from "./repository.js";

/**
 * Closing a doubt round when its time runs out. The server is the authority, in two layers:
 *
 *  1. Every read and every command goes by the entity's clock (`DoubtRound.isOpen(now)`): past closes_at a round is
 *     closed for everybody, whether or not the transition has been persisted yet.
 *  2. This timer PERSISTS the transition and pushes it to WatchLesson (touch). One in-process timer per open round,
 *     in the style of the hub's debounce (single API instance, no scheduler, no queue). OpenDoubtRound and
 *     ExtendDoubtRound (re)schedule it, CloseDoubtRound cancels it.
 *
 * The timer is an optimisation of layer 2, never of correctness: the round row is locked and the entity decides
 * (`expire` is true for exactly one caller), so a timer racing "Cerrar ahora" or "+30 s" closes the round once, and a
 * timer that fires after an extension finds the round not due and re-arms itself. A restart loses the timers: reads
 * still show those rounds closed, and the next command on the lesson persists it (OpenDoubtRound settles it first).
 */
const timers = new Map<string, NodeJS.Timeout>();
const SLACK_MS = 50; // timers can fire a hair early; closes_at must have passed

export function scheduleExpiry(schoolId: string, round: DoubtRound): void {
  cancelExpiry(round.id);
  const delay = Math.max(0, round.closesAt.getTime() - Date.now()) + SLACK_MS;
  const timer = setTimeout(() => { timers.delete(round.id); void closeIfExpired(schoolId, round.id); }, delay);
  timer.unref(); // never keeps the process alive
  timers.set(round.id, timer);
}

export function cancelExpiry(roundId: string): void {
  const timer = timers.get(roundId);
  if (timer) clearTimeout(timer);
  timers.delete(roundId);
}

async function closeIfExpired(schoolId: string, roundId: string): Promise<void> {
  try {
    const outcome = await inTx(async (trx) => {
      const repo = new DoubtRoundRepository(trx);
      await repo.lockRound(schoolId, roundId);
      const round = await repo.findById(schoolId, roundId);
      if (!round) return null;
      const closed = round.expire(new Date());
      if (closed) await repo.saveRound(round);
      return { round, closed };
    });
    if (!outcome) return;
    if (outcome.closed) touch(schoolId, outcome.round.lessonId); // the snapshot now shows it closed
    else if (outcome.round.status === "open") scheduleExpiry(schoolId, outcome.round); // fired early or the deadline moved
  } catch (e) {
    console.error("doubt round expiry failed", roundId, e);
  }
}
