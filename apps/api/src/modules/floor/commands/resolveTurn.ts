import { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { lockedRoundForTeacher } from "./loadRound.js";
import { getFloorRound } from "../queries/getFloorRound.js";

/** The teacher's verdict on the current responder. Correct closes the round; incorrect moves the queue on. */
export async function resolveTurn(a: Actor, req: floor.ResolveTurnRequest) {
  const now = new Date();
  const verdict = req.verdict === floor.TurnVerdict.CORRECT ? "correct" : "incorrect";
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForTeacher(trx, a, req.roundId);
    const changed = round.judge(await repo.findTurns(a.schoolId, round.id), verdict, now);
    for (const t of changed) await repo.saveTurn(t); // in the order judge() returns them
    await repo.saveRound(round);
    return round.lessonId;
  });
  touch(a.schoolId, lessonId);
  return getFloorRound(a, { roundId: req.roundId } as floor.GetFloorRoundRequest);
}
