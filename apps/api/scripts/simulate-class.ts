// Classroom simulator: makes a live lesson feel full by acting as N enrolled students through the REAL API.
// Teacher-side demo/test tool for the floor (Flujo 3) and quiz (Flujo 4) flows. It does NOT open or start lessons:
// first open the room from the teacher screen ("Abrir sala"), then run this.
//
// Usage:
//   pnpm --filter @aula/api simulate -- --course CC3057 [--students 25] [--api http://localhost:3000] [--seconds 600]
//                                       [--no-pulse] [--no-floor] [--no-quiz]
//   --course    course code (exact or suffix, case-insensitive) or a fragment of the course name, of ana.morales
//   --students  how many enrolled students to simulate (the first N of the course)
//   --seconds   run time; Ctrl-C also stops cleanly (exit 0). It never ends the lesson.
//   --no-X      switch a loop off. pulse, floor and quiz are on by default.
// Logins use the seed password (demo1234). Students are derived from the names exactly as the seed does.
import { ConnectError, createClient } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, floor, identity, lessons, quizzes } from "@aula/rpc";

const argv = process.argv.slice(2).filter((a) => a !== "--");
const arg = (name: string) => { const i = argv.indexOf(`--${name}`); return i >= 0 ? argv[i + 1] : undefined; };
const on = (name: string) => !argv.includes(`--no-${name}`);
const course = arg("course");
const apiUrl = arg("api") ?? "http://localhost:3000";
const studentCount = Number(arg("students") ?? 25);
const seconds = Number(arg("seconds") ?? 600);
if (!course || !(studentCount >= 1) || !(seconds > 0)) {
  console.error("usage: pnpm --filter @aula/api simulate -- --course CC3057 [--students 25] [--api URL] [--seconds 600] [--no-pulse] [--no-floor] [--no-quiz]");
  process.exit(2);
}

const PASSWORD = "demo1234";
const TEACHER = "ana.morales@sanmarcos.edu.gt";
const QUIET_FLOOR = new Set(["FLOOR_ROUND_CLOSED"]);
const QUIET_QUIZ = new Set(["QUIZ_ALREADY_ANSWERED", "QUIZ_CLOSED"]);
const QUIET_PULSE = new Set(["LESSON_NOT_LIVE"]);

const stamp = () => new Date().toTimeString().slice(0, 8);
const log = (msg: string) => console.log(`[${stamp()}] ${msg}`);
const rand = (min: number, max: number) => min + Math.random() * (max - min);
const pick = <T,>(xs: T[]): T => xs[Math.floor(Math.random() * xs.length)];
function shuffle<T>(xs: T[]): T[] {
  const out = [...xs];
  for (let i = out.length - 1; i > 0; i--) { const j = Math.floor(Math.random() * (i + 1)); [out[i], out[j]] = [out[j], out[i]]; }
  return out;
}
const msOf = (ts?: { seconds: bigint; nanos: number }) => (ts ? Number(ts.seconds) * 1000 + ts.nanos / 1e6 : 0);
const errCode = (e: unknown) => { const ce = ConnectError.from(e); return ce.metadata.get("Aula-Error-Code") ?? ConnectError.from(e).message; };
/** Runs one call; returns false on a domain refusal, logging it unless the code is an expected race (quiet). */
async function attempt(fn: () => Promise<unknown>, quiet: Set<string>): Promise<boolean> {
  try { await fn(); return true; }
  catch (e) { const code = errCode(e); if (!quiet.has(code)) log(`warning: ${code}`); return false; }
}

/** Derives the seed login email from a student name, exactly as scripts/seed.ts does. */
const emailOf = (name: string) => name.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/ /g, ".") + "@sanmarcos.edu.gt";
/** "Marta Ramírez" -> "Marta R." (the name shown in the log). */
const shortName = (name: string) => { const p = name.split(" "); return `${p[0]} ${p[p.length - 1][0]}.`; };

async function login(email: string) {
  const res = await createClient(identity.IdentityService, createConnectTransport({ baseUrl: apiUrl, httpVersion: "1.1" }))
    .login({ email, password: PASSWORD });
  return { token: res.session!.accessToken, school: res.session!.user!.school!.id };
}

/** One actor: the typed clients it may use, on one token and tenant. */
function bundle(name: string, token: string, school: string) {
  const transport = createConnectTransport({
    baseUrl: apiUrl,
    httpVersion: "1.1",
    interceptors: [(next) => (req) => { req.header.set("Authorization", `Bearer ${token}`); req.header.set("X-School-Id", school); return next(req); }],
  });
  return {
    name, short: shortName(name),
    courses: createClient(courses.CoursesService, transport),
    lessons: createClient(lessons.LessonsService, transport),
    floor: createClient(floor.FloorService, transport),
    quizzes: createClient(quizzes.QuizService, transport),
  };
}

// ---- resolve the course and its live lesson (the teacher's view) --------------------------------------------
const teacherLogin = await login(TEACHER);
const T = bundle(TEACHER, teacherLogin.token, teacherLogin.school);
const { courses: mine } = await T.courses.listCourses({});
const q = course.toLowerCase();
const exact = mine.filter((c) => c.code.toLowerCase() === q);
const matches = exact.length ? exact : mine.filter((c) => c.code.toLowerCase().endsWith(q) || c.name.toLowerCase().includes(q));
if (matches.length !== 1) {
  console.error(matches.length ? `"${course}" matches several courses: ${matches.map((c) => c.code || c.name).join(", ")}` : `no course of ${TEACHER} matches "${course}"`);
  process.exit(1);
}
const target = matches[0];
const live = target.liveLesson;
if (!live) {
  console.error(`${target.name} has no live lesson. Open the room first from the teacher screen ("Abrir sala").`);
  process.exit(1);
}
const lessonId = live.id;
let status = live.status;
let skewMs = 0; // server clock minus local clock, from the snapshot, so countdowns use the server's deadlines

