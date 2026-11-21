import type { MessageInitShape } from "@bufbuild/protobuf";
import type { followup } from "@aula/rpc";
import { sql } from "kysely";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/**
 * READ (Docente-Perfil): participation of one student in one of the teacher's courses, derived from the lesson tables.
 * Describes use of the app, not comprehension. The private note travels here, which is why the RPC is teacher-only.
 */
export async function getStudentParticipation(a: Actor, req: followup.GetStudentParticipationRequest): Promise<MessageInitShape<typeof followup.GetStudentParticipationResponseSchema>> {
  const course = await db.selectFrom("course").select("id")
    .where("school_id", "=", a.schoolId).where("id", "=", req.courseId).where("teacher_id", "=", a.userId).executeTakeFirst();
  if (!course) throw new AppError(ErrorCode.COURSE_NOT_FOUND);
  const student = await db.selectFrom("enrollment")
    .innerJoin("app_user", (j) => j.onRef("app_user.id", "=", "enrollment.student_id").onRef("app_user.school_id", "=", "enrollment.school_id"))
    .select(["app_user.id", "app_user.name"])
    .where("enrollment.school_id", "=", a.schoolId).where("enrollment.course_id", "=", course.id).where("enrollment.student_id", "=", req.studentId)
    .where("app_user.role", "=", "student").executeTakeFirst();
  if (!student) throw new AppError(ErrorCode.COURSE_NOT_ENROLLED);

  // One row per ended lesson: did the student attend, and how many things did they do (pulse, doubt, turn, quiz answer).
  const per = (await sql<{ number: number; attended: boolean; interactions: number; doubts: number; turns: number; correct: number }>`
    select l.number,
      exists (select 1 from lesson_participant lp where lp.school_id = ${a.schoolId} and lp.lesson_id = l.id and lp.student_id = ${student.id}) as attended,
      ((select count(*) from pulse_event pe where pe.school_id = ${a.schoolId} and pe.lesson_id = l.id and pe.student_id = ${student.id})
       + (select count(*) from doubt d join doubt_round dr on dr.id = d.round_id and dr.school_id = d.school_id
            where d.school_id = ${a.schoolId} and dr.lesson_id = l.id and d.student_id = ${student.id})
       + (select count(*) from floor_turn ft join floor_round fr on fr.id = ft.round_id and fr.school_id = ft.school_id
            where ft.school_id = ${a.schoolId} and fr.lesson_id = l.id and ft.student_id = ${student.id})
       + (select count(*) from quiz_answer qa join quiz q on q.id = qa.quiz_id and q.school_id = qa.school_id
            where qa.school_id = ${a.schoolId} and q.lesson_id = l.id and qa.student_id = ${student.id}))::int as interactions,
      (select count(*) from doubt d join doubt_round dr on dr.id = d.round_id and dr.school_id = d.school_id
         where d.school_id = ${a.schoolId} and dr.lesson_id = l.id and d.student_id = ${student.id})::int as doubts,
      (select count(*) from floor_turn ft join floor_round fr on fr.id = ft.round_id and fr.school_id = ft.school_id
         where ft.school_id = ${a.schoolId} and fr.lesson_id = l.id and ft.student_id = ${student.id} and ft.state in ('correct', 'incorrect'))::int as turns,
      (select count(*) from floor_turn ft join floor_round fr on fr.id = ft.round_id and fr.school_id = ft.school_id
         where ft.school_id = ${a.schoolId} and fr.lesson_id = l.id and ft.student_id = ${student.id} and ft.state = 'correct')::int as correct
    from lesson l
    where l.school_id = ${a.schoolId} and l.course_id = ${course.id} and l.status = 'ended'
    order by l.number`.execute(db)).rows;

  const quizTotal = await db.selectFrom("quiz").innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "quiz.lesson_id").onRef("lesson.school_id", "=", "quiz.school_id"))
    .select((e) => e.fn.countAll().as("n")).where("quiz.school_id", "=", a.schoolId).where("lesson.course_id", "=", course.id).where("quiz.launched_at", "is not", null).executeTakeFirstOrThrow();
  const quizAnswered = await db.selectFrom("quiz_answer")
    .innerJoin("quiz", (j) => j.onRef("quiz.id", "=", "quiz_answer.quiz_id").onRef("quiz.school_id", "=", "quiz_answer.school_id"))
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "quiz.lesson_id").onRef("lesson.school_id", "=", "quiz.school_id"))
    .select((e) => e.fn.countAll().as("n")).where("quiz_answer.school_id", "=", a.schoolId).where("lesson.course_id", "=", course.id)
    .where("quiz_answer.student_id", "=", student.id).executeTakeFirstOrThrow();
  const note = await db.selectFrom("student_note").select("body")
    .where("school_id", "=", a.schoolId).where("course_id", "=", course.id).where("student_id", "=", student.id).executeTakeFirst();

  return {
    studentId: student.id, name: student.name,
    lessonsAttended: per.filter((p) => p.attended).length, lessonsTotal: per.length,
    doubtsSent: per.reduce((s, p) => s + p.doubts, 0),
    turnsTaken: per.reduce((s, p) => s + p.turns, 0), turnsCorrect: per.reduce((s, p) => s + p.correct, 0),
    quizzesAnswered: Number(quizAnswered.n), quizzesTotal: Number(quizTotal.n),
    perLesson: per.map((p) => ({ number: p.number, interactions: p.interactions })),
    note: note?.body ?? "",
  };
}
