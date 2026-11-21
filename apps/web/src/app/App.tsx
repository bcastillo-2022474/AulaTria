import { RouterProvider } from "react-router";
import { router } from "./routes";

/**
 * The app is one full-viewport box. Screens use `Screen` (min-h-full), which fills it, so vertically
 * centred layouts work and the stage background covers the whole window.
 */
export function App() {
  return (
    <div className="h-dvh">
      <RouterProvider router={router} />
    </div>
  );
}
