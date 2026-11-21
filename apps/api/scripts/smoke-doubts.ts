// End-to-end check of the doubt round lifecycle (open, extend, get, close, expiry) against a RUNNING api:
//   API_URL=http://localhost:3101 pnpm smoke:doubts
// Uses the generated TypeScript clients, so it also proves the contract and the server agree.
// Only touches "Bases de Datos 2" (CC3057). Always ends the lesson it opened (finally), so the course is free again.
// Out of scope here (later missions): SubmitDoubt, ListDoubts, the AI grouping.
import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, doubts, identity, lessons } from "@aula/rpc";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
const COURSE_CODE = "CC3057";
const NIL_UUID = "00000000-0000-4000-8000-000000000000";
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

/** One actor = the clients it can use, on one token. */
function clients(who: { token: string; school: string }) {
  const t = transport(who.token, who.school);
  return { doubts: createClient(doubts.DoubtsService, t), lessons: createClient(lessons.LessonsService, t), courses: createClient(courses.CoursesService, t) };
}

async function expectError(name: string, expected: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { check(name, codeOf(e) === expected, codeOf(e) === expected ? "" : `want ${expected}, got ${codeOf(e)}`); }
}
/**
 * A duration outside the proto's range must be REFUSED and change nothing (the later checks prove that). Known, pre-existing
 * issue: @connectrpc/validate 0.2.0 does not work with @bufbuild/protovalidate 0.4.0 (`violationsToProto is not a function`),
 * so every violation reaches the client as `internal` instead of invalid_argument. Fixing it means bumping dependencies, so
 * this only asserts the refusal; tighten it to Code.InvalidArgument once the pair is upgraded.
 */
async function expectRefused(name: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { check(name, true, `code ${Code[ConnectError.from(e).code]}`); }
}

const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
const secs = (t?: { seconds: bigint }) => Number(t?.seconds ?? 0n);
/** Waits (no calls to the API: only what the stream pushes) until `predicate` holds for a snapshot, up to `ms`. */
async function until(predicate: () => boolean, ms: number) {
  const end = Date.now() + ms;
  while (Date.now() < end) { if (predicate()) return true; await sleep(100); }
  return predicate();
}

const teacherLogin = await login("ana.morales@sanmarcos.edu.gt");
const otherTeacherLogin = await login("carlos.ruiz@sanmarcos.edu.gt"); // same school, owns none of Ana's courses
const studentLogin = await login("marta.ramirez@sanmarcos.edu.gt");
const T = clients(teacherLogin);
const otherT = clients(otherTeacherLogin);
const S = clients(studentLogin);

const list = await T.courses.listCourses({});
const bd2 = list.courses.find((c) => c.code === COURSE_CODE);
if (!bd2) { console.log("FAIL  course CC3057 not found: is the database seeded?"); process.exit(1); }

// A previous run that died can leave a lesson open on this course: close it so the run is repeatable.
const stale = (await T.courses.getCourse({ courseId: bd2.id })).course?.liveLesson;
if (stale) {
  if (stale.status === courses.LessonStatus.LOBBY) await T.lessons.closeRoom({ lessonId: stale.id });
  else await T.lessons.endLesson({ lessonId: stale.id });
  console.log(`note  closed a stale lesson left by an earlier run (${stale.id})`);
}

let lessonId: string | undefined;
type Snap = { revision: number; active?: string; activeClosesAt?: number; lastClosed?: string };
const snaps: Snap[] = [];
const last = () => snaps.at(-1);
const ac = new AbortController();
let watching: Promise<void> = Promise.resolve();

