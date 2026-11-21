import type { floor } from "@aula/rpc";
import type { Actor } from "../../../shared/context.js";
import { inTx } from "../../../shared/tx.js";
import { touch } from "../../lessons/hub.js";
import { lockedRoundForTeacher } from "./loadRound.js";
import { getFloorRound } from "../queries/getFloorRound.js";

export async function closeFloorRound(a: Actor, req: floor.CloseFloorRoundRequest) {
  const now = new Date();
  const lessonId = await inTx(async (trx) => {
    const { repo, round } = await lockedRoundForTeacher(trx, a, req.roundId);
    round.close(now);
    await repo.saveRound(round);
    return round.lessonId;
  });
  touch(a.schoolId, lessonId);
  return getFloorRound(a, { roundId: req.roundId } as floor.GetFloorRoundRequest);
}
