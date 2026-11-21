import { attendance } from "@aula/rpc";
import type { MessageInitShape } from "@bufbuild/protobuf";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { reasonToProto, statusToProto } from "../mappers.js";
import { ownedAttendanceRound } from "./visibility.js";

const count = async (q: { executeTakeFirstOrThrow(): Promise<{ n: unknown }> }) => Number((await q.executeTakeFirstOrThrow()).n);

/**
 * READ: the teacher's view of a round (live counters and the review list), for the open screen and the closed one.
 *  - present_count: records that are PRESENT (by location or accepted by the teacher).
 *  - in_room_count: PRESENT by location alone (no review reason): the ones physically inside the radius.
 *  - review: every record that ever needed review, oldest scan first. It stays listed after the teacher decides
 *    (status PRESENT / ABSENT) so the screen can show the outcome; NEEDS_REVIEW means it is still pending.
 * Students without a record are absent (derived against enrollment); they are not listed.
 */
export async function getAttendanceRound(a: Actor, req: attendance.GetAttendanceRoundRequest): Promise<{ round: MessageInitShape<typeof attendance.AttendanceRoundSchema> }> {
  const round = await ownedAttendanceRound(a, req.roundId);
  const records = () => db.selectFrom("attendance_record").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("round_id", "=", round.id);
  const enrolled = await count(db.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("course_id", "=", round.course_id));
  const present = await count(records().where("status", "=", "present"));
  const inRoom = await count(records().where("status", "=", "present").where("review_reason", "is", null));
  const review = await db.selectFrom("attendance_record")
    .innerJoin("app_user", (j) => j.onRef("app_user.id", "=", "attendance_record.student_id").onRef("app_user.school_id", "=", "attendance_record.school_id"))
    .select(["attendance_record.student_id", "app_user.name", "attendance_record.review_reason", "attendance_record.distance_m", "attendance_record.status"])
    .where("attendance_record.school_id", "=", a.schoolId).where("attendance_record.round_id", "=", round.id)
    .where("attendance_record.review_reason", "is not", null)
    .orderBy("attendance_record.scanned_at").orderBy("attendance_record.id").execute();

  return {
    round: {
      id: round.id, lessonId: round.lesson_id,
      status: round.status === "open" ? attendance.AttendanceRoundStatus.OPEN : attendance.AttendanceRoundStatus.CLOSED,
      presentCount: present, enrolledCount: enrolled, inRoomCount: inRoom,
      review: review.map((r) => ({ studentId: r.student_id, name: r.name, reason: reasonToProto(r.review_reason), distanceM: r.distance_m ?? 0, status: statusToProto(r.status) })),
    },
  };
}
