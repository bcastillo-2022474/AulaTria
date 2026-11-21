import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { lockedRoundForStudent } from "./loadRound.js";

/** "Salir de la fila". Queued or answering; if the responder leaves, the next queued student answers. No turn = no-op. */
export async function lowerHand(a: Actor, req: floor.LowerHandRequest) {
  const now = new Date();
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForStudent(trx, a, req.roundId);
    round.assertAcceptsHands();
    const turn = await repo.findTurn(a.schoolId, round.id, a.userId);
    if (!turn) return null;
    turn.lower();
    await repo.saveTurn(turn);
    const started = round.startNextTurn(await repo.findTurns(a.schoolId, round.id), now);
    if (started) await repo.saveTurn(started);
    return round.lessonId;
  });
  if (lessonId) touch(a.schoolId, lessonId);
  return {};
}
