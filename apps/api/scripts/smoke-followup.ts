// End-to-end check of Flow 5 (class review + student follow-up) against a RUNNING api: API_URL=http://localhost:3101 pnpm smoke:followup
// Reads the seeded past lessons of "Bases de Datos 2" (CC3057), so the numbers below are the seed's. Read-only, except the
// teacher note of the student that already has one, which is rewritten and then restored.
import { ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, followup, identity, lessons } from "@aula/rpc";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
const NIL_UUID = "00000000-0000-4000-8000-000000000000";
let failures = 0;
const check = (name: string, ok: boolean, extra = "") => { console.log(`${ok ? "PASS" : "FAIL"}  ${name}${extra ? "  " + extra : ""}`); if (!ok) failures++; };
const withAuth = (token: string, school: string): Interceptor => (next) => async (req) => {
  req.header.set("Authorization", `Bearer ${token}`);
  req.header.set("X-School-Id", school);
  return next(req);
};
const codeOf = (e: unknown) => ConnectError.from(e).metadata.get("Aula-Error-Code") ?? ConnectError.from(e).message;
async function login(email: string) {
  const res = await createClient(identity.IdentityService, createConnectTransport({ baseUrl, httpVersion: "1.1" })).login({ email, password: "demo1234" });
  return { token: res.session!.accessToken, school: res.session!.user!.school!.id };
}
function clients(who: { token: string; school: string }) {
  const t = createConnectTransport({ baseUrl, httpVersion: "1.1", interceptors: [withAuth(who.token, who.school)] });
  return { courses: createClient(courses.CoursesService, t), lessons: createClient(lessons.LessonsService, t), followup: createClient(followup.FollowupService, t) };
}
async function expectError(name: string, expected: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { check(name, codeOf(e) === expected, codeOf(e) === expected ? "" : `want ${expected}, got ${codeOf(e)}`); }
}

const anaLogin = await login("ana.morales@sanmarcos.edu.gt");
const ana = clients(anaLogin);
const carlos = clients(await login("carlos.ruiz@sanmarcos.edu.gt"));
const student = clients(await login("marta.ramirez@sanmarcos.edu.gt"));

const bd2 = (await ana.courses.listCourses({})).courses.find((c) => c.code === "CC3057");
if (!bd2) { console.log("FAIL  course CC3057 not found: is the database seeded?"); process.exit(1); }
const roster = (await ana.courses.listCourseStudents({ courseId: bd2.id })).students;
check("roster has 38 students", roster.length === 38, `got ${roster.length}`);

// Lesson 11 = the one with the confusing stretch (minutes 28-33), the doubt round, the quiz and the floor round.
const l11 = (await ana.courses.getCourse({ courseId: bd2.id })).course?.lessons.find((l) => l.number === 11);
if (!l11) { console.log("FAIL  lesson 11 not found in getCourse"); process.exit(1); }

const r = await ana.lessons.getLessonReview({ lessonId: l11.id });
check("review: course and number", r.courseName === bd2.name && r.number === 11, `${r.courseName} #${r.number}`);
check("review: duration 50 min", r.durationSeconds === 3000, `${r.durationSeconds}s`);
check("review: enrolled 38", r.enrolledCount === 38, `${r.enrolledCount}`);
check("review: one pulse point per minute", r.pulseSeries.length === 50, `${r.pulseSeries.length}`);
check("review: ok_percent within 0..100", r.pulseSeries.every((p) => p.okPercent >= 0 && p.okPercent <= 100));
const calmMax = Math.max(...r.pulseSeries.filter((p) => p.minute < 25).map((p) => p.lostCount));
const worst = r.pulseSeries.reduce((b, p) => (p.lostCount > b.lostCount ? p : b));
check("review: peak is the worst minute, inside the confusing stretch (28-34)", !!r.peak && r.peak.minute >= 28 && r.peak.minute <= 34 && r.peak.minute === worst.minute, `minute ${r.peak?.minute} lost ${r.peak?.lostCount}`);
check("review: peak lost count is large vs calm minutes", (r.peak?.lostCount ?? 0) > calmMax, `peak ${r.peak?.lostCount}, calm max ${calmMax}`);
check("review: peak top doubt = slide 22 / writes", r.peak?.slideNumber === 22 && r.peak.topDoubt.includes("escrituras"), `${r.peak?.slideNumber} ${r.peak?.topDoubt}`);
const doubtM = r.markers.find((m) => m.kind === lessons.MarkerKind.DOUBTS);
const quizM = r.markers.find((m) => m.kind === lessons.MarkerKind.QUIZ);
check("review: doubt marker at minute 31 with 31 doubts", doubtM?.minute === 31 && doubtM.label === "Dudas (31)", `${doubtM?.minute} ${doubtM?.label}`);
check("review: quiz marker at minute 37", quizM?.minute === 37, `${quizM?.minute} ${quizM?.label}`);
check("review: attendance 35 present + 1 to review + 2 absent", r.attendance?.present === 35 && r.attendance.needsReview === 1 && r.attendance.absent === 2, JSON.stringify(r.attendance));

