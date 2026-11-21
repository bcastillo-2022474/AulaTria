import { createBrowserRouter, Navigate } from "react-router";
import { Gallery } from "../design-system/Gallery";
import { LoginPage } from "../features/auth/LoginPage";
import { CoursePage } from "../features/courses/CoursePage";
import { CoursesPage } from "../features/courses/CoursesPage";
import { CreateCoursePage } from "../features/courses/CreateCoursePage";
import { AttendancePage } from "../features/lesson/AttendancePage";
import { DoubtsPage } from "../features/lesson/DoubtsPage";
import { FloorPage } from "../features/lesson/FloorPage";
import { LivePage } from "../features/lesson/LivePage";
import { QuizPage } from "../features/lesson/QuizPage";
import { RoomPage } from "../features/lesson/RoomPage";
import { ReviewPage } from "../features/review/ReviewPage";
import { StudentProfilePage } from "../features/review/StudentProfilePage";
import { StudentsPage } from "../features/review/StudentsPage";
import { RequireAuth } from "./RequireAuth";

/**
 * THE route table of the web app. REAL paths (/courses/123), not hashes: refresh keeps the page, links can be shared,
 * and query strings work (`useSearchParams`). In production the host must serve index.html for unknown paths
 * (see features/README.md). To add a screen: import it, add one entry in its area, wrap it in <RequireAuth>.
 */
// Served under a prefix in production (VITE_BASE=/docentes/): the router works with prefix-free paths.
const basename = import.meta.env.BASE_URL.replace(/\/+$/, "") || undefined;

export const router = createBrowserRouter([
  // Auth
  { path: "/login", element: <LoginPage /> },
  { path: "/", element: <RequireAuth><Navigate to="/courses" replace /></RequireAuth> },

  // Courses (teacher)
  { path: "/courses", element: <RequireAuth roles={["teacher"]}><CoursesPage /></RequireAuth> },
  { path: "/courses/new", element: <RequireAuth roles={["teacher"]}><CreateCoursePage /></RequireAuth> },
  { path: "/courses/:courseId", element: <RequireAuth roles={["teacher"]}><CoursePage /></RequireAuth> },

  // Live class (teacher): room (lobby) -> live dashboard -> floor / quiz
  { path: "/lessons/:lessonId/room", element: <RequireAuth roles={["teacher"]}><RoomPage /></RequireAuth> },
  { path: "/lessons/:lessonId/live", element: <RequireAuth roles={["teacher"]}><LivePage /></RequireAuth> },
  { path: "/lessons/:lessonId/doubts/:roundId", element: <RequireAuth roles={["teacher"]}><DoubtsPage /></RequireAuth> },
  { path: "/lessons/:lessonId/attendance/:roundId", element: <RequireAuth roles={["teacher"]}><AttendancePage /></RequireAuth> },
  { path: "/lessons/:lessonId/floor", element: <RequireAuth roles={["teacher"]}><FloorPage /></RequireAuth> },
  // the quiz id is in the URL once a draft exists (a draft is not in the live snapshot): /quiz starts a draft, /quiz/:quizId shows it
  { path: "/lessons/:lessonId/quiz", element: <RequireAuth roles={["teacher"]}><QuizPage /></RequireAuth> },
  { path: "/lessons/:lessonId/quiz/:quizId", element: <RequireAuth roles={["teacher"]}><QuizPage /></RequireAuth> },

  // Review and follow-up (teacher): class pulse, roster, student profile + private note
  { path: "/lessons/:lessonId/review", element: <RequireAuth roles={["teacher"]}><ReviewPage /></RequireAuth> },
  { path: "/courses/:courseId/students", element: <RequireAuth roles={["teacher"]}><StudentsPage /></RequireAuth> },
  { path: "/courses/:courseId/students/:studentId", element: <RequireAuth roles={["teacher"]}><StudentProfilePage /></RequireAuth> },

  // Design system (gallery, not behind login)
  { path: "/design-system", element: <Gallery /> },

  // ROUTES: add yours here, grouped by area (copy the Courses group above)

  { path: "*", element: <Navigate to="/" replace /> },
], { basename });
