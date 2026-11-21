# AulaTria: Flows and screen-to-RPC map

Source: [`design/`](./design/README.md), the 42 artboards (22 teacher and admin, 20 student), and the section titles in `design/canvas.json`. Serve the folder with `python3 -m http.server` to view them. UI text is Spanish; this document is English. RPC names are the proto names from [CONTRACT.md](./CONTRACT.md). Whether each RPC is implemented or a stub is in [CONTRACT.md section 4](./CONTRACT.md#4-rpc-catalogue).

**Built screens:** only the web login (`apps/web/src/features/auth/LoginPage.tsx`) exists in code. Every other screen below is designed and mapped, not built.

## Roles and surfaces

| Role | Device | Surface |
|---|---|---|
| Admin | Laptop, 1280x800 | Stage (deep purple). Sets a password from an invitation link, invites teachers |
| Teacher ("catedrático") | Laptop, 1280x800 | Stage (deep purple). All 18 teacher screens. Owns courses, opens rooms, runs interactions, reviews the class |
| Student | Phone, 390x844 | Paper (light). Joins courses with a code, answers live prompts |

Web has no paper screens: every teacher and admin screen uses the stage surface. Paper is only the student app.

## Flow 0a: Onboarding (school, teachers, students)

| Screen | What happens | RPCs |
|---|---|---|
| Admin-Acceso | Opens the invitation link, sees the school name, sets a password | `GetInvitation`, `AcceptInvitation` |
| Admin-Catedraticos | List of teachers, or the empty state with "Agregar catedrático" | `ListTeachers` |
| Admin-Agregar | Name and email, then "Enviar invitación" | `InviteTeacher` |
| Admin-Lista | Teachers marked Activo or Invitación pendiente, "Copiar enlace" | `ListTeachers`, `RegenerateTeacherInvite` |
| Docente-Invitacion | The teacher sets a password and lands on the empty course list | `GetInvitation`, `AcceptInvitation` |
| Estudiante-Bienvenida | Types the course code, or "Ya tengo cuenta" | `LookupJoinCode`, `Login` |
| Estudiante-Registro | Name, email, password. Shows the course and school the code points to | `LookupJoinCode`, `RegisterStudent` |

## Flow 0b: Create a course, open a room, join

| Screen | What happens | RPCs |
|---|---|---|
| Docente-CursosVacio / Docente-Cursos | Course list, or the empty state | `ListCourses` |
| Docente-CrearCurso | Name and optional code | `CreateCourse` |
| Docente-CursoCreado | Join code and QR, with copy (data comes from `CreateCourse`) | (`CreateCourse`) |
| Docente-Curso | Join code, past lessons, "Iniciar sala" | `GetCourse`, `OpenRoom` |
| Docente-SalaAbierta | "24 de 38 se han unido"; "Empezar clase" or "Cerrar sala" | `GetLessonSnapshot` and `WatchLesson`, `StartLesson`, `CloseRoom` |
| Estudiante-Cursos | "EN VIVO AHORA" card with "Entrar"; "Unirme a un curso" | `ListCourses`, `JoinRoom` |
| Estudiante-Unirme, then Estudiante-Confirmar, then Estudiante-Unido | Types the code (camera scan is not in the MVP), "¿Es este tu curso?", joined | `LookupJoinCode`, `JoinCourse` |

## Flow 1: Doubt round (30 s)

| Screen | What happens | RPCs |
|---|---|---|
| Docente-Live, "Preguntar dudas" | Opens the round | `OpenDoubtRound` |
| Docente-DudasAbierta | Countdown, "+30 s", "Cerrar ahora", "23 de 38 enviaron" | `ExtendDoubtRound`, `CloseDoubtRound`, `GetDoubtRound`, `WatchLesson` |
| Docente-DudasResumen | Top grouped topics, "Ver las 31 dudas originales", "Otra ronda" | `GetDoubtRound`, `ListDoubts`, `OpenDoubtRound` |
| Estudiante-Home | Banner "ABIERTA AHORA" | `GetLessonSnapshot`, `WatchLesson` |
| Estudiante-Dudas, then Estudiante-DudaEnviada | Writes the doubt with a countdown; editable until the deadline | `SubmitDoubt`, `GetDoubtRound` |
| Estudiante-DudaGrupo | Round closed: "tu duda quedó en un grupo" | `GetDoubtRound` |

## Flow 2: Attendance with QR and location

| Screen | What happens | RPCs |
|---|---|---|
| Docente-Asistencia | **Two codes on screen:** the QR, which rotates every 8 s, and a short typable code, which rotates about every 30 s. Counts: present, in the room, to review | `OpenAttendanceRound`, `GetAttendanceCodes`, `GetAttendanceRound` |
| Estudiante-Escanear, then Estudiante-Verificando | Student scans the QR or types the code | `CheckIn` |
| Estudiante-Presente or Estudiante-PorRevisar | Result. "Por revisar" offers "Escanear de nuevo" | (`CheckIn` result) |
| Docente-AsistCerrada | Review list with "Aceptar" and "Ausente" | `CloseAttendanceRound`, `ResolveAttendance`, `GetAttendanceRound` |

**Location rule (decided in the contract):** the teacher's position when the round opens is the centre of the allowed area, 50 m by default.

| Student situation | Result for the student | Teacher sees |
|---|---|---|
| Inside the radius | Presente | Counted as present |
| Outside the radius | Por revisar (too far) | Review item with the distance |
| No location permission | Por revisar | Review item, reason "Sin permiso de ubicación" |
| Location too imprecise | Por revisar | Review item, reason low accuracy |
| Wrong or expired code | Error, try again | Nothing |

The distance is computed on the server. The client never decides presence.

## Flow 3: Floor ("toma de la palabra")

| Screen | What happens | RPCs |
|---|---|---|
| Docente-Live, "Dar la palabra", then Docente-Palabra | Current responder, "Acertó" or "No acertó · siguiente", queue with time offsets, "Abrir a todos" | `OpenFloorRound`, `GetFloorRound`, `ResolveTurn`, `OpenFloorToAll`, `CloseFloorRound` |
| Estudiante-Levantar | "Levantar la mano" | `RaiseHand` |
| Estudiante-Turno | Position in the queue, "Salir de la fila" | `GetFloorRound`, `LowerHand` |
| Estudiante-TuTurno, then Estudiante-ResultadoTurno | 15 s countdown, "Ya respondí" | `FinishMyTurn`, `GetFloorRound` |

## Flow 4: Live quiz

| Screen | What happens | RPCs |
|---|---|---|
| Docente-QuizArmar | Draft from the material, rationale, time per question, "Regenerar", "Lanzar quiz" | `DraftQuiz`, `RegenerateQuiz`, `UpdateQuizTime`, `LaunchQuiz` |
| Docente-QuizVivo | Countdown, "29 de 38", counts per option | `GetQuiz`, `WatchLesson`, `CloseQuiz` |
| Docente-QuizResultados | Percentage correct, counts per option, insight, "Repasar el concepto" | `GetQuiz` |
| Estudiante-Quiz, then Estudiante-QuizResultado | Answers (first answer wins), then the result | `AnswerQuiz`, `GetQuiz` |

Students never receive the correct answer or the insight while the quiz is live (role projection, see CONTRACT section 3).

## Flow 5: Class review and student follow-up

| Screen | What happens | RPCs |
|---|---|---|
| Docente-Pulso | Pulse bars per minute, quiz and doubt markers, "9 de 38 marcaron me perdí", "Repasar este tramo" | `GetLessonReview` |
| Docente-Perfil | Attendance, doubts, turns, quizzes, interactions per class, private note. Says "Tu criterio cuenta": participation is not comprehension | `GetStudentParticipation`, `SaveStudentNote` |

## During the class: student home

Estudiante-Home has the pulse buttons "Voy bien" and "Me perdí" (`SetPulse`), plus shortcuts to the attendance scan and "Dejar una duda en la diapositiva". The teacher only sees the aggregate ("Tu catedrático ve el total de la clase").

## Lesson state machine

```
(none) --OpenRoom--> lobby --StartLesson--> live --EndLesson--> ended
                       |
                       +--CloseRoom--> cancelled

live: at most one active round of each kind (doubts, floor, quiz, attendance).
EndLesson closes every round that is still open.
```

Students may enter a room while it is in the lobby or live (`JoinRoom`). Rounds and pulse exist only while the lesson is live.

## Gaps found while mapping

These are in [CONTRACT.md section 8](./CONTRACT.md#8-open-questions) as well.

- **Uploading lesson material** (`UploadMaterial`): the quiz and the doubt grouping depend on it, but no screen shows it.
- **Student roster** (`ListCourseStudents`): the profile screen exists; the list that leads to it does not.
- **Recording** (the "Grabando" indicator and playback in Docente-Pulso): not in v1, and no RPC.
- **Profile actions** "Asignar actividad de reposición" and "Dar puntos extra": they imply an activity and grades model that exists nowhere else. Out of scope.
- **Quiz history:** the quiz flow has one question at a time, and there is no history screen.
