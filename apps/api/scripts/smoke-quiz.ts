// End-to-end check of the live quiz (Flujo 4) against a RUNNING api. Uses the generated TypeScript clients, so it proves
// the contract and the server agree. Works on "Sistemas Operativos" (CC3064), not on the Bases de Datos 2 demo.
// It creates its own lesson and material and removes them at the end (also when a check fails).
import { ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, errors, identity, lessons, quizzes } from "@aula/rpc";
import { db } from "../src/db/index.js";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
let failures = 0;
const check = (name: string, ok: boolean, extra = "") => { console.log(`${ok ? "PASS" : "FAIL"}  ${name}${extra ? "  " + extra : ""}`); if (!ok) failures++; };

const withAuth = (token?: string, school?: string): Interceptor => (next) => async (req) => {
  if (token) req.header.set("Authorization", `Bearer ${token}`);
  if (school) req.header.set("X-School-Id", school);
  return next(req);
};
const transport = (token?: string, school?: string) => createConnectTransport({ baseUrl, httpVersion: "1.1", interceptors: [withAuth(token, school)] });
const codeOf = (e: unknown) => ConnectError.from(e).metadata.get("Aula-Error-Code") ?? ConnectError.from(e).code;

/** Runs the call and expects the domain code in the Aula-Error-Code header. */
async function refused(name: string, code: keyof typeof errors.ErrorCode, call: () => Promise<unknown>) {
  try { await call(); check(name, false, "was accepted"); }
  catch (e) { const got = codeOf(e); check(name, got === code, `expected ${code}, got ${got}`); }
}

async function login(email: string) {
  const res = await createClient(identity.IdentityService, transport()).login({ email, password: "demo1234" });
  return { token: res.session!.accessToken, school: res.session!.user!.school!.id, userId: res.session!.user!.id };
}

const SO_MATERIAL = [
  "Diapositiva 9. Planificación de procesos. El planificador decide qué proceso listo recibe la CPU. Round robin asigna a cada proceso un quantum de tiempo y lo reencola al agotarlo.",
  "Diapositiva 12. Cambio de contexto. Al cambiar de proceso el sistema guarda el estado del actual y carga el del siguiente. Ese cambio tiene costo: no produce trabajo útil mientras ocurre.",
  "Diapositiva 15. Colas multinivel con retroalimentación. Los procesos se mueven entre colas según su comportamiento. Un proceso intensivo en E/S suele quedarse en colas de prioridad alta.",
].join("\n\n");

const t = await login("ana.morales@sanmarcos.edu.gt");
const m = await login("marta.ramirez@sanmarcos.edu.gt");
const a = await login("ana.lucia.mendez@sanmarcos.edu.gt");
const teacherLessons = createClient(lessons.LessonsService, transport(t.token, t.school));
const teacherQuizzes = createClient(quizzes.QuizService, transport(t.token, t.school));
const martaQuizzes = createClient(quizzes.QuizService, transport(m.token, m.school));
const anaQuizzes = createClient(quizzes.QuizService, transport(a.token, a.school));
const martaLessons = createClient(lessons.LessonsService, transport(m.token, m.school));
const anaLessons = createClient(lessons.LessonsService, transport(a.token, a.school));

const STATUS = quizzes.QuizStatus;
const LABELS = ["A", "B", "C", "D"];
const isValidQuiz = (q: quizzes.Quiz) => q.options.map((o) => o.label).join("") === "ABCD" && LABELS.includes(q.correctLabel) && q.question.trim().length > 0;

let room: lessons.Lesson | undefined;
let materialId: string | undefined;
let fileId: string | undefined;
let watching: Promise<void> | undefined;
const ac = new AbortController();
const snapshots: lessons.LessonSnapshot[] = [];

