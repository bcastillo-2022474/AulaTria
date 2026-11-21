import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { lockedRoundForTeacher } from "./loadRound.js";
import { getFloorRound } from "../queries/getFloorRound.js";

/** "Abrir a todos": sets the flag only. The round stays open and the queue is untouched. */
export async function openFloorToAll(a: Actor, req: floor.OpenFloorToAllRequest) {
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForTeacher(trx, a, req.roundId);
    round.openToAll();
    await repo.saveRound(round);
    return round.lessonId;
  });
  touch(a.schoolId, lessonId);
  return getFloorRound(a, { roundId: req.roundId } as floor.GetFloorRoundRequest);
}
