// End-to-end check of QR attendance (open, codes, CheckIn, review, resolve, close, realtime) against a RUNNING api:
//   API_URL=http://localhost:3101 pnpm smoke:attendance
// Uses the generated TypeScript clients, so it also proves the contract and the server agree.
// Only touches "Sistemas Operativos" (CC3064): 31 of the 38 students are enrolled, so it can also test a student who is not.
// Always ends the lesson it opened (finally), so the course is free again. It does not wipe or reseed anything.
import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { attendance, courses, identity, lessons } from "@aula/rpc";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
const COURSE_CODE = "CC3064";
const NIL_UUID = "00000000-0000-4000-8000-000000000000";
// Where the "teacher" stands. One degree of latitude is ~111 km, so 0.002 is ~222 m: well outside the 50 m radius.
const ROOM = { lat: 14.6349, lng: -90.5069 };
const FAR = { lat: ROOM.lat + 0.002, lng: ROOM.lng };
let failures = 0;
const check = (name: string, ok: boolean, extra = "") => { console.log(`${ok ? "PASS" : "FAIL"}  ${name}${extra ? "  " + extra : ""}`); if (!ok) failures++; };

const withAuth = (token: string, school: string): Interceptor => (next) => async (req) => {
  req.header.set("Authorization", `Bearer ${token}`);
  req.header.set("X-School-Id", school);
  return next(req);
};
const transport = (token: string, school: string) => createConnectTransport({ baseUrl, httpVersion: "1.1", interceptors: [withAuth(token, school)] });
const codeOf = (e: unknown) => ConnectError.from(e).metadata.get("Aula-Error-Code") ?? ConnectError.from(e).message;

async function login(email: string) {
  const res = await createClient(identity.IdentityService, createConnectTransport({ baseUrl, httpVersion: "1.1" })).login({ email, password: "demo1234" });
  const u = res.session!.user!;
  return { token: res.session!.accessToken, school: u.school!.id, id: u.id, name: u.name };
}
function clients(who: { token: string; school: string }) {
  const t = transport(who.token, who.school);
  return { attendance: createClient(attendance.AttendanceService, t), lessons: createClient(lessons.LessonsService, t), courses: createClient(courses.CoursesService, t) };
}
async function expectError(name: string, expected: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { check(name, codeOf(e) === expected, codeOf(e) === expected ? "" : `want ${expected}, got ${codeOf(e)}`); }
}
async function expectInvalid(name: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { const err = ConnectError.from(e); check(name, err.code === Code.InvalidArgument, `got ${Code[err.code]}: ${err.rawMessage}`); }
}
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const secs = (t?: { seconds: bigint }) => Number(t?.seconds ?? 0n);
async function until(predicate: () => boolean, ms: number) {
  const end = Date.now() + ms;
  while (Date.now() < end) { if (predicate()) return true; await sleep(100); }
  return predicate();
}

const teacherLogin = await login("ana.morales@sanmarcos.edu.gt");
const otherTeacherLogin = await login("carlos.ruiz@sanmarcos.edu.gt"); // same school, owns none of Ana's courses
const logins = {
  s1: await login("marta.ramirez@sanmarcos.edu.gt"),       // inside the radius (QR)
  s2: await login("ana.lucia.mendez@sanmarcos.edu.gt"),    // typed code, no location, then rescans well
  s3: await login("alejandra.mejia@sanmarcos.edu.gt"),     // too far, then accepted by the teacher
  s4: await login("pablo.herrera@sanmarcos.edu.gt"),       // low accuracy, then rejected, then tries to rescan
  s5: await login("sofia.pineda@sanmarcos.edu.gt"),      // no location, rejected AFTER the round closes
  n: await login("cecilia.marroquin@sanmarcos.edu.gt"),    // NOT enrolled in CC3064
};
const T = clients(teacherLogin), otherT = clients(otherTeacherLogin);
const S = Object.fromEntries(Object.entries(logins).map(([k, v]) => [k, clients(v)])) as Record<keyof typeof logins, ReturnType<typeof clients>>;

const list = await T.courses.listCourses({});
const course = list.courses.find((c) => c.code === COURSE_CODE);
if (!course) { console.log(`FAIL  course ${COURSE_CODE} not found: is the database seeded?`); process.exit(1); }
const stale = (await T.courses.getCourse({ courseId: course.id })).course?.liveLesson;
if (stale) {
  if (stale.status === courses.LessonStatus.LOBBY) await T.lessons.closeRoom({ lessonId: stale.id });
  else await T.lessons.endLesson({ lessonId: stale.id });
  console.log(`note  closed a stale lesson left by an earlier run (${stale.id})`);
}

