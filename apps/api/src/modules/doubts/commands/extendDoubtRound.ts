import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { scheduleExpiry } from "../expiry.js";
import { getDoubtRound } from "../queries/getDoubtRound.js";
import { changeRoundAsTeacher } from "./loadRound.js";

/** WRITE: "+30 s". Only an open round that has not run out; the new deadline is persisted and the expiry timer re-armed. */
export async function extendDoubtRound(a: Actor, req: doubts.ExtendDoubtRoundRequest) {
  const round = await changeRoundAsTeacher(a, req.roundId, (r, now) => r.extend(req.seconds, now));
  scheduleExpiry(a.schoolId, round);
  return getDoubtRound(a, { roundId: round.id } as doubts.GetDoubtRoundRequest);
}
