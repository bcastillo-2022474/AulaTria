// End-to-end check of "Toma de la palabra" (floor) against a RUNNING api: API_URL=http://localhost:3101 pnpm smoke:floor
// Uses the generated TypeScript clients, so it also proves the contract and the server agree.
// Only touches "Bases de Datos 2" (CC3057). Always ends the lesson it opened (finally), so the course is free again.
import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, errors, floor, identity, lessons } from "@aula/rpc";

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

/** One actor = the three clients it can use, on one token. */
function clients(who: { token: string; school: string }) {
  const t = transport(who.token, who.school);
  return { floor: createClient(floor.FloorService, t), lessons: createClient(lessons.LessonsService, t), courses: createClient(courses.CoursesService, t) };
}

async function expectError(name: string, expected: string, fn: () => Promise<unknown>) {
  try { await fn(); check(name, false, "no error was returned"); }
  catch (e) { check(name, codeOf(e) === expected, codeOf(e) === expected ? "" : `want ${expected}, got ${codeOf(e)}`); }
}

const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));

const teacherLogin = await login("ana.morales@sanmarcos.edu.gt");
const A = await login("marta.ramirez@sanmarcos.edu.gt");   // student A: answers first
const B = await login("ana.lucia.mendez@sanmarcos.edu.gt"); // student B: queues behind A
const T = clients(teacherLogin);
const sA = clients(A);
const sB = clients(B);

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
const snaps: { revision: number; round?: string }[] = [];
const ac = new AbortController();
let watching: Promise<void> = Promise.resolve();