type Snap = { revision: number; id?: string; present?: number; review?: number };
const snaps: Snap[] = [];
const last = () => snaps.at(-1);
const ac = new AbortController();
let watching: Promise<void> = Promise.resolve();
let lessonId: string | undefined;
const scan = (who: keyof typeof logins, roundId: string, code: string, loc?: { lat: number; lng: number; accuracyM: number }) =>
  S[who].attendance.checkIn({ roundId, code, hasLocation: !!loc, lat: loc?.lat ?? 0, lng: loc?.lng ?? 0, accuracyM: loc?.accuracyM ?? 0 });
const here = { ...ROOM, accuracyM: 10 };

try {
  const room = (await T.lessons.openRoom({ courseId: course.id })).lesson!;
  lessonId = room.id;
  watching = (async () => {
    try {
      for await (const ev of T.lessons.watchLesson({ lessonId: room.id }, { signal: ac.signal })) {
        if (ev.event.case === "snapshot") {
          const s = ev.event.value;
          snaps.push({ revision: s.revision, id: s.activeAttendanceRound?.id, present: s.activeAttendanceRound?.presentCount, review: s.activeAttendanceRound?.needsReviewCount });
        }
      }
    } catch (e) { if (!ac.signal.aborted) throw e; }
  })();
  await sleep(300);

  // --- who may open, and when -------------------------------------------------------------------------------
  await expectError("OpenAttendanceRound while the lesson is in the lobby -> LESSON_NOT_LIVE", "LESSON_NOT_LIVE", () => T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: true, ...ROOM }));
  await T.lessons.startLesson({ lessonId: room.id });
  await expectError("a student cannot open attendance -> FORBIDDEN", "FORBIDDEN", () => S.s1.attendance.openAttendanceRound({ lessonId: room.id }));
  await expectError("another teacher cannot open it -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => otherT.attendance.openAttendanceRound({ lessonId: room.id }));
  await expectError("unknown lesson -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => T.attendance.openAttendanceRound({ lessonId: NIL_UUID }));
  await expectInvalid("lesson_id that is not a uuid -> invalid_argument", () => T.attendance.openAttendanceRound({ lessonId: "nope" }));
  await expectError("a latitude off the globe -> MALFORMED_ARGUMENT", "MALFORMED_ARGUMENT", () => T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: true, lat: 95, lng: 0 }));
  check("the refused opens created nothing", last()?.id === undefined);

  // --- open with location ---------------------------------------------------------------------------------------
  const rev0 = last()?.revision ?? 0;
  const r1 = (await T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: true, ...ROOM })).round!;
  check("OpenAttendanceRound -> OPEN with empty counters", r1.status === attendance.AttendanceRoundStatus.OPEN && r1.presentCount === 0 && r1.inRoomCount === 0 && r1.review.length === 0 && r1.enrolledCount > 0 && r1.lessonId === room.id, `enrolled ${r1.enrolledCount}`);
  await expectError("a second round on the same lesson -> ATTENDANCE_ROUND_ALREADY_OPEN", "ATTENDANCE_ROUND_ALREADY_OPEN", () => T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: true, ...ROOM }));
  check("WatchLesson shows the round (revision moved) without any manual refresh", await until(() => last()?.id === r1.id && last()!.revision > rev0, 3000));

  // --- the codes -------------------------------------------------------------------------------------------------
  await expectError("a student cannot read the codes -> FORBIDDEN", "FORBIDDEN", () => S.s1.attendance.getAttendanceCodes({ roundId: r1.id }));
  await expectError("another teacher cannot read the codes -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => otherT.attendance.getAttendanceCodes({ roundId: r1.id }));
  await expectError("unknown round -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => T.attendance.getAttendanceCodes({ roundId: NIL_UUID }));
  const c1 = await T.attendance.getAttendanceCodes({ roundId: r1.id });
  const nowS = Date.now() / 1000;
  check("codes: QR is 16 hex chars, typable is 6 chars", /^[0-9A-F]{16}$/.test(c1.qrCode) && /^[A-Z2-9]{6}$/.test(c1.manualCode), `${c1.qrCode} / ${c1.manualCode}`);
  check("codes: the QR lives <= 8 s, the typable one <= 30 s", secs(c1.qrExpiresAt) > nowS - 1 && secs(c1.qrExpiresAt) - nowS <= 8.5 && secs(c1.manualExpiresAt) - nowS <= 30.5);

  // --- check in: refusals ----------------------------------------------------------------------------------------
  await expectError("a student NOT enrolled cannot check in -> COURSE_NOT_ENROLLED", "COURSE_NOT_ENROLLED", () => scan("n", r1.id, c1.qrCode, here));
  await expectError("a wrong code -> ATTENDANCE_QR_INVALID", "ATTENDANCE_QR_INVALID", () => scan("s1", r1.id, "0000000000000000", here));
  await expectError("unknown round -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => scan("s1", NIL_UUID, c1.qrCode, here));
  await expectError("a teacher cannot check in -> FORBIDDEN", "FORBIDDEN", () => T.attendance.checkIn({ roundId: r1.id, code: c1.qrCode }));
  await expectError("coordinates off the globe -> MALFORMED_ARGUMENT", "MALFORMED_ARGUMENT", () => scan("s1", r1.id, c1.qrCode, { lat: 0, lng: 400, accuracyM: 5 }));
  await expectError("the wrong X-School-Id is refused -> TENANT_MISMATCH", "TENANT_MISMATCH", () =>
    createClient(attendance.AttendanceService, transport(logins.s1.token, NIL_UUID)).checkIn({ roundId: r1.id, code: c1.qrCode }));
  check("none of the refused scans stored anything", (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!.presentCount === 0 && (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!.review.length === 0);

  // --- an EXPIRED code: wait for the QR step to end, then use the old QR ---------------------------------------------
  const wait = secs(c1.qrExpiresAt) * 1000 - Date.now() + 300;
  if (wait > 0) await sleep(wait);
  await expectError("the QR of the previous step has expired -> ATTENDANCE_QR_INVALID", "ATTENDANCE_QR_INVALID", () => scan("s1", r1.id, c1.qrCode, here));
  const c2 = await T.attendance.getAttendanceCodes({ roundId: r1.id });
  check("the QR rotated", c2.qrCode !== c1.qrCode);

  // --- check in: the outcomes ----------------------------------------------------------------------------------------
  const rev1 = last()!.revision;
  const a1 = await scan("s1", r1.id, c2.qrCode, here);
  check("inside the radius -> PRESENT", a1.status === attendance.AttendanceStatus.PRESENT && a1.reason === attendance.ReviewReason.UNSPECIFIED);
  check("WatchLesson: the teacher's present counter moves (no refetch)", await until(() => last()?.id === r1.id && last()?.present === 1 && last()!.revision > rev1, 3000), `present ${last()?.present}`);
  const a1b = await scan("s1", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode, here);
  check("a duplicate scan answers PRESENT again", a1b.status === attendance.AttendanceStatus.PRESENT);
  const mid = (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!;
  check("...and there is still ONE record (present 1, nothing to review)", mid.presentCount === 1 && mid.review.length === 0);

  const manual = (await T.attendance.getAttendanceCodes({ roundId: r1.id })).manualCode;
  const a2 = await scan("s2", r1.id, manual.toLowerCase());
  check("the typable code (any case) with NO location -> NEEDS_REVIEW / NO_LOCATION", a2.status === attendance.AttendanceStatus.NEEDS_REVIEW && a2.reason === attendance.ReviewReason.NO_LOCATION);
  const a3 = await scan("s3", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode, { ...FAR, accuracyM: 8 });
  check("~220 m away -> NEEDS_REVIEW / TOO_FAR", a3.status === attendance.AttendanceStatus.NEEDS_REVIEW && a3.reason === attendance.ReviewReason.TOO_FAR);
  const a4 = await scan("s4", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode, { ...ROOM, accuracyM: 300 });
  check("a location too imprecise -> NEEDS_REVIEW / LOW_ACCURACY", a4.status === attendance.AttendanceStatus.NEEDS_REVIEW && a4.reason === attendance.ReviewReason.LOW_ACCURACY);
  const a5 = await scan("s5", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode);
  check("another scan with no location -> NEEDS_REVIEW", a5.status === attendance.AttendanceStatus.NEEDS_REVIEW);

  check("WatchLesson: present 1, to review 4 (stream only)", await until(() => last()?.present === 1 && last()?.review === 4, 3000), `present ${last()?.present}, review ${last()?.review}`);
  const st = (await S.s1.lessons.getLessonSnapshot({ lessonId: room.id })).snapshot!;
  check("a STUDENT's snapshot has the round id but not the teacher's counters", st.activeAttendanceRound?.id === r1.id && st.activeAttendanceRound.presentCount === 0 && st.activeAttendanceRound.needsReviewCount === 0);

  // --- the teacher's view ------------------------------------------------------------------------------------------
  await expectError("a student cannot read the round -> FORBIDDEN", "FORBIDDEN", () => S.s1.attendance.getAttendanceRound({ roundId: r1.id }));
  await expectError("another teacher cannot read it -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => otherT.attendance.getAttendanceRound({ roundId: r1.id }));
  const v1 = (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!;
  const item = (id: string) => v1.review.find((r) => r.studentId === id);
  check("GetAttendanceRound: present 1, in the room 1, 4 to review", v1.presentCount === 1 && v1.inRoomCount === 1 && v1.review.length === 4);
  check("review items carry name, reason and the SERVER's distance (~222 m for the far one)",
    item(logins.s3.id)?.reason === attendance.ReviewReason.TOO_FAR && Math.abs((item(logins.s3.id)?.distanceM ?? 0) - 222) < 10 && item(logins.s3.id)?.name === logins.s3.name
    && item(logins.s2.id)?.reason === attendance.ReviewReason.NO_LOCATION && item(logins.s2.id)?.distanceM === 0, `${item(logins.s3.id)?.distanceM} m`);

  // --- a rescan ("Escanear de nuevo") ------------------------------------------------------------------------------------
  const re = await scan("s2", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode, here);
  check("rescanning inside the radius turns the pending record PRESENT", re.status === attendance.AttendanceStatus.PRESENT);
  const v2 = (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!;
  check("...present 2, in the room 2, 3 left to review (still one row per student)", v2.presentCount === 2 && v2.inRoomCount === 2 && v2.review.length === 3);

  // --- the teacher decides ----------------------------------------------------------------------------------------------
  await expectError("a student cannot resolve -> FORBIDDEN", "FORBIDDEN", () => S.s1.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.ACCEPT }));
  await expectError("another teacher cannot resolve -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => otherT.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.ACCEPT }));
  await expectInvalid("an unspecified decision -> invalid_argument", () => T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.UNSPECIFIED }));
  await expectError("a student with no record -> NOT_FOUND", "NOT_FOUND", () => T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.n.id, decision: attendance.Decision.ACCEPT }));
  await expectError("a record accepted by location is not pending -> ATTENDANCE_ALREADY_RESOLVED", "ATTENDANCE_ALREADY_RESOLVED", () => T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s1.id, decision: attendance.Decision.REJECT }));
  const d1 = (await T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.ACCEPT })).round!;
  check("Aceptar -> that student is PRESENT (present 3) and stays listed with the outcome", d1.presentCount === 3 && d1.inRoomCount === 2
    && d1.review.find((r) => r.studentId === logins.s3.id)?.status === attendance.AttendanceStatus.PRESENT);
  check("WatchLesson: counters follow the teacher's decision (present 3, review 2)", await until(() => last()?.present === 3 && last()?.review === 2, 3000), `present ${last()?.present}, review ${last()?.review}`);
  const d1b = (await T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.ACCEPT })).round!;
  check("repeating the same decision (a double click) is a no-op", d1b.presentCount === 3);
  await expectError("a DIFFERENT decision afterwards -> ATTENDANCE_ALREADY_RESOLVED", "ATTENDANCE_ALREADY_RESOLVED", () => T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s3.id, decision: attendance.Decision.REJECT }));
  const d2 = (await T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s4.id, decision: attendance.Decision.REJECT })).round!;
  check("Ausente -> ABSENT, not counted as present", d2.presentCount === 3 && d2.review.find((r) => r.studentId === logins.s4.id)?.status === attendance.AttendanceStatus.ABSENT);
  await expectError("a student the teacher rejected cannot rescan -> ATTENDANCE_ALREADY_RESOLVED", "ATTENDANCE_ALREADY_RESOLVED",
    async () => scan("s4", r1.id, (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode, here));

  // --- close ---------------------------------------------------------------------------------------------------------
  await expectError("a student cannot close -> FORBIDDEN", "FORBIDDEN", () => S.s1.attendance.closeAttendanceRound({ roundId: r1.id }));
  await expectError("another teacher cannot close -> ATTENDANCE_ROUND_NOT_FOUND", "ATTENDANCE_ROUND_NOT_FOUND", () => otherT.attendance.closeAttendanceRound({ roundId: r1.id }));
  const staleCode = (await T.attendance.getAttendanceCodes({ roundId: r1.id })).qrCode;
  const closed = (await T.attendance.closeAttendanceRound({ roundId: r1.id })).round!;
  check("CloseAttendanceRound -> CLOSED, the counters are kept", closed.status === attendance.AttendanceRoundStatus.CLOSED && closed.presentCount === 3);
  check("WatchLesson: the active round disappears", await until(() => last()?.id === undefined, 3000));
  await expectError("closing twice -> ATTENDANCE_ROUND_CLOSED", "ATTENDANCE_ROUND_CLOSED", () => T.attendance.closeAttendanceRound({ roundId: r1.id }));
  await expectError("a scan after the close -> ATTENDANCE_ROUND_CLOSED", "ATTENDANCE_ROUND_CLOSED", () => scan("s1", r1.id, staleCode, here));
  await expectError("the codes of a closed round -> ATTENDANCE_ROUND_CLOSED", "ATTENDANCE_ROUND_CLOSED", () => T.attendance.getAttendanceCodes({ roundId: r1.id }));
  const afterClose = (await T.attendance.resolveAttendance({ roundId: r1.id, studentId: logins.s5.id, decision: attendance.Decision.REJECT })).round!;
  check("the teacher still reviews AFTER closing (Docente-AsistCerrada): s5 -> ABSENT", afterClose.review.find((r) => r.studentId === logins.s5.id)?.status === attendance.AttendanceStatus.ABSENT && afterClose.status === attendance.AttendanceRoundStatus.CLOSED);
  check("GetAttendanceRound keeps working on the closed round", (await T.attendance.getAttendanceRound({ roundId: r1.id })).round!.presentCount === 3);

  // --- a round opened WITHOUT the teacher's location (migration 004) ----------------------------------------------------------
  const r2 = (await T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: false })).round!;
  check("OpenAttendanceRound without location -> OPEN (a new round once the other is closed)", r2.status === attendance.AttendanceRoundStatus.OPEN && r2.id !== r1.id);
  const n1 = await scan("s1", r2.id, (await T.attendance.getAttendanceCodes({ roundId: r2.id })).qrCode, here);
  check("there is no radius to compare with: every scan -> NEEDS_REVIEW / NO_LOCATION", n1.status === attendance.AttendanceStatus.NEEDS_REVIEW && n1.reason === attendance.ReviewReason.NO_LOCATION);
  check("...and the teacher sees it to review, with unknown distance", (await T.attendance.getAttendanceRound({ roundId: r2.id })).round!.review.find((r) => r.studentId === logins.s1.id)?.distanceM === 0);
  await T.attendance.closeAttendanceRound({ roundId: r2.id });

  // --- ending the lesson closes an open round -----------------------------------------------------------------------------------
  const r3 = (await T.attendance.openAttendanceRound({ lessonId: room.id, hasLocation: true, ...ROOM })).round!;
  await T.lessons.endLesson({ lessonId: room.id });
  check("ending the lesson closes its open attendance round", (await T.attendance.getAttendanceRound({ roundId: r3.id })).round!.status === attendance.AttendanceRoundStatus.CLOSED);
  lessonId = undefined;

  const revs = snaps.map((s) => s.revision);
  check("WatchLesson revision never goes back", revs.every((r, i) => i === 0 || r >= revs[i - 1]) && revs.at(-1)! > revs[0], `${revs[0]} -> ${revs.at(-1)}`);
} catch (e) {
  check("unexpected error during the attendance checks", false, e instanceof Error ? e.message : codeOf(e));
} finally {
  if (lessonId) {
    try { check("endLesson (cleanup) -> ENDED", (await T.lessons.endLesson({ lessonId })).lesson?.status === courses.LessonStatus.ENDED); }
    catch (e) { check("endLesson (cleanup)", false, codeOf(e)); }
  }
  ac.abort();
  await watching.catch(() => {});
}

console.log(failures ? `\n${failures} check(s) FAILED` : "\nall checks passed");
process.exit(failures ? 1 : 0);
