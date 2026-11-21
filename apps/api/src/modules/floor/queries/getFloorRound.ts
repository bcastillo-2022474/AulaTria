import { floor } from "@aula/rpc";
import type { MessageInitShape } from "@bufbuild/protobuf";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { ts } from "../../../shared/ids.js";
import { visibleFloorRound } from "./visibility.js";

/** READ: the round as this actor sees it. One message for both roles; the student-only fields stay unset for a teacher. */
export async function getFloorRound(a: Actor, req: floor.GetFloorRoundRequest): Promise<{ round: MessageInitShape<typeof floor.FloorRoundSchema> }> {
  const round = await visibleFloorRound(a, req.roundId);
  // Names come from app_user, tenant-scoped on both sides of the join. Waiting and responding turns only.
  const turns = await db.selectFrom("floor_turn")
    .innerJoin("app_user", (j) => j.onRef("app_user.id", "=", "floor_turn.student_id").onRef("app_user.school_id", "=", "floor_turn.school_id"))
    .select(["floor_turn.student_id", "floor_turn.state", "floor_turn.raised_at", "floor_turn.answer_deadline_at", "app_user.name"])
    .where("floor_turn.school_id", "=", a.schoolId).where("floor_turn.round_id", "=", round.id)
    .orderBy("floor_turn.raised_at").execute();

  const responder = turns.find((t) => t.state === "answering" || t.state === "answered");
  const waiting = turns.filter((t) => t.state === "queued"); // already ordered by raised_at

  const message: MessageInitShape<typeof floor.FloorRoundSchema> = {
    id: round.id,
    lessonId: round.lesson_id,
    prompt: round.prompt,
    status: round.status === "open" ? floor.FloorRoundStatus.OPEN : floor.FloorRoundStatus.CLOSED,
    openToAll: round.open_to_all,
    current: responder
      ? {
        studentId: responder.student_id,
        name: responder.name,
        deadlineAt: responder.state === "answering" && responder.answer_deadline_at ? ts(responder.answer_deadline_at) : undefined,
      }
      : undefined,
    queue: waiting.map((t) => ({
      studentId: t.student_id,
      name: t.name,
      offsetMs: Math.round(t.raised_at.getTime() - round.opened_at.getTime()),
    })),
  };

  if (a.role === "student") {
    const mine = turns.find((t) => t.student_id === a.userId);
    message.myState = mine ? floorTurnStateToProto(mine.state) : floor.FloorTurnState.UNSPECIFIED;
    // 1-based place in the line, the responder counting as 1. Waiting students come after the responder, if any.
    if (mine?.state === "answering" || mine?.state === "answered") message.myPosition = 1;
    else if (mine?.state === "queued") message.myPosition = (responder ? 1 : 0) + waiting.indexOf(mine) + 1;
    else message.myPosition = 0;
  }
  return { round: message };
}

/** DB state -> proto enum. Explicit on purpose: a new DB state must fail loudly here, not map to 0. */
export function floorTurnStateToProto(state: string): floor.FloorTurnState {
  switch (state) {
    case "queued": return floor.FloorTurnState.QUEUED;
    case "answering": return floor.FloorTurnState.ANSWERING;
    case "answered": return floor.FloorTurnState.ANSWERED;
    case "correct": return floor.FloorTurnState.CORRECT;
    case "incorrect": return floor.FloorTurnState.INCORRECT;
    case "withdrawn": return floor.FloorTurnState.WITHDRAWN;
    default: throw new Error(`unknown floor turn state ${state}`);
  }
}