try {
  // --- the room, the lesson and the realtime stream ----------------------------------------------------------
  const room = (await T.lessons.openRoom({ courseId: bd2.id })).lesson!;
  lessonId = room.id;
  check("openRoom on Bases de Datos 2 -> lobby", room.status === courses.LessonStatus.LOBBY);

  watching = (async () => {
    try {
      for await (const ev of T.lessons.watchLesson({ lessonId: room.id }, { signal: ac.signal })) {
        if (ev.event.case === "snapshot") snaps.push({ revision: ev.event.value.revision, round: ev.event.value.activeFloorRound?.id });
      }
    } catch (e) { if (!ac.signal.aborted) throw e; }
  })();
  await sleep(300);

  await sA.lessons.joinRoom({ lessonId: room.id });
  await sB.lessons.joinRoom({ lessonId: room.id });
  await T.lessons.startLesson({ lessonId: room.id });

  // --- round 1: raise, queue, idempotence, positions -------------------------------------------------------
  const r1 = (await T.floor.openFloorRound({ lessonId: room.id, prompt: "¿Qué es una llave foránea?" })).round!;
  await sleep(400); // the stream coalesces bursts (hub debounce 120 ms): let it settle while the round is open
  check("OpenFloorRound -> open round, nobody answering", r1.status === floor.FloorRoundStatus.OPEN && r1.current === undefined && r1.queue.length === 0 && r1.lessonId === room.id);
  await expectError("second OpenFloorRound on the same lesson -> FLOOR_ROUND_ALREADY_OPEN", "FLOOR_ROUND_ALREADY_OPEN", () => T.floor.openFloorRound({ lessonId: room.id, prompt: "otra" }));
  await expectError("a student cannot open a round -> FORBIDDEN", "FORBIDDEN", () => sA.floor.openFloorRound({ lessonId: room.id, prompt: "x" }));

  const a1 = (await sA.floor.raiseHand({ roundId: r1.id })).round!;
  check("A raises first -> answers at once (responder, 15 s deadline)", a1.current?.studentId === A.id && !!a1.current.deadlineAt && a1.myState === floor.FloorTurnState.ANSWERING && a1.myPosition === 1, `position ${a1.myPosition}`);
  const a2 = (await sA.floor.raiseHand({ roundId: r1.id })).round!;
  check("A raises again -> idempotent (still responder, nothing queued)", a2.current?.studentId === A.id && a2.queue.length === 0 && a2.myPosition === 1);

  const b1 = (await sB.floor.raiseHand({ roundId: r1.id })).round!;
  const bEntry = b1.queue.find((q) => q.studentId === B.id);
  check("B raises -> queued, position 2", b1.myState === floor.FloorTurnState.QUEUED && b1.myPosition === 2, `position ${b1.myPosition}`);
  check("B's queue entry carries offsetMs >= 0", !!bEntry && bEntry.offsetMs >= 0, `offsetMs ${bEntry?.offsetMs}`);
  const b2 = (await sB.floor.raiseHand({ roundId: r1.id })).round!;
  check("B raises again -> idempotent (one queue entry)", b2.queue.length === 1 && b2.myPosition === 2);

  const aView = (await sA.floor.getFloorRound({ roundId: r1.id })).round!;
  const bView = (await sB.floor.getFloorRound({ roundId: r1.id })).round!;
  check("A sees ANSWERING at position 1", aView.myState === floor.FloorTurnState.ANSWERING && aView.myPosition === 1);
  check("B sees QUEUED at position 2", bView.myState === floor.FloorTurnState.QUEUED && bView.myPosition === 2);
  check("B sees the responder's name", bView.current?.name === A.name, bView.current?.name);

  const tView = (await T.floor.getFloorRound({ roundId: r1.id })).round!;
  check("teacher sees current=A and queue=[B], no student-only fields", tView.current?.studentId === A.id && tView.queue.length === 1 && tView.queue[0].studentId === B.id
    && tView.myState === floor.FloorTurnState.UNSPECIFIED && tView.myPosition === 0);

  // --- the answer: finish, incorrect, next student -----------------------------------------------------------
  await expectError("B says 'Ya respondi' without the floor -> FLOOR_NOT_YOUR_TURN", "FLOOR_NOT_YOUR_TURN", () => sB.floor.finishMyTurn({ roundId: r1.id }));
  await sA.floor.finishMyTurn({ roundId: r1.id });
  const aDone = (await sA.floor.getFloorRound({ roundId: r1.id })).round!;
  check("A 'Ya respondi' -> ANSWERED, still position 1", aDone.myState === floor.FloorTurnState.ANSWERED && aDone.myPosition === 1);

  await expectError("a student cannot resolve a turn -> FORBIDDEN", "FORBIDDEN", () => sA.floor.resolveTurn({ roundId: r1.id, verdict: floor.TurnVerdict.CORRECT }));
  const incorrect = (await T.floor.resolveTurn({ roundId: r1.id, verdict: floor.TurnVerdict.INCORRECT })).round!;
  check("teacher INCORRECT -> B becomes the responder, queue empty", incorrect.current?.studentId === B.id && incorrect.queue.length === 0 && incorrect.status === floor.FloorRoundStatus.OPEN);
  const aAfter = (await sA.floor.getFloorRound({ roundId: r1.id })).round!;
  check("A keeps INCORRECT after the verdict, position 0", aAfter.myState === floor.FloorTurnState.INCORRECT && aAfter.myPosition === 0);
  const bNow = (await sB.floor.getFloorRound({ roundId: r1.id })).round!;
  check("B is ANSWERING at position 1", bNow.myState === floor.FloorTurnState.ANSWERING && bNow.myPosition === 1);

  const closed = (await T.floor.resolveTurn({ roundId: r1.id, verdict: floor.TurnVerdict.CORRECT })).round!;
  check("teacher CORRECT (from answering) -> round CLOSED", closed.status === floor.FloorRoundStatus.CLOSED && closed.current === undefined);
  const bFinal = (await sB.floor.getFloorRound({ roundId: r1.id })).round!;
  check("B's myState is CORRECT after the closed round", bFinal.myState === floor.FloorTurnState.CORRECT);
  await expectError("raising on the closed round -> FLOOR_ROUND_CLOSED", "FLOOR_ROUND_CLOSED", () => sA.floor.raiseHand({ roundId: r1.id }));

  // --- visibility -----------------------------------------------------------------------------------------
  await expectError("unknown round (teacher) -> FLOOR_ROUND_NOT_FOUND", "FLOOR_ROUND_NOT_FOUND", () => T.floor.getFloorRound({ roundId: NIL_UUID }));
  await expectError("unknown round (student) -> FLOOR_ROUND_NOT_FOUND", "FLOOR_ROUND_NOT_FOUND", () => sA.floor.getFloorRound({ roundId: NIL_UUID }));

  // --- round 2: lower hand, open to all, empty queue keeps the round open, close ---------------------------
  const r2 = (await T.floor.openFloorRound({ lessonId: room.id, prompt: "¿Qué es la normalización?" })).round!;
  check("a new round opens after round 1 closed", r2.status === floor.FloorRoundStatus.OPEN && r2.id !== r1.id);
  await sleep(400); // see above: the stream must settle while round 2 is open
  await sA.floor.raiseHand({ roundId: r2.id });
  await sB.floor.raiseHand({ roundId: r2.id });
  const openAll = (await T.floor.openFloorToAll({ roundId: r2.id })).round!;
  check("OpenFloorToAll -> flag set, round open, queue untouched", openAll.openToAll && openAll.status === floor.FloorRoundStatus.OPEN && openAll.current?.studentId === A.id && openAll.queue.length === 1);
  await sA.floor.lowerHand({ roundId: r2.id });
  await sA.floor.lowerHand({ roundId: r2.id }); // idempotent
  const aLow = (await sA.floor.getFloorRound({ roundId: r2.id })).round!;
  check("A lowers the hand -> WITHDRAWN, position 0", aLow.myState === floor.FloorTurnState.WITHDRAWN && aLow.myPosition === 0);
  const afterLower = (await T.floor.getFloorRound({ roundId: r2.id })).round!;
  check("A leaving moves the queue: B answers", afterLower.current?.studentId === B.id && afterLower.queue.length === 0);
  const bLowerView = (await sB.floor.getFloorRound({ roundId: r2.id })).round!;
  check("B is ANSWERING at position 1 after A left", bLowerView.myState === floor.FloorTurnState.ANSWERING && bLowerView.myPosition === 1);
  const emptied = (await T.floor.resolveTurn({ roundId: r2.id, verdict: floor.TurnVerdict.INCORRECT })).round!;
  check("INCORRECT with nobody waiting -> round stays OPEN", emptied.status === floor.FloorRoundStatus.OPEN && emptied.current === undefined && emptied.queue.length === 0);
  await sleep(400);
  const r2Closed = (await T.floor.closeFloorRound({ roundId: r2.id })).round!;
  check("CloseFloorRound -> CLOSED", r2Closed.status === floor.FloorRoundStatus.CLOSED);
  await expectError("closing a closed round -> FLOOR_ROUND_CLOSED", "FLOOR_ROUND_CLOSED", () => T.floor.closeFloorRound({ roundId: r2.id }));

  // --- the realtime stream ------------------------------------------------------------------------------
  await sleep(500);
  check("WatchLesson snapshot showed activeFloorRound = round 1 while open", snaps.some((s) => s.round === r1.id), `${snaps.length} snapshots`);
  check("WatchLesson snapshot showed activeFloorRound = round 2 while open", snaps.some((s) => s.round === r2.id));
  check("WatchLesson has no active round after the close", snaps.at(-1)?.round === undefined);
  const revs = snaps.map((s) => s.revision);
  const monotonic = revs.every((r, i) => i === 0 || r >= revs[i - 1]);
  check("WatchLesson revision is increasing", monotonic && revs.length > 1 && revs.at(-1)! > revs[0], `${revs[0]} -> ${revs.at(-1)}`);
} catch (e) {
  check("unexpected error during the floor checks", false, e instanceof Error ? e.message : String(e));
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
