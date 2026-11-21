import { isUniqueViolation } from "../../db/index.js";
import { AppError, ErrorCode } from "../../shared/errors.js";
import type { Executor } from "../../shared/tx.js";
import { DoubtRound, type DoubtRoundStatus, type SummaryStatus } from "./domain/doubtRound.js";
import { Doubt } from "./domain/doubt.js";
import { DoubtGroup } from "./domain/doubtGroup.js";

/**
 * WRITE-SIDE persistence of doubt rounds, the doubts students write in them, and the groups the grouping step
 * produces. Concrete class, no interface. Reads (GetDoubtRound, ListDoubts) are specialized queries in queries/.
 * Used by: OpenDoubtRound, ExtendDoubtRound, CloseDoubtRound, SubmitDoubt, and the grouping step.
 */
export class DoubtRoundRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only loads rounds of lessons in their own courses. */
  async findForTeacher(schoolId: string, teacherId: string, roundId: string): Promise<DoubtRound | null> {
    const r = await this.db.selectFrom("doubt_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "doubt_round.lesson_id").onRef("lesson.school_id", "=", "doubt_round.school_id"))
      .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("doubt_round").where("doubt_round.school_id", "=", schoolId).where("doubt_round.id", "=", roundId)
      .where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** A student may load rounds of lessons in courses they are enrolled in. */
  async findForStudent(schoolId: string, studentId: string, roundId: string): Promise<DoubtRound | null> {
    const r = await this.db.selectFrom("doubt_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "doubt_round.lesson_id").onRef("lesson.school_id", "=", "doubt_round.school_id"))
      .selectAll("doubt_round").where("doubt_round.school_id", "=", schoolId).where("doubt_round.id", "=", roundId)
      .where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
        .where("enrollment.school_id", "=", schoolId).where("enrollment.student_id", "=", studentId)))
      .executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /**
   * Every command that changes a round's state calls this first, inside its transaction: the row stays locked until
   * the command commits, so "+30 s", "Cerrar ahora" and the expiry timer decide one at a time.
   */
  async lockRound(schoolId: string, roundId: string): Promise<void> {
    await this.db.selectFrom("doubt_round").select("id").where("school_id", "=", schoolId).where("id", "=", roundId).forUpdate().execute();
  }

  /**
   * SubmitDoubt takes this instead of the exclusive lock: many students may write at once (share locks do not block each
   * other) but a "Cerrar ahora", "+30 s" or the expiry (exclusive lock) cannot slip in between reading the round as open
   * and saving the doubt. Duplicates are not this lock's job: the unique (round_id, student_id) is.
   */
  async lockRoundShared(schoolId: string, roundId: string): Promise<void> {
    await this.db.selectFrom("doubt_round").select("id").where("school_id", "=", schoolId).where("id", "=", roundId).forShare().execute();
  }

  /** By id, no actor: for the expiry timer, which is the server acting on its own clock (the tenant is still a filter). */
  async findById(schoolId: string, roundId: string): Promise<DoubtRound | null> {
    const r = await this.db.selectFrom("doubt_round").selectAll().where("school_id", "=", schoolId).where("id", "=", roundId).executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** The round of a lesson still marked open (locked), expired or not: OpenDoubtRound settles it before opening another. */
  async findOpenForLesson(schoolId: string, lessonId: string): Promise<DoubtRound | null> {
    const r = await this.db.selectFrom("doubt_round").selectAll().where("school_id", "=", schoolId).where("lesson_id", "=", lessonId)
      .where("status", "=", "open").forUpdate().executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** The student's own doubt in this round, if any (SubmitDoubt uses it to decide between a new one and an edit). */
  async findDoubt(schoolId: string, roundId: string, studentId: string): Promise<Doubt | null> {
    const r = await this.db.selectFrom("doubt").selectAll().where("school_id", "=", schoolId).where("round_id", "=", roundId)
      .where("student_id", "=", studentId).executeTakeFirst();
    return r ? toDoubt(r) : null;
  }

  async insertRound(round: DoubtRound): Promise<void> {
    try {
      await this.db.insertInto("doubt_round").values({
        id: round.id, school_id: round.schoolId, lesson_id: round.lessonId, status: round.status, summary_status: round.summaryStatus,
        opened_at: round.openedAt, closes_at: round.closesAt, closed_at: round.closedAt,
      }).execute();
    } catch (e) {
      if (isUniqueViolation(e)) throw new AppError(ErrorCode.DOUBT_ROUND_ALREADY_OPEN); // the partial unique index: one open round per lesson
      throw e;
    }
  }

  /** Persists the state the entity moved to (status, deadline, summary). */
  async saveRound(round: DoubtRound): Promise<void> {
    await this.db.updateTable("doubt_round").set({
      status: round.status, summary_status: round.summaryStatus, closes_at: round.closesAt, closed_at: round.closedAt,
    }).where("school_id", "=", round.schoolId).where("id", "=", round.id).execute();
  }

  /**
   * Insert, or edit the text if the student already has a doubt in the round (unique (round_id, student_id)).
   * The created_at of an existing row is kept. The id of an existing row is kept too: load it first if the caller needs it.
   */
  async upsertDoubt(d: Doubt): Promise<void> {
    await this.db.insertInto("doubt").values({
      id: d.id, school_id: d.schoolId, round_id: d.roundId, student_id: d.studentId, group_id: d.groupId,
      text: d.text, created_at: d.createdAt, edited_at: d.editedAt,
    }).onConflict((oc) => oc.columns(["round_id", "student_id"]).doUpdateSet({
      // A conflict means the student already had a doubt (an edit, or a double tap that raced): the row keeps its id and
      // created_at, takes the new text, and is marked edited even when this entity was built as a first submission.
      text: d.text, edited_at: d.editedAt ?? d.createdAt,
    })).execute();
  }

  /**
   * Writes the groups of a round and points each doubt at its group. Used by the grouping step, in its transaction.
   * TODO(contributor): a re-grouping must first clear the previous groups of the round. Decide how.
   */
  async saveGroups(schoolId: string, roundId: string, groups: DoubtGroup[], assignments: { doubtId: string; groupId: string }[]): Promise<void> {
    if (groups.length > 0) {
      await this.db.insertInto("doubt_group").values(groups.map((g) => ({
        id: g.id, school_id: schoolId, round_id: roundId, rank: g.rank, title: g.title,
        slide_number: g.slideNumber, sample_quote: g.sampleQuote,
      }))).execute();
    }
    for (const a of assignments) {
      await this.db.updateTable("doubt").set({ group_id: a.groupId })
        .where("school_id", "=", schoolId).where("round_id", "=", roundId).where("id", "=", a.doubtId).execute();
    }
  }
}

type RoundRow = { id: string; school_id: string; lesson_id: string; status: DoubtRoundStatus; summary_status: SummaryStatus; opened_at: Date; closes_at: Date; closed_at: Date | null };
const toRound = (r: RoundRow) => new DoubtRound(r.id, r.school_id, r.lesson_id, r.status, r.summary_status, r.opened_at, r.closes_at, r.closed_at);

type DoubtRow = { id: string; school_id: string; round_id: string; student_id: string; text: string; created_at: Date; edited_at: Date | null; group_id: string | null };
const toDoubt = (r: DoubtRow) => new Doubt(r.id, r.school_id, r.round_id, r.student_id, r.text, r.created_at, r.edited_at, r.group_id);
