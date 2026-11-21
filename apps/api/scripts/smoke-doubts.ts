// End-to-end check of the doubt rounds (open, extend, get, close, expiry, SubmitDoubt, ListDoubts) against a RUNNING api:
//   API_URL=http://localhost:3101 pnpm smoke:doubts
// Uses the generated TypeScript clients, so it also proves the contract and the server agree.
// Only touches "Sistemas Operativos" (CC3064): 31 of the 38 students are enrolled, so it can also test a student who is not.
// Always ends the lesson it opened (finally), so the course is free again. The AI grouping is a later mission.
import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, doubts, identity, lessons } from "@aula/rpc";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
const COURSE_CODE = "CC3064";
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
/** A request that breaks a rule declared in the proto is refused BEFORE the handler runs: invalid_argument, with a message. */
async function expectInvalid(name: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) {
    const err = ConnectError.from(e);
    check(name, err.code === Code.InvalidArgument && err.rawMessage.length > 0, `got ${Code[err.code]}: ${err.rawMessage}`);
  }
}
/** The opposite: a VALID request gets past validation (whatever the handler then answers, it is not invalid_argument). */
async function expectPastValidation(name: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, true); }
  catch (e) { const code = ConnectError.from(e).code; check(name, code !== Code.InvalidArgument, `answered ${Code[code]}`); }
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
const s1Login = await login("marta.ramirez@sanmarcos.edu.gt");           // enrolled
const s2Login = await login("ana.lucia.mendez@sanmarcos.edu.gt");       // enrolled
const s3Login = await login("alejandra.mejia@sanmarcos.edu.gt");        // enrolled (first submission raced below)
const nLogin = await login("cecilia.marroquin@sanmarcos.edu.gt");       // NOT enrolled in CC3064
const T = clients(teacherLogin);
const anonymous = createClient(doubts.DoubtsService, createConnectTransport({ baseUrl, httpVersion: "1.1" })); // no token, no tenant header
const otherT = clients(otherTeacherLogin);
const S = clients(s1Login), S2 = clients(s2Login), S3 = clients(s3Login), N = clients(nLogin);

const list = await T.courses.listCourses({});
const course = list.courses.find((c) => c.code === COURSE_CODE);
if (!course) { console.log(`FAIL  course ${COURSE_CODE} not found: is the database seeded?`); process.exit(1); }

// A previous run that died can leave a lesson open on this course: close it so the run is repeatable.
const stale = (await T.courses.getCourse({ courseId: course.id })).course?.liveLesson;
if (stale) {
  if (stale.status === courses.LessonStatus.LOBBY) await T.lessons.closeRoom({ lessonId: stale.id });
  else await T.lessons.endLesson({ lessonId: stale.id });
  console.log(`note  closed a stale lesson left by an earlier run (${stale.id})`);
}

let lessonId: string | undefined;
type Snap = { revision: number; active?: string; activeClosesAt?: number; submitted?: number; lastClosed?: string; lastClosedSubmitted?: number };
const snaps: Snap[] = [];
const last = () => snaps.at(-1);
let lastRaw: unknown; // the last snapshot message as the server sent it (for the privacy check)
const json = (v: unknown) => JSON.stringify(v, (_k, x) => (typeof x === "bigint" ? x.toString() : x));
/** The first message of a brand new WatchLesson stream: what a client sees after a reconnection. */
async function firstSnapshot(c: ReturnType<typeof clients>, lessonId: string) {
  const stop = new AbortController();
  try {
    for await (const ev of c.lessons.watchLesson({ lessonId }, { signal: stop.signal })) if (ev.event.case === "snapshot") return ev.event.value;
  } finally { stop.abort(); }
  throw new Error("the stream ended without a snapshot");
}
const ac = new AbortController();
let watching: Promise<void> = Promise.resolve();

