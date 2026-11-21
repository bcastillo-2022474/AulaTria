// End-to-end check against a RUNNING api (mage api:dev) on a freshly seeded database (mage db:seed).
// Uses the generated TypeScript clients, so it also proves the contract and the server agree.
import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-node";
import { courses, errors, identity, lessons } from "@aula/rpc";

const baseUrl = process.env.API_URL ?? "http://localhost:3000";
let failures = 0;
const check = (name: string, ok: boolean, extra = "") => { console.log(`${ok ? "PASS" : "FAIL"}  ${name}${extra ? "  " + extra : ""}`); if (!ok) failures++; };

const withAuth = (token?: string, school?: string): Interceptor => (next) => async (req) => {
  if (token) req.header.set("Authorization", `Bearer ${token}`);
  if (school) req.header.set("X-School-Id", school);
  return next(req);
};
const transport = (token?: string, school?: string) => createConnectTransport({ baseUrl, httpVersion: "1.1", interceptors: [withAuth(token, school)] });

async function login(email: string) {
  const res = await createClient(identity.IdentityService, transport()).login({ email, password: "demo1234" });
  return { token: res.session!.accessToken, school: res.session!.user!.school!.id };
}

const t = await login("ana.morales@sanmarcos.edu.gt");
const s = await login("marta.ramirez@sanmarcos.edu.gt");
const teacher = { id: createClient(courses.CoursesService, transport(t.token, t.school)), lessons: createClient(lessons.LessonsService, transport(t.token, t.school)) };
const student = { lessons: createClient(lessons.LessonsService, transport(s.token, s.school)), courses: createClient(courses.CoursesService, transport(s.token, s.school)) };

// 1. domain error codes travel in the header, and the tenant header is enforced
try { await createClient(identity.IdentityService, transport()).login({ email: "ana.morales@sanmarcos.edu.gt", password: "wrong-password" }); check("bad password is refused", false); }
catch (e) { const ce = ConnectError.from(e); check("bad password -> USER_INVALID_CREDENTIALS", ce.code === Code.Unauthenticated && ce.metadata.get("Aula-Error-Code") === errors.ErrorCode[errors.ErrorCode.USER_INVALID_CREDENTIALS]); }
try { await createClient(courses.CoursesService, transport(t.token, "00000000-0000-0000-0000-000000000000")).listCourses({}); check("wrong tenant header is refused", false); }
catch (e) { check("wrong X-School-Id -> TENANT_MISMATCH", ConnectError.from(e).metadata.get("Aula-Error-Code") === "TENANT_MISMATCH"); }
try { await student.courses.createCourse({ name: "x" }); check("student cannot create a course", false); }
catch (e) { check("student createCourse -> FORBIDDEN", ConnectError.from(e).metadata.get("Aula-Error-Code") === "FORBIDDEN"); }

// 2. role-scoped reads
const list = await teacher.id.listCourses({});
check("teacher sees 2 courses", list.courses.length === 2, list.courses.map((c) => c.name).join(", "));
const bd2 = list.courses.find((c) => c.code === "CC3057")!;
check("BD2 has 38 students", bd2.studentCount === 38);

// 3. a live lesson and the realtime stream
const room = (await teacher.lessons.openRoom({ courseId: bd2.id })).lesson!;
check("openRoom -> a new lesson (after the 11 seeded) in lobby", room.number > 11 && room.status === courses.LessonStatus.LOBBY, `lesson ${room.number}`);
try { await teacher.lessons.openRoom({ courseId: bd2.id }); check("second room is refused", false); }
catch (e) { check("second openRoom -> LESSON_ALREADY_ACTIVE", ConnectError.from(e).metadata.get("Aula-Error-Code") === "LESSON_ALREADY_ACTIVE"); }

const seen: { status: number; joined: number; lost: number; connected: number }[] = [];
const ac = new AbortController();
const watching = (async () => {
  try {
    for await (const ev of teacher.lessons.watchLesson({ lessonId: room.id }, { signal: ac.signal })) {
      if (ev.event.case === "snapshot") {
        const sn = ev.event.value;
        seen.push({ status: sn.status, joined: sn.joinedCount, lost: sn.pulse?.lostCount ?? 0, connected: sn.connectedCount });
      }
    }
  } catch (e) { if (!ac.signal.aborted) throw e; }
})();
await new Promise((r) => setTimeout(r, 400));
await student.lessons.joinRoom({ lessonId: room.id });
await teacher.lessons.startLesson({ lessonId: room.id });
await student.lessons.setPulse({ lessonId: room.id, state: lessons.PulseState.LOST });
await new Promise((r) => setTimeout(r, 600));
ac.abort(); await watching;
const last = seen.at(-1);
check("stream delivered snapshots", seen.length >= 2, `${seen.length} messages`);
check("stream shows live + 1 joined + 1 lost", last?.status === courses.LessonStatus.LIVE && last.joined === 1 && last.lost === 1, JSON.stringify(last));
const mine = (await student.lessons.getLessonSnapshot({ lessonId: room.id })).snapshot!;
check("student snapshot carries my_pulse", mine.myPulse === lessons.PulseState.LOST);

await teacher.lessons.endLesson({ lessonId: room.id });
check("endLesson", (await teacher.lessons.getLessonSnapshot({ lessonId: room.id })).snapshot!.status === courses.LessonStatus.ENDED);

console.log(failures ? `\n${failures} check(s) FAILED` : "\nall checks passed");
process.exit(failures ? 1 : 0);
