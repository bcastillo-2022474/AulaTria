import type { doubts } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { visibleDoubtRound } from "./visibility.js";

/**
 * READ (teacher): the doubts as the students wrote them, for "Ver las N dudas originales". Anonymous on purpose: the
 * author (student_id) is never selected, so it cannot reach the response. Available before and after the round closes.
 * Order: oldest first, id as the tie-break, so the list is the same on every call.
 */
export async function listDoubts(a: Actor, req: doubts.ListDoubtsRequest) {
  const round = await visibleDoubtRound(a, req.roundId); // the teacher must own the course; other school / not yours = NOT_FOUND
  const rows = await db.selectFrom("doubt").select(["id", "text", "group_id"])
    .where("school_id", "=", a.schoolId).where("round_id", "=", round.id)
    .orderBy("created_at").orderBy("id").execute();
  return { doubts: rows.map((r) => ({ id: r.id, text: r.text, groupId: r.group_id ?? "" })) };
}
