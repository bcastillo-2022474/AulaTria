import type { MessageInitShape } from "@bufbuild/protobuf";
import { lessons } from "@aula/rpc";
import { sql } from "kysely";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ (Docente-Pulso): the class after the fact, all derived. One lesson of the teacher's own course.
 * - pulse_series: per minute of the class, how many participants had "lost" as their latest state at the end of it.
 * - markers: doubt rounds and quizzes, placed on the same minute axis.
 * - peak: the minute with the most "me perdi" and the top doubt group of the round closest to it.
 */
export async function getLessonReview(a: Actor, req: lessons.GetLessonReviewRequest): Promise<MessageInitShape<typeof lessons.GetLessonReviewResponseSchema>> {
  const lesson = await db.selectFrom("lesson")
    .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
    .select(["lesson.id", "lesson.number", "lesson.started_at", "lesson.ended_at", "lesson.course_id", "course.name as course_name"])
    .where("lesson.school_id", "=", a.schoolId).where("lesson.id", "=", req.lessonId).where("course.teacher_id", "=", a.userId)
    .executeTakeFirst();
  if (!lesson) throw new AppError(ErrorCode.LESSON_NOT_FOUND);

  const started = lesson.started_at;
  const endedAt = lesson.ended_at ?? new Date();
  const durationSeconds = started ? Math.max(0, Math.round((endedAt.getTime() - started.getTime()) / 1000)) : 0;
  const minutesCount = started ? Math.max(1, Math.ceil(durationSeconds / 60)) : 0;
  const minuteOf = (t: Date) => (started ? Math.max(0, Math.floor((t.getTime() - started.getTime()) / 60_000)) : 0);

  const enrolled = await db.selectFrom("enrollment").select((e) => e.fn.countAll().as("n"))
    .where("school_id", "=", a.schoolId).where("course_id", "=", lesson.course_id).executeTakeFirstOrThrow();

  // Pulse: for every minute bucket and every participant, the latest pulse event up to the end of that minute (none = ok).
  const series = started ? (await sql<{ minute: number; total: number; lost: number }>`
    select m.minute::int as minute, count(*)::int as total,
           (count(*) filter (where ls.state = 'lost'))::int as lost
    from generate_series(0, ${minutesCount - 1}::int) as m(minute)
    cross join lesson_participant p
    left join lateral (
      select pe.state from pulse_event pe
      where pe.school_id = ${a.schoolId} and pe.lesson_id = ${lesson.id} and pe.student_id = p.student_id
        and pe.created_at < ${started}::timestamptz + (m.minute + 1) * interval '1 minute'
      order by pe.created_at desc limit 1
    ) ls on true
    where p.school_id = ${a.schoolId} and p.lesson_id = ${lesson.id}
    group by m.minute order by m.minute`.execute(db)).rows : [];
  const pulseSeries = series.map((r) => ({ minute: r.minute, lostCount: r.lost, okPercent: r.total ? Math.round(100 * (r.total - r.lost) / r.total) : 100 }));

  const rounds = await db.selectFrom("doubt_round")
    .select((eb) => ["doubt_round.id", "doubt_round.opened_at", eb.selectFrom("doubt").select((e) => e.fn.countAll().as("n"))
      .whereRef("doubt.round_id", "=", "doubt_round.id").where("doubt.school_id", "=", a.schoolId).as("doubts")])
    .where("doubt_round.school_id", "=", a.schoolId).where("doubt_round.lesson_id", "=", lesson.id).orderBy("doubt_round.opened_at").execute();
  const quizzes = await db.selectFrom("quiz").select(["id", "launched_at", "slide_number"])
    .where("school_id", "=", a.schoolId).where("lesson_id", "=", lesson.id).where("launched_at", "is not", null).orderBy("launched_at").execute();

  const markers = [
    ...rounds.map((r) => ({ minute: minuteOf(r.opened_at), kind: lessons.MarkerKind.DOUBTS, refId: r.id, label: `Dudas (${Number(r.doubts ?? 0)})` })),
    ...quizzes.map((q) => ({ minute: minuteOf(q.launched_at!), kind: lessons.MarkerKind.QUIZ, refId: q.id, label: q.slide_number ? `Quiz, diapositiva ${q.slide_number}` : "Quiz" })),
  ].sort((x, y) => x.minute - y.minute);

  // Peak = most lost (earliest wins a tie); what was going on = top group of the doubt round nearest in time.
  let peak: MessageInitShape<typeof lessons.PeakMomentSchema> | undefined;
  const top = pulseSeries.reduce<(typeof pulseSeries)[number] | undefined>((best, p) => (!best || p.lostCount > best.lostCount ? p : best), undefined);
  if (top && top.lostCount > 0) {
    peak = { minute: top.minute, lostCount: top.lostCount, topDoubt: "", slideNumber: 0 };
    const near = rounds.map((r) => ({ r, d: Math.abs(minuteOf(r.opened_at) - top.minute) })).sort((x, y) => x.d - y.d)[0];
    if (near && near.d <= 10) {
      const g = await db.selectFrom("doubt_group").select(["title", "slide_number"])
        .where("school_id", "=", a.schoolId).where("round_id", "=", near.r.id).orderBy("rank").limit(1).executeTakeFirst();
      if (g) peak = { ...peak, topDoubt: g.title, slideNumber: g.slide_number ?? 0 };
    }
  }

  // Attendance: one row per student (latest record across the lesson's rounds); no record = absent.
  const att = await db.selectFrom("attendance_record")
    .innerJoin("attendance_round", (j) => j.onRef("attendance_round.id", "=", "attendance_record.round_id").onRef("attendance_round.school_id", "=", "attendance_record.school_id"))
    .select(["attendance_record.student_id", "attendance_record.status", "attendance_record.scanned_at"])
    .where("attendance_record.school_id", "=", a.schoolId).where("attendance_round.lesson_id", "=", lesson.id)
    .orderBy("attendance_record.scanned_at").execute();
  const latest = new Map(att.map((r) => [r.student_id, r.status]));
  const present = [...latest.values()].filter((s) => s === "present").length;
  const needsReview = [...latest.values()].filter((s) => s === "needs_review").length;
  const enrolledCount = Number(enrolled.n);

  return {
    lessonId: lesson.id, courseId: lesson.course_id, courseName: lesson.course_name, number: lesson.number, durationSeconds,
    enrolledCount, pulseSeries, markers, peak,
    attendance: { present, needsReview, absent: Math.max(0, enrolledCount - present - needsReview) },
  };
}
