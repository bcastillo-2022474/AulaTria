// Demo data. Idempotent: wipes the school and rebuilds it, so run it as often as you like.
//
//   teacher:  ana.morales@sanmarcos.edu.gt    / demo1234      (owns Bases de Datos 2, CC3057, code BD2-4K9P)
//   student:  marta.ramirez@sanmarcos.edu.gt  / demo1234      (and 37 more, same password)
//   admin:    admin@sanmarcos.edu.gt          / demo1234
//
// The demo starts with NO lesson 12: the teacher presses "Iniciar sala" live. Lessons 1-11 are history
// so Docente-Pulso and Docente-Perfil have real data from the first second.
import bcrypt from "bcryptjs";
import { uuidv7 } from "uuidv7";
import { db } from "../src/db/index.js";

if (process.env.NODE_ENV === "production" && !process.env.SEED_PASSWORD) {
  throw new Error("SEED_PASSWORD must be set in production: the demo accounts must not use the public dev password");
}
const PASSWORD = process.env.SEED_PASSWORD ?? "demo1234";

// `--if-empty`: deploys run this on every release, so it must not wipe a database that already has the demo school.
if (process.argv.includes("--if-empty")) {
  const existing = await db.selectFrom("school").select("id").limit(1).executeTakeFirst();
  if (existing) { console.log("seed skipped: the database already has data"); await db.destroy(); process.exit(0); }
}
const hash = await bcrypt.hash(PASSWORD, 10);
const id = () => uuidv7();
const minutes = (n: number) => n * 60_000;

// Deterministic randomness: the same demo every time.
let seed = 42;
const rnd = () => ((seed = (seed * 1664525 + 1013904223) % 4294967296) / 4294967296);

// FKs have no ON DELETE CASCADE (on purpose): wipe in dependency order.
const old = await db.selectFrom("school").select("id").where("name", "=", "Colegio San Marcos").execute();
for (const { id: sid } of old) {
  for (const t of ["quiz_answer", "quiz", "doubt", "doubt_group", "doubt_round", "floor_turn", "floor_round", "attendance_record", "attendance_round",
    "pulse_event", "lesson_material", "lesson_participant", "student_note", "lesson", "enrollment", "course", "device", "refresh_token", "invitation", "file", "app_user"] as const) {
    await db.deleteFrom(t).where("school_id", "=", sid).execute();
  }
  await db.deleteFrom("school").where("id", "=", sid).execute();
}

const now = new Date();
const schoolId = id();
await db.insertInto("school").values({ id: schoolId, name: "Colegio San Marcos", created_at: now, updated_at: now }).execute();

const user = (role: "admin" | "teacher" | "student", name: string, email: string, status: "active" | "invited" = "active") =>
  ({ id: id(), school_id: schoolId, role, status, name, email, password_hash: status === "active" ? hash : null, created_at: now, updated_at: now });

const admin = user("admin", "Directora San Marcos", "admin@sanmarcos.edu.gt");
const ana = user("teacher", "Ana Morales", "ana.morales@sanmarcos.edu.gt");
const luis = user("teacher", "Luis Pérez", "luis.perez@sanmarcos.edu.gt", "invited");
const carlos = user("teacher", "Carlos Ruiz", "carlos.ruiz@sanmarcos.edu.gt");

const names = ["Marta Ramírez", "Ana Lucía Méndez", "Diego Archila", "Sofía Pineda", "Luis Gómez", "Camila Torres", "Pablo Herrera", "Valeria Ortiz", "Andrés Castillo",
  "Daniela Reyes", "Javier Morales", "Paola Díaz", "Mateo Vásquez", "Isabella López", "Sebastián Cruz", "Lucía Fuentes", "Emilio Barrios", "Renata Solís", "Gabriel Aguilar",
  "Fernanda Paz", "Nicolás Rivera", "Mariana Cano", "Tomás Escobar", "Alejandra Mejía", "Rodrigo Girón", "Carolina Leal", "Samuel Orellana", "Natalia Chávez", "Hugo Estrada",
  "Julia Montenegro", "Bruno Salazar", "Elena Ponce", "Iván Coronado", "Regina Alvarado", "Oscar Villatoro", "Mónica Rosales", "Felipe Barrera", "Cecilia Marroquín"];