try {
  // 0. the SO course (the other agent works on Bases de Datos 2 at the same time: do not touch it)
  const so = (await createClient(courses.CoursesService, transport(t.token, t.school)).listCourses({})).courses.find((c) => c.code === "CC3064");
  check("teacher owns Sistemas Operativos (CC3064)", !!so);
  const soId = so!.id;

  // a leftover active lesson of this course (from an earlier run that crashed) would block openRoom: end it
  const stale = await db.selectFrom("lesson").select(["id", "status"]).where("school_id", "=", t.school).where("course_id", "=", soId).where("status", "in", ["lobby", "live"]).execute();
  for (const s of stale) {
    if (s.status === "lobby") await teacherLessons.startLesson({ lessonId: s.id });
    await teacherLessons.endLesson({ lessonId: s.id });
    console.log(`note  ended a leftover lesson of this course (${s.id})`);
  }

  // 1. the room, the students, the material
  room = (await teacherLessons.openRoom({ courseId: soId })).lesson!;
  check("openRoom -> lobby", room!.status === courses.LessonStatus.LOBBY);
  const watcher = createClient(lessons.LessonsService, transport(t.token, t.school)).watchLesson({ lessonId: room!.id }, { signal: ac.signal });
  watching = (async () => { try { for await (const ev of watcher) if (ev.event.case === "snapshot") snapshots.push(ev.event.value); } catch (e) { if (!ac.signal.aborted) throw e; } })();

  fileId = crypto.randomUUID();
  materialId = crypto.randomUUID();
  const now = new Date();
  await db.insertInto("file").values({ id: fileId, school_id: t.school, content_type: "application/pdf", size_bytes: 1, storage_key: `smoke-quiz/${fileId}.pdf`, status: "uploaded", created_at: now, uploaded_at: now }).execute();
  await db.insertInto("lesson_material").values({ id: materialId, school_id: t.school, lesson_id: room!.id, file_id: fileId, filename: "smoke-quiz-procesos.pdf", extracted_text: SO_MATERIAL, created_at: now }).execute();
  check("material with slides 9, 12, 15 inserted for the new lesson", true);

  await martaLessons.joinRoom({ lessonId: room!.id });
  await anaLessons.joinRoom({ lessonId: room!.id });
  await teacherLessons.startLesson({ lessonId: room!.id });
  check("startLesson -> live", (await teacherLessons.getLessonSnapshot({ lessonId: room!.id })).snapshot!.status === courses.LessonStatus.LIVE);

  // 2. drafting
  await refused("student cannot DraftQuiz -> FORBIDDEN", "FORBIDDEN", () => martaQuizzes.draftQuiz({ lessonId: room!.id, slideNumber: 0 }));
  const drafted = (await teacherQuizzes.draftQuiz({ lessonId: room!.id, slideNumber: 0 })).quiz!;
  check("DraftQuiz -> DRAFT with options A-D and a correct label", drafted.status === STATUS.DRAFT && isValidQuiz(drafted), `${drafted.options.length} options, correct ${drafted.correctLabel}`);
  check("DraftQuiz without a slide focus takes the first slide (9)", drafted.slideNumber === 9, `slide ${drafted.slideNumber}`);
  check("teacher sees the correct label of a draft", drafted.correctLabel !== "");
  await refused("student cannot GetQuiz a draft -> QUIZ_NOT_FOUND", "QUIZ_NOT_FOUND", () => martaQuizzes.getQuiz({ quizId: drafted.id }));

  const regenerated = (await teacherQuizzes.regenerateQuiz({ quizId: drafted.id })).quiz!;
  check("RegenerateQuiz -> a valid draft", regenerated.status === STATUS.DRAFT && isValidQuiz(regenerated), `question changed: ${regenerated.question !== drafted.question}`);

  const timed = (await teacherQuizzes.updateQuizTime({ quizId: drafted.id, timeLimitSeconds: 20 })).quiz!;
  check("UpdateQuizTime 20 s on the draft", timed.timeLimitSeconds === 20);

  // 3. launch
  const launchedAt = Date.now();
  const live = (await teacherQuizzes.launchQuiz({ quizId: drafted.id })).quiz!;
  check("LaunchQuiz -> LIVE", live.status === STATUS.LIVE);
  const closesMs = Number(live.closesAt?.seconds ?? 0) * 1000;
  check("closes_at is about now + 20 s", Math.abs(closesMs - (launchedAt + 20_000)) < 5_000, `in ${Math.round((closesMs - launchedAt) / 1000)} s`);

  const second = (await teacherQuizzes.draftQuiz({ lessonId: room!.id, slideNumber: 12 })).quiz!;
  check("second DraftQuiz focuses the requested slide (12)", second.slideNumber === 12, `slide ${second.slideNumber}`);
  await refused("answering a draft -> QUIZ_NOT_LIVE", "QUIZ_NOT_LIVE", () => martaQuizzes.answerQuiz({ quizId: second.id, label: "A" }));
  await refused("launching another quiz while one is live -> QUIZ_ALREADY_LIVE", "QUIZ_ALREADY_LIVE", () => teacherQuizzes.launchQuiz({ quizId: second.id }));
  await refused("RegenerateQuiz of a live quiz -> QUIZ_NOT_DRAFT", "QUIZ_NOT_DRAFT", () => teacherQuizzes.regenerateQuiz({ quizId: drafted.id }));

  // 4. answers (first answer wins), and the security projection while live
  const correct = drafted.correctLabel;
  const wrong = LABELS.find((l) => l !== correct)!;
  await martaQuizzes.answerQuiz({ quizId: drafted.id, label: wrong });
  check("student A answers a wrong option", true, wrong);
  await refused("answering again -> QUIZ_ALREADY_ANSWERED", "QUIZ_ALREADY_ANSWERED", () => martaQuizzes.answerQuiz({ quizId: drafted.id, label: correct }));
  await anaQuizzes.answerQuiz({ quizId: drafted.id, label: correct });
  check("student B answers the correct option", true, correct);

  const studentLive = (await martaQuizzes.getQuiz({ quizId: drafted.id })).quiz!;
  check("SECURITY student GetQuiz while live: no correctLabel", studentLive.correctLabel === "");
  check("SECURITY student GetQuiz while live: no countsByLabel", Object.keys(studentLive.countsByLabel).length === 0);
  check("SECURITY student GetQuiz while live: no rationale or insight", studentLive.rationale === "" && studentLive.insight === "");
  check("student GetQuiz while live: myLabel is the own answer", studentLive.myLabel === wrong && studentLive.status === STATUS.LIVE);

  const teacherLive = (await teacherQuizzes.getQuiz({ quizId: drafted.id })).quiz!;
  check("teacher GetQuiz live: counts for the 4 labels, answeredCount 2", Object.keys(teacherLive.countsByLabel).length === 4 && teacherLive.answeredCount === 2 && teacherLive.enrolledCount >= 2,
    `counts ${JSON.stringify(teacherLive.countsByLabel)}, enrolled ${teacherLive.enrolledCount}`);
  check("teacher GetQuiz live: correctLabel is set", teacherLive.correctLabel === correct);

  await new Promise((r) => setTimeout(r, 500));
  const lastSnap = snapshots.at(-1);
  check("WatchLesson snapshot shows activeQuiz.answeredCount 2", lastSnap?.activeQuiz?.answeredCount === 2 && lastSnap.activeQuiz.id === drafted.id, `activeQuiz ${lastSnap?.activeQuiz?.answeredCount}`);

  // 5. close and results
  const closed = (await teacherQuizzes.closeQuiz({ quizId: drafted.id })).quiz!;
  check("CloseQuiz -> CLOSED", closed.status === STATUS.CLOSED);
  check("teacher result: correctPercent 50 and an insight about the wrong option", closed.correctPercent === 50 && closed.insight.includes("eligieron") && closed.insight.includes("repasar"), `"${closed.insight}"`);
  await refused("answering after close -> QUIZ_CLOSED", "QUIZ_CLOSED", () => anaQuizzes.answerQuiz({ quizId: drafted.id, label: wrong }));
  await refused("UpdateQuizTime on a closed quiz -> QUIZ_CLOSED", "QUIZ_CLOSED", () => teacherQuizzes.updateQuizTime({ quizId: drafted.id, timeLimitSeconds: 30 }));
  await refused("closing a draft -> QUIZ_NOT_LIVE", "QUIZ_NOT_LIVE", () => teacherQuizzes.closeQuiz({ quizId: second.id }));

  const studentClosed = (await martaQuizzes.getQuiz({ quizId: drafted.id })).quiz!;
  check("student GetQuiz closed: correctLabel, correctPercent, myLabel", studentClosed.correctLabel === correct && studentClosed.correctPercent === 50 && studentClosed.myLabel === wrong);
  check("student GetQuiz closed: counts and insight still hidden", Object.keys(studentClosed.countsByLabel).length === 0 && studentClosed.insight === "");

  await new Promise((r) => setTimeout(r, 500));
  check("stream: no activeQuiz once the quiz is closed", snapshots.at(-1)?.activeQuiz === undefined);
} catch (e) {
  failures++;
  console.log(`FAIL  unexpected error: ${codeOf(e)} ${e instanceof Error ? e.message : ""}`);
} finally {
  ac.abort();
  await watching?.catch(() => {});
  if (room) {
    try { await teacherLessons.endLesson({ lessonId: room!.id }); } catch { /* already ended */ }
    // remove what this run created: quizzes and their answers, the material and its file, the lesson itself
    const quizIds = (await db.selectFrom("quiz").select("id").where("school_id", "=", t.school).where("lesson_id", "=", room!.id).execute()).map((r) => r.id);
    if (quizIds.length) await db.deleteFrom("quiz_answer").where("school_id", "=", t.school).where("quiz_id", "in", quizIds).execute();
    await db.deleteFrom("quiz").where("school_id", "=", t.school).where("lesson_id", "=", room!.id).execute();
    if (materialId) await db.deleteFrom("lesson_material").where("school_id", "=", t.school).where("id", "=", materialId).execute();
    if (fileId) await db.deleteFrom("file").where("school_id", "=", t.school).where("id", "=", fileId).execute();
    await db.deleteFrom("pulse_event").where("school_id", "=", t.school).where("lesson_id", "=", room!.id).execute();
    await db.deleteFrom("lesson_participant").where("school_id", "=", t.school).where("lesson_id", "=", room!.id).execute();
    await db.deleteFrom("lesson").where("school_id", "=", t.school).where("id", "=", room!.id).execute();
    console.log("note  lesson ended; the smoke lesson, its quizzes, material and file were removed");
  }
  await db.destroy();
}

console.log(`\nAI path: ${process.env.ANTHROPIC_API_KEY ? "ANTHROPIC_API_KEY is set (the server may call the model)" : "not exercised (no ANTHROPIC_API_KEY): deterministic fallback only"}`);
console.log(failures ? `\n${failures} check(s) FAILED` : "\nall checks passed");
process.exit(failures ? 1 : 0);
