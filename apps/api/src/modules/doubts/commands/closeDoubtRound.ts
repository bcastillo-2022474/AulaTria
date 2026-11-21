import type { doubts } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { cancelExpiry } from "../expiry.js";
import { getDoubtRound } from "../queries/getDoubtRound.js";
import { changeRoundAsTeacher } from "./loadRound.js";

/**
 * WRITE: "Cerrar ahora". The round stays readable (GetDoubtRound, ListDoubts). summary_status stays PENDING: the grouping
 * is a later step (it will start from here) and is NOT run by this command.
 */
export async function closeDoubtRound(a: Actor, req: doubts.CloseDoubtRoundRequest) {
  const round = await changeRoundAsTeacher(a, req.roundId, (r, now) => r.close(now));
  cancelExpiry(round.id);
  return getDoubtRound(a, { roundId: round.id } as doubts.GetDoubtRoundRequest);
}