const students = names.map((n, i) => {
  const email = n.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase().replace(/ /g, ".") + "@sanmarcos.edu.gt";
  return user("student", n, email);
});
await db.insertInto("app_user").values([admin, ana, luis, carlos, ...students]).execute();

await db.insertInto("invitation").values({
  id: id(), school_id: schoolId, user_id: luis.id, created_by_id: admin.id, token_hash: "seed-invite-luis", expires_at: new Date(Date.now() + 7 * 864e5), created_at: now,
}).execute();

const bd2 = { id: id(), school_id: schoolId, teacher_id: ana.id, name: "Bases de Datos 2", code: "CC3057", join_code: "BD2-4K9P", tp_status: "active" as const, created_at: now, updated_at: now };
const so = { id: id(), school_id: schoolId, teacher_id: ana.id, name: "Sistemas Operativos", code: "CC3064", join_code: "SO-7M2Q", tp_status: "active" as const, created_at: now, updated_at: now };
await db.insertInto("course").values([bd2, so]).execute();
await db.insertInto("enrollment").values([
  ...students.map((s) => ({ course_id: bd2.id, student_id: s.id, school_id: schoolId, joined_at: now })),
  ...students.slice(0, 31).map((s) => ({ course_id: so.id, student_id: s.id, school_id: schoolId, joined_at: now })),
]).execute();

// ---- Eleven past lessons of BD2 ------------------------------------------------------------------
const DURATION = 50;
const lessonRows = Array.from({ length: 11 }, (_, i) => {
  const started = new Date(now.getTime() - (12 - i) * 3.5 * 864e5);
  started.setHours(8, 0, 0, 0);
  return { id: id(), school_id: schoolId, course_id: bd2.id, number: i + 1, status: "ended" as const,
    room_opened_at: new Date(started.getTime() - minutes(5)), started_at: started, ended_at: new Date(started.getTime() + minutes(DURATION)) };
});
await db.insertInto("lesson").values(lessonRows).execute();

const participants: any[] = [], pulses: any[] = [];
for (const l of lessonRows) {
  const attendance = l.number === 11 ? 36 : 33 + Math.floor(rnd() * 5);
  const present = students.slice(0, attendance);
  for (const s of present) participants.push({ lesson_id: l.id, student_id: s.id, school_id: schoolId, joined_at: new Date(l.started_at.getTime() - minutes(2)) });
  // Pulse: everyone starts "ok"; a confusing stretch (minute 28-33 in lesson 11) flips many to "lost".
  const state = new Map(present.map((s) => [s.id, "ok"]));
  for (let m = 1; m < DURATION; m++) {
    const confusing = l.number === 11 && m >= 28 && m <= 33;
    for (const s of present) {
      const flip = confusing ? (state.get(s.id) === "ok" ? 0.30 : 0.05) : (state.get(s.id) === "lost" ? 0.35 : 0.015);
      if (rnd() < flip) {
        const next = state.get(s.id) === "ok" ? "lost" : "ok";
        state.set(s.id, next);
        pulses.push({ id: id(), school_id: schoolId, lesson_id: l.id, student_id: s.id, state: next, created_at: new Date(l.started_at.getTime() + minutes(m) + Math.floor(rnd() * 50_000)) });
      }
    }
  }
}
for (let i = 0; i < participants.length; i += 1000) await db.insertInto("lesson_participant").values(participants.slice(i, i + 1000)).execute();
for (let i = 0; i < pulses.length; i += 1000) await db.insertInto("pulse_event").values(pulses.slice(i, i + 1000)).execute();

