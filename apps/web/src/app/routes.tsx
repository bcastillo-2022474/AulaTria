import { createHashRouter, Navigate } from "react-router";
import { Gallery } from "../design-system/Gallery";
import { LoginPage } from "../features/auth/LoginPage";
import { CoursePage } from "../features/courses/CoursePage";
import { CoursesPage } from "../features/courses/CoursesPage";
import { CreateCoursePage } from "../features/courses/CreateCoursePage";
import { RequireAuth } from "./RequireAuth";

/**
 * THE route table of the web app. Hash router (links are "#/path", e.g. Button or CourseTile `href`), so it
 * runs from any static host. To add a screen: import it, add one entry in its area, wrap it in <RequireAuth>.
 */
export const router = createHashRouter([
  // Auth
  { path: "/login", element: <LoginPage /> },
  { path: "/", element: <RequireAuth><Navigate to="/courses" replace /></RequireAuth> },

  // Courses (teacher)
  { path: "/courses", element: <RequireAuth roles={["teacher"]}><CoursesPage /></RequireAuth> },
  { path: "/courses/new", element: <RequireAuth roles={["teacher"]}><CreateCoursePage /></RequireAuth> },
  { path: "/courses/:courseId", element: <RequireAuth roles={["teacher"]}><CoursePage /></RequireAuth> },

  // Design system (gallery, not behind login)
  { path: "/design-system", element: <Gallery /> },

  // ROUTES: add yours here, grouped by area (copy the Courses group above)

  { path: "*", element: <Navigate to="/" replace /> },
]);
