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

  const doubt = await db.selectFrom("doubt_round").select(["id", "closes_at"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").executeTakeFirst();
  const submitted = doubt ? await db.selectFrom("doubt").select("student_id").where("school_id", "=", schoolId).where("round_id", "=", doubt.id).execute() : [];
  const floor = await db.selectFrom("floor_round").select(["id", "prompt"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").executeTakeFirst();
  const quiz = await db.selectFrom("quiz").select(["id", "closes_at"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "live").executeTakeFirst();
  const quizAnswered = quiz ? await count(db.selectFrom("quiz_answer").select((e) => e.fn.countAll().as("n")).where("school_id", "=", schoolId).where("quiz_id", "=", quiz.id)) : 0;
  const att = await db.selectFrom("attendance_round").select(["id"]).where("school_id", "=", schoolId).where("lesson_id", "=", lessonId).where("status", "=", "open").executeTakeFirst();
  const attRows = att ? await db.selectFrom("attendance_record").select("status").where("school_id", "=", schoolId).where("round_id", "=", att.id).execute() : [];

  const snapshot = {
    serverTime: ts(new Date()), lessonId: l.id, courseId: l.course_id, courseName: l.course_name, number: l.number,
    status: lessonStatusToProto(l.status), startedAt: tsOrUndef(l.started_at),
    enrolledCount: enrolled, joinedCount: joined, connectedCount: connectedStudents(lessonId),
    pulse: { okCount: ok, lostCount: pulseRows.length - ok, okPercent: pulseRows.length ? Math.round((ok / pulseRows.length) * 100) : 0 },
    myPulse: lessons.PulseState.UNSPECIFIED,
    activeDoubtRound: doubt ? { id: doubt.id, closesAt: ts(doubt.closes_at), submittedCount: submitted.length, mySubmitted: false } : undefined,
    activeFloorRound: floor ? { id: floor.id, prompt: floor.prompt } : undefined,
    activeQuiz: quiz ? { id: quiz.id, closesAt: tsOrUndef(quiz.closes_at), answeredCount: quizAnswered } : undefined,
    revision: revisionOf(lessonId),
    activeAttendanceRound: att ? { id: att.id, presentCount: attRows.filter((r) => r.status === "present").length, needsReviewCount: attRows.filter((r) => r.status === "needs_review").length } : undefined,
  };
  return { snapshot, pulseByStudent, submittedBy: new Set(submitted.map((s) => s.student_id)) };
}

export type Built = Awaited<ReturnType<typeof buildSnapshot>>;

/** Teacher gets the base snapshot; a student gets it plus their own pulse / submitted flag. */
export function forUser(b: Built, a: { userId: string; role: string }) {
  if (a.role !== "student") return b.snapshot;
  const mine = b.pulseByStudent.get(a.userId);
  return {
    ...b.snapshot,
    myPulse: mine === "ok" ? lessons.PulseState.OK : mine === "lost" ? lessons.PulseState.LOST : lessons.PulseState.UNSPECIFIED,
    activeDoubtRound: b.snapshot.activeDoubtRound && { ...b.snapshot.activeDoubtRound, mySubmitted: b.submittedBy.has(a.userId) },
  };
}

export async function getLessonSnapshot(a: Actor, req: lessons.GetLessonSnapshotRequest) {
  await visibleLesson(a, req.lessonId);
  return { snapshot: forUser(await buildSnapshot(a.schoolId, req.lessonId), a) };
}