// Attendance rounds with a few borderline cases in lesson 11.
for (const l of lessonRows) {
  const round = { id: id(), school_id: schoolId, lesson_id: l.id, status: "closed" as const, qr_secret: "seed", qr_step_seconds: 8, room_lat: "14.634900", room_lng: "-90.506900", radius_m: 50, opened_at: l.started_at, closed_at: new Date(l.started_at.getTime() + minutes(5)) };
  await db.insertInto("attendance_round").values(round).execute();
  const rows = participants.filter((p) => p.lesson_id === l.id).map((p, i) => ({
    id: id(), school_id: schoolId, round_id: round.id, student_id: p.student_id,
    status: (l.number === 11 && i === 4 ? "needs_review" : "present") as "needs_review" | "present", review_reason: l.number === 11 && i === 4 ? ("too_far" as const) : null,
    scanned_at: new Date(l.started_at.getTime() + minutes(2)), distance_m: l.number === 11 && i === 4 ? 120 : 8,
  }));
  await db.insertInto("attendance_record").values(rows).execute();
}

// ---- Doubt round in lesson 11 (the three topics from the design) --------------------------------------
const L11 = lessonRows[10];
const round = { id: id(), school_id: schoolId, lesson_id: L11.id, status: "closed" as const, summary_status: "ready" as const, opened_at: new Date(L11.started_at.getTime() + minutes(31)), closes_at: new Date(L11.started_at.getTime() + minutes(31) + 30_000), closed_at: new Date(L11.started_at.getTime() + minutes(31) + 30_000) };
await db.insertInto("doubt_round").values(round).execute();
const groups = [
  { id: id(), rank: 1, title: "¿Por qué un índice puede hacer más lentas las escrituras?", slide_number: 22, sample_quote: "si indexo todo, ¿no se vuelve lento el INSERT?", n: 14 },
  { id: id(), rank: 2, title: "¿Cuándo el optimizador ignora un índice y hace un escaneo completo?", slide_number: 27, sample_quote: "a veces EXPLAIN dice Seq Scan aunque haya índice", n: 10 },
  { id: id(), rank: 3, title: "¿Qué diferencia hay entre un índice B-tree y uno hash?", slide_number: 18, sample_quote: "¿el hash sirve para búsquedas por rango?", n: 7 },
];
await db.insertInto("doubt_group").values(groups.map(({ n, ...g }) => ({ ...g, school_id: schoolId, round_id: round.id }))).execute();
let k = 0;
const doubts: any[] = [];
for (const g of groups) for (let j = 0; j < g.n; j++) {
  doubts.push({ id: id(), school_id: schoolId, round_id: round.id, student_id: students[k++].id, group_id: g.id, text: j === 0 ? g.sample_quote : `${g.sample_quote} (variante ${j})`, created_at: new Date(round.opened_at.getTime() + 10_000 + j * 700) });
}
await db.insertInto("doubt").values(doubts).execute();

// ---- Quiz in lesson 11: 14 of 29 correct (48%) --------------------------------------------------------------
const quiz = { id: id(), school_id: schoolId, lesson_id: L11.id, created_by_id: ana.id, slide_number: 22,
  question: "¿Qué ocurre con el costo de un INSERT cuando una tabla tiene muchos índices?",
  options: JSON.stringify([{ label: "A", text: "Disminuye: hay más rutas de acceso" }, { label: "B", text: "Se mantiene igual" }, { label: "C", text: "Aumenta: cada índice también se actualiza" }, { label: "D", text: "Solo aumenta con índices hash" }]),
  correct_label: "C", rationale: "14 estudiantes dejaron dudas sobre escrituras e índices.", time_limit_seconds: 45, status: "closed" as const,
  created_at: new Date(L11.started_at.getTime() + minutes(36)), launched_at: new Date(L11.started_at.getTime() + minutes(37)), closes_at: new Date(L11.started_at.getTime() + minutes(37) + 45_000), closed_at: new Date(L11.started_at.getTime() + minutes(38)) };