try {
  // --- the room, the lesson and the realtime stream ----------------------------------------------------------
  const room = (await T.lessons.openRoom({ courseId: bd2.id })).lesson!;
  lessonId = room.id;
  watching = (async () => {
    try {
      for await (const ev of T.lessons.watchLesson({ lessonId: room.id }, { signal: ac.signal })) {
        if (ev.event.case === "snapshot") {
          const s = ev.event.value;
          snaps.push({ revision: s.revision, active: s.activeDoubtRound?.id, activeClosesAt: secs(s.activeDoubtRound?.closesAt), lastClosed: s.lastClosedDoubtRound?.id });
        }
      }
    } catch (e) { if (!ac.signal.aborted) throw e; }
  })();
  await sleep(300);

  // --- who may open a round, and when ---------------------------------------------------------------------------
  await expectError("OpenDoubtRound while the lesson is in the lobby -> LESSON_NOT_LIVE", "LESSON_NOT_LIVE", () => T.doubts.openDoubtRound({ lessonId: room.id }));
  await T.lessons.startLesson({ lessonId: room.id });
  await expectError("a student cannot open a round -> FORBIDDEN", "FORBIDDEN", () => S.doubts.openDoubtRound({ lessonId: room.id }));
  await expectError("another teacher of the same school cannot open a round -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => otherT.doubts.openDoubtRound({ lessonId: room.id }));
  await expectError("unknown lesson -> LESSON_NOT_FOUND", "LESSON_NOT_FOUND", () => T.doubts.openDoubtRound({ lessonId: NIL_UUID }));
  await expectRefused("duration above 300 s is refused", () => T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: 301 }));
  check("no round exists yet", last()?.active === undefined && last()?.lastClosed === undefined);

  // --- open (default 30 s) ----------------------------------------------------------------------------------
  const rev0 = last()?.revision ?? 0;
  const r1 = (await T.doubts.openDoubtRound({ lessonId: room.id })).round!;
  check("OpenDoubtRound -> OPEN, 30 s by default", r1.status === doubts.DoubtRoundStatus.OPEN && secs(r1.closesAt) - secs(r1.openedAt) === 30, `${secs(r1.closesAt) - secs(r1.openedAt)} s`);
  check("the new round carries counts and a pending summary", r1.submittedCount === 0 && r1.enrolledCount > 0 && r1.summaryStatus === doubts.SummaryStatus.PENDING && r1.lessonId === room.id, `enrolled ${r1.enrolledCount}`);
  await expectError("a second OpenDoubtRound on the same lesson -> DOUBT_ROUND_ALREADY_OPEN", "DOUBT_ROUND_ALREADY_OPEN", () => T.doubts.openDoubtRound({ lessonId: room.id }));
  check("WatchLesson shows the round as active (revision moved)", await until(() => last()?.active === r1.id && (last()?.revision ?? 0) > rev0, 3000), `revision ${rev0} -> ${last()?.revision}`);

  // --- get, per role ----------------------------------------------------------------------------------------
  const asTeacher = (await T.doubts.getDoubtRound({ roundId: r1.id })).round!;
  check("GetDoubtRound (teacher) -> same round, no student-only fields", asTeacher.id === r1.id && asTeacher.status === doubts.DoubtRoundStatus.OPEN && asTeacher.myDoubt === undefined && asTeacher.myGroup === undefined && asTeacher.groups.length === 0);
  const asStudent = (await S.doubts.getDoubtRound({ roundId: r1.id })).round!;
  check("GetDoubtRound (student) -> round with no teacher-only groups", asStudent.id === r1.id && asStudent.groups.length === 0 && asStudent.myDoubt === undefined && secs(asStudent.openedAt) === secs(r1.openedAt));
  await expectError("another teacher cannot read the round -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => otherT.doubts.getDoubtRound({ roundId: r1.id }));
  await expectError("unknown round -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => T.doubts.getDoubtRound({ roundId: NIL_UUID }));

  // --- extend ---------------------------------------------------------------------------------------------
  await expectError("a student cannot extend -> FORBIDDEN", "FORBIDDEN", () => S.doubts.extendDoubtRound({ roundId: r1.id }));
  await expectError("another teacher cannot extend -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => otherT.doubts.extendDoubtRound({ roundId: r1.id }));
  await expectRefused("extending by more than 120 s is refused", () => T.doubts.extendDoubtRound({ roundId: r1.id, seconds: 121 }));
  const e1 = (await T.doubts.extendDoubtRound({ roundId: r1.id })).round!;
  check("ExtendDoubtRound -> +30 s by default, opened_at unchanged", secs(e1.closesAt) - secs(r1.closesAt) === 30 && secs(e1.openedAt) === secs(r1.openedAt), `${secs(e1.closesAt) - secs(r1.closesAt)} s`);
  const e2 = (await T.doubts.extendDoubtRound({ roundId: r1.id, seconds: 45 })).round!;
  check("ExtendDoubtRound(45) -> +45 s", secs(e2.closesAt) - secs(e1.closesAt) === 45);
  check("the extension is persisted (GetDoubtRound) and reaches the stream", secs((await T.doubts.getDoubtRound({ roundId: r1.id })).round!.closesAt) === secs(e2.closesAt)
    && await until(() => last()?.activeClosesAt === secs(e2.closesAt), 3000));

  // --- close ----------------------------------------------------------------------------------------------
  const c1 = (await T.doubts.closeDoubtRound({ roundId: r1.id })).round!;
  check("CloseDoubtRound -> CLOSED, summary still PENDING (grouping is a later step)", c1.status === doubts.DoubtRoundStatus.CLOSED && c1.summaryStatus === doubts.SummaryStatus.PENDING);
  await expectError("closing a closed round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.closeDoubtRound({ roundId: r1.id }));
  await expectError("extending a closed round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.extendDoubtRound({ roundId: r1.id }));
  check("a closed round stays readable by the student", (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.status === doubts.DoubtRoundStatus.CLOSED);
  check("WatchLesson: no active round, last closed = round 1", await until(() => last()?.active === undefined && last()?.lastClosed === r1.id, 3000));

  // --- expiry: the server closes the round at closes_at, nobody pressed "Cerrar ahora" -----------------------
  const r2 = (await T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: 2 })).round!;
  check("a new round opens after the previous one closed (2 s)", r2.status === doubts.DoubtRoundStatus.OPEN && r2.id !== r1.id && secs(r2.closesAt) - secs(r2.openedAt) <= 3);
  check("WatchLesson: round 2 active, round 1 still the last closed", await until(() => last()?.active === r2.id && last()?.lastClosed === r1.id, 3000));
  // No API call from here until the stream says so: only the server's own timer can produce the change.
  check("WatchLesson pushes the expiry by itself: round 2 no longer active, now the last closed", await until(() => last()?.active === undefined && last()?.lastClosed === r2.id, 5000));
  const expired = (await T.doubts.getDoubtRound({ roundId: r2.id })).round!;
  check("GetDoubtRound -> CLOSED after expiry", expired.status === doubts.DoubtRoundStatus.CLOSED);
  await expectError("extending an expired round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.extendDoubtRound({ roundId: r2.id }));
  await expectError("closing an expired round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.closeDoubtRound({ roundId: r2.id }));

  // --- an extension re-arms the timer: the round must NOT close at its first deadline ---------------------------
  const r3 = (await T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: 2 })).round!;
  await T.doubts.extendDoubtRound({ roundId: r3.id, seconds: 3 });
  await sleep(2800); // past the first deadline, before the extended one
  check("after the first deadline the extended round is still OPEN", (await T.doubts.getDoubtRound({ roundId: r3.id })).round!.status === doubts.DoubtRoundStatus.OPEN && last()?.active === r3.id);
  check("the extended round closes at its new deadline", await until(() => last()?.active === undefined && last()?.lastClosed === r3.id, 5000));

  // --- the stream, as a whole ---------------------------------------------------------------------------------
  const revs = snaps.map((s) => s.revision);
  check("WatchLesson revision never goes back", revs.every((r, i) => i === 0 || r >= revs[i - 1]) && revs.at(-1)! > revs[0], `${revs[0]} -> ${revs.at(-1)}`);
} catch (e) {
  check("unexpected error during the doubt checks", false, e instanceof Error ? e.message : String(e));
} finally {
  if (lessonId) {
    try {
      const ended = (await T.lessons.endLesson({ lessonId })).lesson;
      check("endLesson (cleanup) -> ENDED", ended?.status === courses.LessonStatus.ENDED);
    } catch (e) {
      check("endLesson (cleanup)", false, codeOf(e));
    }
  }
  ac.abort();
  await watching.catch(() => {});
}

console.log(failures ? `\n${failures} check(s) FAILED` : "\nall checks passed");
process.exit(failures ? 1 : 0);