try {
  // --- the room, the lesson and the realtime stream ----------------------------------------------------------
  const room = (await T.lessons.openRoom({ courseId: course.id })).lesson!;
  lessonId = room.id;
  watching = (async () => {
    try {
      for await (const ev of T.lessons.watchLesson({ lessonId: room.id }, { signal: ac.signal })) {
        if (ev.event.case === "snapshot") {
          const s = ev.event.value;
          lastRaw = s;
          snaps.push({ revision: s.revision, active: s.activeDoubtRound?.id, activeClosesAt: secs(s.activeDoubtRound?.closesAt), submitted: s.activeDoubtRound?.submittedCount,
            lastClosed: s.lastClosedDoubtRound?.id, lastClosedSubmitted: s.lastClosedDoubtRound?.submittedCount });
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
  await expectInvalid("OpenDoubtRound: duration above 300 s -> invalid_argument", () => T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: 301 }));
  check("no round exists yet (the invalid request created nothing)", last()?.active === undefined && last()?.lastClosed === undefined);
  await expectInvalid("OpenDoubtRound: negative duration -> invalid_argument", () => T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: -1 }));
  await expectInvalid("OpenDoubtRound: lesson_id that is not a uuid -> invalid_argument", () => T.doubts.openDoubtRound({ lessonId: "not-a-uuid" }));
  // Validation refuses bad input before any handler.
  await expectInvalid("SubmitDoubt: empty text -> invalid_argument (before the handler)", () => S.doubts.submitDoubt({ roundId: NIL_UUID, text: "" }));
  await expectInvalid("SubmitDoubt: text over 500 characters -> invalid_argument", () => S.doubts.submitDoubt({ roundId: NIL_UUID, text: "x".repeat(501) }));
  await expectPastValidation("SubmitDoubt: valid text passes validation and reaches the handler (here: no such round)", () => S.doubts.submitDoubt({ roundId: NIL_UUID, text: "una duda" }));
  await expectError("an unauthenticated VALID request keeps UNAUTHENTICATED", "UNAUTHENTICATED", () => anonymous.getDoubtRound({ roundId: NIL_UUID }));

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
  await expectInvalid("ExtendDoubtRound: more than 120 s -> invalid_argument", () => T.doubts.extendDoubtRound({ roundId: r1.id, seconds: 121 }));
  await expectInvalid("ExtendDoubtRound: negative seconds -> invalid_argument", () => T.doubts.extendDoubtRound({ roundId: r1.id, seconds: -5 }));
  check("the invalid extensions changed nothing", secs((await T.doubts.getDoubtRound({ roundId: r1.id })).round!.closesAt) === secs(r1.closesAt));
  const e1 = (await T.doubts.extendDoubtRound({ roundId: r1.id })).round!;
  check("ExtendDoubtRound -> +30 s by default, opened_at unchanged", secs(e1.closesAt) - secs(r1.closesAt) === 30 && secs(e1.openedAt) === secs(r1.openedAt), `${secs(e1.closesAt) - secs(r1.closesAt)} s`);
  const e2 = (await T.doubts.extendDoubtRound({ roundId: r1.id, seconds: 45 })).round!;
  check("ExtendDoubtRound(45) -> +45 s", secs(e2.closesAt) - secs(e1.closesAt) === 45);
  check("the extension is persisted (GetDoubtRound) and reaches the stream", secs((await T.doubts.getDoubtRound({ roundId: r1.id })).round!.closesAt) === secs(e2.closesAt)
    && await until(() => last()?.activeClosesAt === secs(e2.closesAt), 3000));

  // --- SubmitDoubt and ListDoubts: roles, enrolment, tenant -------------------------------------------------
  const TEXT1 = "¿Por qué un índice puede hacer más lentas las escrituras?";
  const TEXT2 = "¿Y cuándo conviene NO indexar una columna?";
  const TEXT_B = "no entiendo el Seq Scan";
  await expectError("a teacher cannot use SubmitDoubt -> FORBIDDEN", "FORBIDDEN", () => T.doubts.submitDoubt({ roundId: r1.id, text: "x" }));
  await expectError("a student cannot use ListDoubts -> FORBIDDEN", "FORBIDDEN", () => S.doubts.listDoubts({ roundId: r1.id }));
  await expectError("another teacher cannot list the doubts -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => otherT.doubts.listDoubts({ roundId: r1.id }));
  await expectError("ListDoubts on an unknown round -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => T.doubts.listDoubts({ roundId: NIL_UUID }));
  await expectError("a student NOT enrolled in the course cannot submit -> COURSE_NOT_ENROLLED", "COURSE_NOT_ENROLLED", () => N.doubts.submitDoubt({ roundId: r1.id, text: "hola" }));
  await expectError("SubmitDoubt on an unknown round -> DOUBT_ROUND_NOT_FOUND", "DOUBT_ROUND_NOT_FOUND", () => S.doubts.submitDoubt({ roundId: NIL_UUID, text: "hola" }));
  await expectError("whitespace-only text is refused by the entity -> MALFORMED_ARGUMENT", "MALFORMED_ARGUMENT", () => S.doubts.submitDoubt({ roundId: r1.id, text: "     " }));
  await expectError("the wrong X-School-Id is refused -> TENANT_MISMATCH", "TENANT_MISMATCH", () => createClient(doubts.DoubtsService, transport(s1Login.token, NIL_UUID)).submitDoubt({ roundId: r1.id, text: "hola" }));
  check("nothing was stored by the refused attempts", (await T.doubts.listDoubts({ roundId: r1.id })).doubts.length === 0 && (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 0);

  // --- first submission, edit, second student ---------------------------------------------------------------
  const revBeforeFirst = last()!.revision;
  const sub1 = (await S.doubts.submitDoubt({ roundId: r1.id, text: `  ${TEXT1}  ` })).doubt!;
  check("SubmitDoubt -> the text (trimmed) and editable_until = the round's deadline", sub1.text === TEXT1 && secs(sub1.editableUntil) === secs(e2.closesAt));
  check("the counter goes to 1", (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 1);
  check("WatchLesson: the first submission shows (count 1, revision moved)", await until(() => last()?.active === r1.id && last()?.submitted === 1 && last()!.revision > revBeforeFirst, 3000), `revision ${revBeforeFirst} -> ${last()?.revision}`);
  check("snapshot per student: my_submitted true for the author, false for another student",
    (await S.lessons.getLessonSnapshot({ lessonId: room.id })).snapshot?.activeDoubtRound?.mySubmitted === true
    && (await S2.lessons.getLessonSnapshot({ lessonId: room.id })).snapshot?.activeDoubtRound?.mySubmitted === false);
  const l1 = (await T.doubts.listDoubts({ roundId: r1.id })).doubts;
  const id1 = l1[0]?.id;
  check("ListDoubts -> the original text, no group yet", l1.length === 1 && l1[0].text === TEXT1 && l1[0].groupId === "");
  check("GetDoubtRound (student) -> my_doubt with the text", (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.myDoubt?.text === TEXT1);

  const revBeforeEdit = last()!.revision;
  await S.doubts.submitDoubt({ roundId: r1.id, text: TEXT2 });
  const l2 = (await T.doubts.listDoubts({ roundId: r1.id })).doubts;
  check("the edit keeps the SAME doubt (same id, one row) with the new text", l2.length === 1 && l2[0].id === id1 && l2[0].text === TEXT2);
  check("GetDoubtRound (student) -> my_doubt shows the edited text", (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.myDoubt?.text === TEXT2);
  check("an edit does NOT raise the counter (still 1)", (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 1);
  check("WatchLesson: the edit moves revision but the count stays 1", await until(() => last()!.revision > revBeforeEdit, 3000) && last()?.submitted === 1, `revision ${revBeforeEdit} -> ${last()?.revision}, count ${last()?.submitted}`);

  await S2.doubts.submitDoubt({ roundId: r1.id, text: TEXT_B });
  check("a second student raises the counter to 2", (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 2 && await until(() => last()?.submitted === 2, 3000));
  const mineS1 = (await S.doubts.getDoubtRound({ roundId: r1.id })).round!;
  const mineS2 = (await S2.doubts.getDoubtRound({ roundId: r1.id })).round!;
  check("each student only gets their own doubt", mineS1.myDoubt?.text === TEXT2 && mineS2.myDoubt?.text === TEXT_B && !json(mineS1).includes(TEXT_B) && !json(mineS2).includes(TEXT2));
  check("the teacher gets the aggregate, never a my_doubt", (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.myDoubt === undefined);

  // Three simultaneous FIRST submissions of the same student: the unique (round_id, student_id) leaves one row.
  const burst = await Promise.allSettled(["uno", "dos", "tres"].map((t) => S3.doubts.submitDoubt({ roundId: r1.id, text: t })));
  check("3 concurrent first submissions all succeed", burst.every((b) => b.status === "fulfilled"), burst.map((b) => b.status).join(","));
  const l3 = (await T.doubts.listDoubts({ roundId: r1.id })).doubts;
  check("...and leave exactly ONE doubt for that student (3 doubts in the round, 3 students)", l3.length === 3 && new Set(l3.map((d) => d.id)).size === 3 && (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 3);
  check("ListDoubts keeps a stable order and the first student's id", l3[0].id === id1 && json((await T.doubts.listDoubts({ roundId: r1.id })).doubts) === json(l3));
  const privacyKeys = Object.keys(l3[0]).filter((k) => !k.startsWith("$"));
  check("OriginalDoubt exposes only id, text and group_id", privacyKeys.join(",") === "id,text,groupId", privacyKeys.join(","));
  check("no identity anywhere in the ListDoubts response", ![s1Login, s2Login, s3Login].some((u) => json(l3).includes(u.id) || json(l3).includes(u.name) || json(l3).includes(u.id.slice(0, 8))));
  check("WatchLesson: 3 students, and the shared snapshot carries no text or identity",
    await until(() => last()?.submitted === 3, 3000)
    && ![TEXT1, TEXT2, TEXT_B, "uno", "tres"].some((t) => json(lastRaw).includes(`"${t}"`)) && ![s1Login, s2Login, s3Login].some((u) => json(lastRaw).includes(u.id)));
  const fresh = await firstSnapshot(T, room.id);
  check("after a reconnection the first snapshot already has the round and the count (3)", fresh.activeDoubtRound?.id === r1.id && fresh.activeDoubtRound.submittedCount === 3);
  check("...and GetDoubtRound recovers the student's own doubt", (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.myDoubt?.text === TEXT2);

  // --- close ----------------------------------------------------------------------------------------------
  const c1 = (await T.doubts.closeDoubtRound({ roundId: r1.id })).round!;
  check("CloseDoubtRound -> CLOSED, summary still PENDING (grouping is a later step)", c1.status === doubts.DoubtRoundStatus.CLOSED && c1.summaryStatus === doubts.SummaryStatus.PENDING);
  await expectError("closing a closed round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.closeDoubtRound({ roundId: r1.id }));
  await expectError("extending a closed round -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => T.doubts.extendDoubtRound({ roundId: r1.id }));
  check("a closed round stays readable by the student", (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.status === doubts.DoubtRoundStatus.CLOSED);
  await expectError("editing after the round closed -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => S.doubts.submitDoubt({ roundId: r1.id, text: "tarde" }));
  await expectError("a student with no doubt cannot submit after the close -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => (clients(s2Login)).doubts.submitDoubt({ roundId: r1.id, text: "tarde" }));
  check("the closed round keeps the doubts: own doubt unchanged, teacher still lists 3 originals",
    (await S.doubts.getDoubtRound({ roundId: r1.id })).round!.myDoubt?.text === TEXT2 && (await T.doubts.listDoubts({ roundId: r1.id })).doubts.length === 3
    && (await T.doubts.getDoubtRound({ roundId: r1.id })).round!.submittedCount === 3);
  check("WatchLesson: no active round, last closed = round 1 (with its 3 submissions)", await until(() => last()?.active === undefined && last()?.lastClosed === r1.id && last()?.lastClosedSubmitted === 3, 3000));

  // --- expiry: the server closes the round at closes_at, nobody pressed "Cerrar ahora" -----------------------
  const r2 = (await T.doubts.openDoubtRound({ lessonId: room.id, durationSeconds: 2 })).round!;
  check("a new round opens after the previous one closed (2 s)", r2.status === doubts.DoubtRoundStatus.OPEN && r2.id !== r1.id && secs(r2.closesAt) - secs(r2.openedAt) <= 3);
  check("WatchLesson: round 2 active, round 1 still the last closed", await until(() => last()?.active === r2.id && last()?.lastClosed === r1.id, 3000));
  await S.doubts.submitDoubt({ roundId: r2.id, text: "dentro del tiempo" });
  check("a doubt sent before the deadline is accepted", (await T.doubts.listDoubts({ roundId: r2.id })).doubts.length === 1);
  // No API call from here until the stream says so: only the server's own timer can produce the change.
  check("WatchLesson pushes the expiry by itself: round 2 no longer active, now the last closed", await until(() => last()?.active === undefined && last()?.lastClosed === r2.id, 5000));
  const expired = (await T.doubts.getDoubtRound({ roundId: r2.id })).round!;
  check("GetDoubtRound -> CLOSED after expiry", expired.status === doubts.DoubtRoundStatus.CLOSED);
  await expectError("submitting after the deadline -> DOUBT_ROUND_CLOSED", "DOUBT_ROUND_CLOSED", () => S.doubts.submitDoubt({ roundId: r2.id, text: "demasiado tarde" }));
  check("the doubt sent in time is kept and the late one was not stored", (await T.doubts.listDoubts({ roundId: r2.id })).doubts.map((d) => d.text).join("|") === "dentro del tiempo");
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