// ---- log the simulated students in and into the room -----------------------------------------------------------
const roster = (await T.courses.listCourseStudents({ courseId: target.id })).students.slice(0, studentCount);
const logins = await Promise.allSettled(roster.map((s) => login(emailOf(s.name))));
const sims = roster.flatMap((s, i) => {
  const r = logins[i];
  if (r.status === "rejected") { log(`skip ${s.name}: login failed`); return []; }
  return [bundle(s.name, r.value.token, r.value.school)];
});
if (sims.length === 0) { console.error("no simulated student could log in"); process.exit(1); }
if (status === courses.LessonStatus.LOBBY || status === courses.LessonStatus.LIVE) {
  await Promise.all(sims.map((s) => attempt(() => s.lessons.joinRoom({ lessonId }), new Set())));
}
log(`simulating ${sims.length} students in ${target.name}, lesson ${live.number} (${courses.LessonStatus[status]})`);

// ---- loops ---------------------------------------------------------------------------------------------------------
const handledRounds = new Set<string>();
const handledQuizzes = new Set<string>();
const timers: NodeJS.Timeout[] = [];

function stop(code: number, why = "stopped") {
  for (const t of timers) clearTimeout(t);
  log(why);
  process.exit(code);
}

/** Pulse: every 4 s, 10-20% of the students tap, 80% "ok" / 20% "lost". Only while the lesson is live. */
async function pulse() {
  if (status !== courses.LessonStatus.LIVE) return;
  const k = Math.max(1, Math.round(sims.length * rand(0.1, 0.2)));
  const results = await Promise.all(shuffle(sims).slice(0, k).map(async (s) => {
    const ok = Math.random() < 0.8;
    const state = ok ? lessons.PulseState.OK : lessons.PulseState.LOST;
    return (await attempt(() => s.lessons.setPulse({ lessonId, state }), QUIET_PULSE)) ? ok : null;
  }));
  const sent = results.filter((r): r is boolean => r !== null);
  if (sent.length) log(`${sent.length} students tapped pulse (${sent.filter(Boolean).length} ok, ${sent.filter((r) => !r).length} lost)`);
}

/** Floor: 3-6 random students raise their hands, each 1-4 s after the round opens, at jittered times. */
function raiseHands(roundId: string) {
  const picks = shuffle(sims).slice(0, Math.min(sims.length, Math.floor(rand(3, 7))));
  const raised: string[] = [];
  let pending = picks.length;
  for (const s of picks) {
    setTimeout(async () => {
      if (await attempt(() => s.floor.raiseHand({ roundId }), QUIET_FLOOR)) raised.push(s.short);
      if (--pending === 0 && raised.length) log(`raised hands: ${raised.join(", ")}`);
    }, rand(1000, 4000));
  }
}

/** Quiz: every student answers once, 2-20 s after the quiz appears and never after closesAt. About half pick the correct option. */
async function answerQuiz(quizId: string, deadlineMs: number) {
  const quiz = (await T.quizzes.getQuiz({ quizId })).quiz; // the teacher may read the correct label: used only to bias the answers
  if (!quiz) return;
  const labels = quiz.options.map((o) => o.label);
  const correct = quiz.correctLabel || labels[0];
  const wrong = labels.filter((l) => l !== correct);
  let answered = 0, right = 0, pending = sims.length;
  const finish = () => { if (--pending === 0) log(`quiz: ${answered} answered (${right} correct, ${answered - right} wrong)`); };
  for (const s of sims) {
    const wait = Math.min(rand(2000, 20000), deadlineMs - Date.now() - 500);
    if (wait < 0) { finish(); continue; }
    setTimeout(async () => {
      const label = Math.random() < 0.5 || wrong.length === 0 ? correct : pick(wrong);
      if (await attempt(() => s.quizzes.answerQuiz({ quizId, label }), QUIET_QUIZ)) { answered++; if (label === correct) right++; }
      finish();
    }, wait);
  }
}

/** The teacher's snapshot, every 1.5 s: status, the active floor round and quiz. Ends the run when the lesson ends. */
async function poll() {
  const snap = (await T.lessons.getLessonSnapshot({ lessonId })).snapshot;
  if (!snap) return;
  status = snap.status;
  if (snap.serverTime) skewMs = msOf(snap.serverTime) - Date.now();
  if (status === courses.LessonStatus.ENDED || status === courses.LessonStatus.CANCELLED) stop(0, "the lesson has ended");
  const round = snap.activeFloorRound;
  if (on("floor") && round && !handledRounds.has(round.id)) { handledRounds.add(round.id); raiseHands(round.id); }
  const quiz = snap.activeQuiz;
  if (on("quiz") && quiz && !handledQuizzes.has(quiz.id)) {
    handledQuizzes.add(quiz.id);
    void answerQuiz(quiz.id, msOf(quiz.closesAt) - skewMs).catch((e) => log(`quiz error: ${errCode(e)}`));
  }
}

let busy = false;
const tick = () => {
  if (busy) return;
  busy = true;
  poll().catch((e) => log(`snapshot error: ${errCode(e)}`)).finally(() => { busy = false; });
};
process.on("SIGINT", () => stop(0, "stopped (Ctrl-C)"));
await poll().catch((e) => log(`snapshot error: ${errCode(e)}`));
timers.push(setInterval(tick, 1500));
if (on("pulse")) timers.push(setInterval(() => { void pulse().catch((e) => log(`pulse error: ${errCode(e)}`)); }, 4000));
timers.push(setTimeout(() => stop(0, "time is up"), seconds * 1000));
