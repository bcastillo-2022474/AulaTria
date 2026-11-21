import { isUniqueViolation } from "../../db/index.js";
import { AppError, ErrorCode } from "../../shared/errors.js";
import type { Executor } from "../../shared/tx.js";
import { FloorRound, type FloorRoundStatus } from "./domain/floorRound.js";
import { FloorTurn, type FloorTurnState } from "./domain/floorTurn.js";

/**
 * WRITE-SIDE persistence of floor rounds and their turns (the queue). Concrete class, no interface.
 * Reads (GetFloorRound: the queue as the screens show it) are specialized queries in queries/.
 * Used by: OpenFloorRound, RaiseHand, LowerHand, FinishMyTurn, ResolveTurn, OpenFloorToAll, CloseFloorRound.
 */
export class FloorRoundRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only loads rounds of lessons in their own courses. */
  async findForTeacher(schoolId: string, teacherId: string, roundId: string): Promise<FloorRound | null> {
    const r = await this.db.selectFrom("floor_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "floor_round.lesson_id").onRef("lesson.school_id", "=", "floor_round.school_id"))
      .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("floor_round").where("floor_round.school_id", "=", schoolId).where("floor_round.id", "=", roundId)
      .where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** A student may load rounds of lessons in courses they are enrolled in. */
  async findForStudent(schoolId: string, studentId: string, roundId: string): Promise<FloorRound | null> {
    const r = await this.db.selectFrom("floor_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "floor_round.lesson_id").onRef("lesson.school_id", "=", "floor_round.school_id"))
      .selectAll("floor_round").where("floor_round.school_id", "=", schoolId).where("floor_round.id", "=", roundId)
      .where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
        .where("enrollment.school_id", "=", schoolId).where("enrollment.student_id", "=", studentId)))
      .executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** Every turn of the round, for the queue decisions (FloorRound.startNextTurn). Ordered by raise time. */
  async findTurns(schoolId: string, roundId: string): Promise<FloorTurn[]> {
    const rows = await this.db.selectFrom("floor_turn").selectAll().where("school_id", "=", schoolId).where("round_id", "=", roundId)
      .orderBy("raised_at").execute();
    return rows.map(toTurn);
  }

  async findTurn(schoolId: string, roundId: string, studentId: string): Promise<FloorTurn | null> {
    const r = await this.db.selectFrom("floor_turn").selectAll().where("school_id", "=", schoolId).where("round_id", "=", roundId)
      .where("student_id", "=", studentId).executeTakeFirst();
    return r ? toTurn(r) : null;
  }

  /**
   * Every command that moves the queue calls this first, inside its transaction: the round row is locked until the
   * command commits, so two raises cannot both decide that nobody is answering.
   */
  async lockRound(schoolId: string, roundId: string): Promise<void> {
    await this.db.selectFrom("floor_round").select("id").where("school_id", "=", schoolId).where("id", "=", roundId).forUpdate().execute();
  }

  async insertRound(round: FloorRound): Promise<void> {
    try {
      await this.db.insertInto("floor_round").values({
        id: round.id, school_id: round.schoolId, lesson_id: round.lessonId, prompt: round.prompt, status: round.status,
        open_to_all: round.isOpenToAll, opened_at: round.openedAt, closed_at: round.closedAt,
      }).execute();
    } catch (e) {
      if (isUniqueViolation(e)) throw new AppError(ErrorCode.FLOOR_ROUND_ALREADY_OPEN); // the partial unique index: one open round per lesson
      throw e;
    }
  }

  async saveRound(round: FloorRound): Promise<void> {
    await this.db.updateTable("floor_round").set({ status: round.status, open_to_all: round.isOpenToAll, closed_at: round.closedAt })
      .where("school_id", "=", round.schoolId).where("id", "=", round.id).execute();
  }

  /** Returns false when the student already had a turn in the round (a double tap): the caller answers idempotently. */
  async insertTurn(t: FloorTurn): Promise<boolean> {
    const res = await this.db.insertInto("floor_turn").values({
      id: t.id, school_id: t.schoolId, round_id: t.roundId, student_id: t.studentId, state: t.state, raised_at: t.raisedAt,
    }).onConflict((oc) => oc.columns(["round_id", "student_id"]).doNothing()).executeTakeFirst();
    return Number(res.numInsertedOrUpdatedRows ?? 0) > 0;
  }

  async saveTurn(t: FloorTurn): Promise<void> {
    await this.db.updateTable("floor_turn").set({
      state: t.state, answering_since: t.answeringSince, answer_deadline_at: t.answerDeadlineAt, resolved_at: t.resolvedAt,
    }).where("school_id", "=", t.schoolId).where("id", "=", t.id).execute();
  }
}

type RoundRow = { id: string; school_id: string; lesson_id: string; prompt: string; status: FloorRoundStatus; open_to_all: boolean; opened_at: Date; closed_at: Date | null };
const toRound = (r: RoundRow) => new FloorRound(r.id, r.school_id, r.lesson_id, r.prompt, r.status, r.open_to_all, r.opened_at, r.closed_at);

type TurnRow = { id: string; school_id: string; round_id: string; student_id: string; state: FloorTurnState; raised_at: Date; answering_since: Date | null; answer_deadline_at: Date | null; resolved_at: Date | null };
const toTurn = (r: TurnRow) => new FloorTurn(r.id, r.school_id, r.round_id, r.student_id, r.state, r.raised_at, r.answering_since, r.answer_deadline_at, r.resolved_at);
