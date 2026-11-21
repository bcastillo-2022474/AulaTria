import type { Executor } from "../../shared/tx.js";
import { AttendanceRound, type AttendanceRoundStatus, type ReviewReason, type RoomLocation } from "./domain/attendanceRound.js";
import { AttendanceRecord, type AttendanceStatus } from "./domain/attendanceRecord.js";

/**
 * WRITE-SIDE persistence of attendance rounds and records. Concrete class, no interface. Reads (GetAttendanceRound,
 * the review list, the totals against enrollment) are specialized queries in queries/.
 * Used by: OpenAttendanceRound, CloseAttendanceRound, CheckIn, ResolveAttendance.
 */
export class AttendanceRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only loads rounds of lessons in their own courses. */
  async findRoundForTeacher(schoolId: string, teacherId: string, roundId: string): Promise<AttendanceRound | null> {
    const r = await this.db.selectFrom("attendance_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "attendance_round.lesson_id").onRef("lesson.school_id", "=", "attendance_round.school_id"))
      .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("attendance_round").where("attendance_round.school_id", "=", schoolId).where("attendance_round.id", "=", roundId)
      .where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toRound(r) : null;
  }

  /** A student may check in to rounds of lessons in courses they are enrolled in. */
  async findRoundForStudent(schoolId: string, studentId: string, roundId: string): Promise<AttendanceRound | null> {
    const r = await this.db.selectFrom("attendance_round")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "attendance_round.lesson_id").onRef("lesson.school_id", "=", "attendance_round.school_id"))
      .selectAll("attendance_round").where("attendance_round.school_id", "=", schoolId).where("attendance_round.id", "=", roundId)
      .where((eb) => eb.exists(eb.selectFrom("enrollment").select("course_id").whereRef("enrollment.course_id", "=", "lesson.course_id")
        .where("enrollment.school_id", "=", schoolId).where("enrollment.student_id", "=", studentId)))
      .executeTakeFirst();
    return r ? toRound(r) : null;
  }

  async insertRound(round: AttendanceRound): Promise<void> {
    await this.db.insertInto("attendance_round").values({
      id: round.id, school_id: round.schoolId, lesson_id: round.lessonId, status: round.status, qr_secret: round.qrSecret,
      qr_step_seconds: round.qrStepSeconds, manual_step_seconds: round.manualStepSeconds,
      room_lat: round.room.lat, room_lng: round.room.lng, radius_m: round.radiusM, opened_at: round.openedAt, closed_at: round.closedAt,
    }).execute();
  }

  async saveRound(round: AttendanceRound): Promise<void> {
    await this.db.updateTable("attendance_round").set({ status: round.status, closed_at: round.closedAt })
      .where("school_id", "=", round.schoolId).where("id", "=", round.id).execute();
  }

  async findRecord(schoolId: string, roundId: string, studentId: string): Promise<AttendanceRecord | null> {
    const r = await this.db.selectFrom("attendance_record").selectAll().where("school_id", "=", schoolId).where("round_id", "=", roundId)
      .where("student_id", "=", studentId).executeTakeFirst();
    return r ? toRecord(r) : null;
  }

  /**
   * Insert, or rewrite the row of the same student (rescan), keyed by unique (round_id, student_id). Persists every
   * field the entity owns, so it also saves a teacher's decision.
   */
  async saveRecord(rec: AttendanceRecord): Promise<void> {
    const loc = rec.location;
    const values = {
      id: rec.id, school_id: rec.schoolId, round_id: rec.roundId, student_id: rec.studentId, status: rec.status,
      review_reason: rec.reviewReason, scanned_at: rec.scannedAt, lat: loc?.lat ?? null, lng: loc?.lng ?? null,
      accuracy_m: loc?.accuracyM ?? null, distance_m: rec.distanceM, reviewed_by_id: rec.reviewedById, reviewed_at: rec.reviewedAt,
    };
    await this.db.insertInto("attendance_record").values(values).onConflict((oc) => oc.columns(["round_id", "student_id"]).doUpdateSet({
      status: values.status, review_reason: values.review_reason, scanned_at: values.scanned_at, lat: values.lat, lng: values.lng,
      accuracy_m: values.accuracy_m, distance_m: values.distance_m, reviewed_by_id: values.reviewed_by_id, reviewed_at: values.reviewed_at,
    })).execute();
  }
}

type RoundRow = {
  id: string; school_id: string; lesson_id: string; status: AttendanceRoundStatus; qr_secret: string; qr_step_seconds: number;
  manual_step_seconds: number; room_lat: string | number; room_lng: string | number; radius_m: number; opened_at: Date; closed_at: Date | null;
};
// numeric columns come back from pg as strings
const toRound = (r: RoundRow) => new AttendanceRound(
  r.id, r.school_id, r.lesson_id, r.status, r.qr_secret, r.qr_step_seconds, r.manual_step_seconds,
  { lat: Number(r.room_lat), lng: Number(r.room_lng) } satisfies RoomLocation, r.radius_m, r.opened_at, r.closed_at,
);

type RecordRow = {
  id: string; school_id: string; round_id: string; student_id: string; status: AttendanceStatus; review_reason: ReviewReason | null;
  scanned_at: Date; lat: string | number | null; lng: string | number | null; accuracy_m: number | null; distance_m: number | null;
  reviewed_by_id: string | null; reviewed_at: Date | null;
};
const toRecord = (r: RecordRow) => new AttendanceRecord(
  r.id, r.school_id, r.round_id, r.student_id, r.status, r.review_reason, r.scanned_at,
  r.lat === null || r.lng === null || r.accuracy_m === null ? null : { lat: Number(r.lat), lng: Number(r.lng), accuracyM: r.accuracy_m },
  r.distance_m, r.reviewed_by_id, r.reviewed_at,
);