await db.insertInto("quiz").values(quiz as any).execute();
const dist = [..."A".repeat(3), ..."B".repeat(6), ..."C".repeat(14), ..."D".repeat(6)];
await db.insertInto("quiz_answer").values(dist.map((label, i) => ({ quiz_id: quiz.id, student_id: students[i].id, school_id: schoolId, option_label: label, answered_at: new Date(quiz.launched_at.getTime() + 5000 + i * 900) }))).execute();

// ---- Floor round in lesson 11 -------------------------------------------------------------------------------
const floor = { id: id(), school_id: schoolId, lesson_id: L11.id, prompt: "¿Qué información devuelve EXPLAIN ANALYZE que EXPLAIN solo no da?", status: "closed" as const, open_to_all: false, opened_at: new Date(L11.started_at.getTime() + minutes(42)), closed_at: new Date(L11.started_at.getTime() + minutes(46)) };
await db.insertInto("floor_round").values(floor).execute();
await db.insertInto("floor_turn").values(students.slice(0, 4).map((s, i) => ({
  id: id(), school_id: schoolId, round_id: floor.id, student_id: s.id, state: (i === 0 ? "correct" : i === 1 ? "incorrect" : "withdrawn") as "correct" | "incorrect" | "withdrawn",
  raised_at: new Date(floor.opened_at.getTime() + 800 + i * 500), resolved_at: new Date(floor.opened_at.getTime() + 20_000 + i * 20_000),
}))).execute();

// ---- Material: the PDF the quiz / doubt grouping are built from ----------------------------------------------
const file = { id: id(), school_id: schoolId, content_type: "application/pdf", size_bytes: 1, storage_key: "seed/Clase12-Indices.pdf", status: "uploaded" as const, created_at: now, uploaded_at: now };
await db.insertInto("file").values(file).execute();
await db.insertInto("lesson_material").values({ id: id(), school_id: schoolId, lesson_id: L11.id, file_id: file.id, filename: "Clase12-Indices.pdf", created_at: now, extracted_text: [
  "Diapositiva 18. Índices B-tree vs hash. Un B-tree mantiene las claves ordenadas, por eso sirve para igualdad y para rangos (<, >, BETWEEN) y para ORDER BY. Un índice hash solo resuelve igualdad exacta: no sirve para rangos.",
  "Diapositiva 22. Costo de escritura. Cada índice de una tabla debe actualizarse en cada INSERT, UPDATE de columnas indexadas y DELETE. Más índices aceleran lecturas pero hacen más lentas las escrituras y ocupan más espacio.",
  "Diapositiva 27. Cuándo el optimizador ignora un índice. Con baja selectividad (la consulta devuelve una gran parte de la tabla), con estadísticas desactualizadas, o si la columna se usa dentro de una función, el planificador prefiere un Seq Scan. EXPLAIN muestra el plan; EXPLAIN ANALYZE ejecuta la consulta y agrega tiempos y filas reales.",
].join("\n\n") }).execute();

