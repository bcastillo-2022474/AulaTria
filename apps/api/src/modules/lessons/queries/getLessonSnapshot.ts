import { lessons } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { ts, tsOrUndef } from "../../../shared/ids.js";
import { lessonStatusToProto } from "../../courses/queries/lessonStatus.js";
import { connectedStudents, revisionOf } from "../hub.js";
import { visibleLesson } from "./visibility.js";

const count = async (q: { executeTakeFirstOrThrow(): Promise<{ n: unknown }> }) => Number((await q.executeTakeFirstOrThrow()).n);

/**
 * READ: "the class right now". Built once per change and then personalised per student (my_pulse, my_submitted)
 * so a burst of 38 taps costs one build, not 38. Every query is scoped by the tenant. Also used by the hub.
 */
export async function buildSnapshot(schoolId: string, lessonId: string) {
  const l = await db.selectFrom("lesson").innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["lesson.id", "lesson.number", "lesson.status", "lesson.started_at", "lesson.course_id", "course.name as course_name"])
    .where("lesson.school_id", "=", schoolId).where("lesson.id", "=", lessonId).executeTakeFirst();
  if (!l) throw new AppError(ErrorCode.LESSON_NOT_FOUND);

  const enrolled = await count(db.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).where("school_id", "=", schoolId).where("course_id", "=", l.course_id));
  const joined = await count(db.selectFrom("lesson_participant").select((e) => e.fn.countAll().as("n")).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId));

  // Each student's current pulse = their latest pulse_event.
  const pulseRows = await db.selectFrom("pulse_event").select(["student_id", "state"]).distinctOn("student_id")
    .where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).orderBy("student_id").orderBy("created_at", "desc").execute();
  const pulseByStudent = new Map(pulseRows.map((r) => [r.student_id, r.state]));
  const ok = pulseRows.filter((r) => r.state === "ok").length;

  // The two newest rounds are enough: a round can only open when none is open, so an open round is always the newest
  // and the last closed one is the newest closed. One query for both rounds, one for who submitted in either.
  // The server's clock decides: a round past closes_at is CLOSED here even if its expiry is not persisted yet.
  const rounds = await db.selectFrom("doubt_round").select(["id", "status", "opened_at", "closes_at"])
    .where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).orderBy("opened_at", "desc").limit(2).execute();
  const now = new Date();
  const doubt = rounds.find((r) => r.status === "open" && r.closes_at > now);
  const lastClosed = rounds.find((r) => r.status === "closed" || r.closes_at <= now);
  const submittedRows = rounds.length ? await db.selectFrom("doubt").select(["round_id", "student_id"])
    .where("school_id", "=", schoolId).where("round_id", "in", rounds.map((r) => r.id)).execute() : [];
  const submittedBy = new Map<string, Set<string>>(rounds.map((r) => [r.id, new Set<string>()]));
  for (const s of submittedRows) submittedBy.get(s.round_id)?.add(s.student_id);
  /** Counts only (and, per student, my_submitted in forUser): no student id or text ever goes in the shared snapshot. */
  const doubtRef = (r: (typeof rounds)[number]) => ({ id: r.id, openedAt: ts(r.opened_at), closesAt: ts(r.closes_at), submittedCount: submittedBy.get(r.id)!.size, mySubmitted: false });
  const floor = await db.selectFrom("floor_round").select(["id", "prompt"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").executeTakeFirst();
  const quiz = await db.selectFrom("quiz").select(["id", "closes_at"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "live").executeTakeFirst();
  const quizAnswered = quiz ? await count(db.selectFrom("quiz_answer").select((e) => e.fn.countAll().as("n")).where("school_id", "=", schoolId).where("quiz_id", "=", quiz.id)) : 0;
  const att = await db.selectFrom("attendance_round").select(["id"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").executeTakeFirst();
  const attRows = att ? await db.selectFrom("attendance_record").select("status").where("school_id", "=", schoolId).where("round_id", "=", att.id).execute() : [];

  const snapshot = {
    serverTime: ts(now), lessonId: l.id, courseId: l.course_id, courseName: l.course_name, number: l.number,
    status: lessonStatusToProto(l.status), startedAt: tsOrUndef(l.started_at),
    enrolledCount: enrolled, joinedCount: joined, connectedCount: connectedStudents(lessonId),
    pulse: { okCount: ok, lostCount: pulseRows.length - ok, okPercent: pulseRows.length ? Math.round((ok / pulseRows.length) * 100) : 0 },
    myPulse: lessons.PulseState.UNSPECIFIED,
    activeDoubtRound: doubt ? doubtRef(doubt) : undefined,
    lastClosedDoubtRound: lastClosed ? doubtRef(lastClosed) : undefined,
    activeFloorRound: floor ? { id: floor.id, prompt: floor.prompt } : undefined,
    activeQuiz: quiz ? { id: quiz.id, closesAt: tsOrUndef(quiz.closes_at), answeredCount: quizAnswered } : undefined,
    revision: revisionOf(lessonId),
    activeAttendanceRound: att ? { id: att.id, presentCount: attRows.filter((r) => r.status === "present").length, needsReviewCount: attRows.filter((r) => r.status === "needs_review").length } : undefined,
  };
  return { snapshot, pulseByStudent, submittedBy };
}

export type Built = Awaited<ReturnType<typeof buildSnapshot>>;

const withMine = <R extends { id: string } | undefined>(b: Built, userId: string, ref: R) =>
  ref && { ...ref, mySubmitted: b.submittedBy.get(ref.id)?.has(userId) ?? false };

/** Teacher gets the base snapshot; a student gets it plus their own pulse / submitted flag. */
export function forUser(b: Built, a: { userId: string; role: string }) {
  if (a.role !== "student") return b.snapshot;
  const mine = b.pulseByStudent.get(a.userId);
  return {
    ...b.snapshot,
    myPulse: mine === "ok" ? lessons.PulseState.OK : mine === "lost" ? lessons.PulseState.LOST : lessons.PulseState.UNSPECIFIED,
    activeDoubtRound: withMine(b, a.userId, b.snapshot.activeDoubtRound),
    lastClosedDoubtRound: withMine(b, a.userId, b.snapshot.lastClosedDoubtRound),
    // A student needs the id to check in, never the counters (present / to review are the teacher's).
    activeAttendanceRound: b.snapshot.activeAttendanceRound && { id: b.snapshot.activeAttendanceRound.id, presentCount: 0, needsReviewCount: 0 },
  };
}

export async function getLessonSnapshot(a: Actor, req: lessons.GetLessonSnapshotRequest) {
  await visibleLesson(a, req.lessonId);
  return { snapshot: forUser(await buildSnapshot(a.schoolId, req.lessonId), a) };
}