// Participation of the whole roster: totals must match what the seed inserted.
const parts = await Promise.all(roster.map((s) => ana.followup.getStudentParticipation({ courseId: bd2.id, studentId: s.id })));
const sum = (f: (p: followup.GetStudentParticipationResponse) => number) => parts.reduce((s, p) => s + f(p), 0);
const p0 = parts[0];
check("participation: lessons_total >= 11, attended in 1..total",  p0.lessonsTotal >= 11 && p0.lessonsAttended > 0 && p0.lessonsAttended <= p0.lessonsTotal, `${p0.lessonsAttended}/${p0.lessonsTotal}`);
check("participation: per_lesson covers every ended lesson, in order", p0.perLesson.length === p0.lessonsTotal && p0.perLesson.every((l, i) => l.number === i + 1));
check("participation: quizzes_total >= 1 (the seed has one; other smokes add more)", p0.quizzesTotal >= 1, `${p0.quizzesTotal}`);
check("participation: quizzes answered over the roster >= 29", sum((p) => p.quizzesAnswered) >= 29, `${sum((p) => p.quizzesAnswered)}`);
check("participation: doubts over the roster = 31", sum((p) => p.doubtsSent) === 31, `${sum((p) => p.doubtsSent)}`);
check("participation: turns taken / correct over the roster = 2 / 1", sum((p) => p.turnsTaken) === 2 && sum((p) => p.turnsCorrect) === 1);

// Note write: replace, read back, restore.
const withNote = parts.find((p) => p.note.includes("trabajo"));
check("participation: the seeded private note is returned", !!withNote, withNote?.name);
if (withNote) {
  const original = withNote.note;
  await ana.followup.saveStudentNote({ courseId: bd2.id, studentId: withNote.studentId, body: "  Nota de prueba del smoke  " });
  const after = await ana.followup.getStudentParticipation({ courseId: bd2.id, studentId: withNote.studentId });
  check("note: saved (trimmed) and read back", after.note === "Nota de prueba del smoke");
  await ana.followup.saveStudentNote({ courseId: bd2.id, studentId: withNote.studentId, body: original });
  check("note: restored", (await ana.followup.getStudentParticipation({ courseId: bd2.id, studentId: withNote.studentId })).note === original);
  await expectError("note: blank body -> MALFORMED_ARGUMENT", "MALFORMED_ARGUMENT", () => ana.followup.saveStudentNote({ courseId: bd2.id, studentId: withNote.studentId, body: "   " }));
}

// Role scoping: a student never reaches teacher-only data (the note travels in participation).
await expectError("student: participation -> FORBIDDEN", "FORBIDDEN", () => student.followup.getStudentParticipation({ courseId: bd2.id, studentId: roster[0].id }));
await expectError("student: save note -> FORBIDDEN", "FORBIDDEN", () => student.followup.saveStudentNote({ courseId: bd2.id, studentId: roster[0].id, body: "x" }));
await expectError("student: review -> FORBIDDEN", "FORBIDDEN", () => student.lessons.getLessonReview({ lessonId: l11.id }));

// Tenant / ownership: another teacher gets "not found", the wrong school header is refused.
await expectError("other teacher: review -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => carlos.lessons.getLessonReview({ lessonId: l11.id }));
await expectError("other teacher: participation -> COURSE_NOT_FOUND", "COURSE_NOT_FOUND", () => carlos.followup.getStudentParticipation({ courseId: bd2.id, studentId: roster[0].id }));
await expectError("other teacher: save note -> COURSE_NOT_FOUND", "COURSE_NOT_FOUND", () => carlos.followup.saveStudentNote({ courseId: bd2.id, studentId: roster[0].id, body: "x" }));
await expectError("unknown lesson -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => ana.lessons.getLessonReview({ lessonId: NIL_UUID }));
await expectError("student outside the course -> COURSE_NOT_ENROLLED", "COURSE_NOT_ENROLLED", () => ana.followup.getStudentParticipation({ courseId: bd2.id, studentId: NIL_UUID }));
const wrongTenant = clients({ token: anaLogin.token, school: NIL_UUID });
await expectError("wrong X-School-Id -> TENANT_MISMATCH", "TENANT_MISMATCH", () => wrongTenant.lessons.getLessonReview({ lessonId: l11.id }));

console.log(failures ? `\n${failures} check(s) FAILED` : "\nall checks passed");
process.exit(failures ? 1 : 0);