// ---- Two more courses, so several people/agents can run live lessons at the same time (one live lesson per course) ----
const extra = [
  { name: "Redes de Datos", code: "CC3071", join: "RED-3N8X", slides: ["Diapositiva 3. Modelo OSI. Siete capas: física, enlace, red, transporte, sesión, presentación y aplicación. Cada capa se comunica con su par y presta servicio a la superior.", "Diapositiva 7. TCP vs UDP. TCP ofrece entrega confiable y ordenada mediante confirmaciones y retransmisión; UDP no garantiza entrega pero tiene menor latencia, por eso se usa en video y juegos.", "Diapositiva 12. Direccionamiento IP. Una máscara de subred separa la parte de red de la parte de host; /24 equivale a 255.255.255.0 y permite 254 hosts."] },
  { name: "Compiladores", code: "CC3081", join: "COM-5T9W", slides: ["Diapositiva 4. Análisis léxico. El analizador léxico convierte el texto fuente en tokens usando expresiones regulares y autómatas finitos.", "Diapositiva 9. Gramáticas libres de contexto. Una gramática describe la sintaxis; la ambigüedad aparece cuando una cadena tiene dos árboles de derivación distintos.", "Diapositiva 15. Optimización. La propagación de constantes y la eliminación de código muerto reducen el tamaño y el tiempo de ejecución sin cambiar el resultado."] },
  { name: "Ingeniería de Software", code: "CC3091", join: "ISW-6P4Z", slides: ["Diapositiva 2. Metodologías ágiles. Scrum organiza el trabajo en sprints cortos con roles definidos: Product Owner, Scrum Master y equipo de desarrollo.", "Diapositiva 6. Pruebas. Las pruebas unitarias verifican una función aislada; las de integración verifican que varios módulos funcionen juntos; las end-to-end recorren el sistema completo.", "Diapositiva 10. Deuda técnica. Tomar atajos acelera hoy pero cobra intereses después: cada atajo hace más caro el siguiente cambio."] },
  { name: "Inteligencia Artificial", code: "CC3101", join: "IAR-8Q2V", slides: ["Diapositiva 5. Aprendizaje supervisado. El modelo aprende de ejemplos etiquetados; la clasificación predice categorías y la regresión predice valores continuos.", "Diapositiva 8. Sobreajuste. Un modelo que memoriza los datos de entrenamiento falla con datos nuevos; se combate con más datos, regularización y validación cruzada.", "Diapositiva 13. Redes neuronales. Capas de neuronas aplican pesos y funciones de activación; el entrenamiento ajusta los pesos con descenso de gradiente."] },
];
for (const x of extra) {
  const c = { id: id(), school_id: schoolId, teacher_id: ana.id, name: x.name, code: x.code, join_code: x.join, tp_status: "active" as const, created_at: now, updated_at: now };
  await db.insertInto("course").values(c).execute();
  await db.insertInto("enrollment").values(students.slice(0, 20).map((s) => ({ course_id: c.id, student_id: s.id, school_id: schoolId, joined_at: now }))).execute();
  const started = new Date(now.getTime() - 5 * 864e5); started.setHours(8, 0, 0, 0);
  const l = { id: id(), school_id: schoolId, course_id: c.id, number: 1, status: "ended" as const, room_opened_at: new Date(started.getTime() - minutes(5)), started_at: started, ended_at: new Date(started.getTime() + minutes(50)) };
  await db.insertInto("lesson").values(l).execute();
  const f = { id: id(), school_id: schoolId, content_type: "application/pdf", size_bytes: 1, storage_key: `seed/${x.code}.pdf`, status: "uploaded" as const, created_at: now, uploaded_at: now };
  await db.insertInto("file").values(f).execute();
  await db.insertInto("lesson_material").values({ id: id(), school_id: schoolId, lesson_id: l.id, file_id: f.id, filename: `${x.code}-clase1.pdf`, created_at: now, extracted_text: x.slides.join("\n\n") }).execute();
}

await db.insertInto("student_note").values({ id: id(), school_id: schoolId, course_id: bd2.id, student_id: students[1].id, body: "Falta a clases por trabajo. Entrega siempre completa; en persona sí pregunta.", updated_at: now }).execute();

console.log(`seeded: school ${schoolId}`);
console.log(`  teacher  ana.morales@sanmarcos.edu.gt / ${PASSWORD}`);
console.log(`  student  marta.ramirez@sanmarcos.edu.gt / ${PASSWORD}  (+37 more)`);
console.log(`  admin    admin@sanmarcos.edu.gt / ${PASSWORD}`);
console.log(`  courses  BD2-4K9P (Bases de Datos 2), SO-7M2Q (Sistemas Operativos), RED-3N8X (Redes de Datos), COM-5T9W (Compiladores), ISW-6P4Z (Ingeniería de Software), IAR-8Q2V (Inteligencia Artificial)`);
await db.destroy();
