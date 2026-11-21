import type { ReactNode } from "react";
import { Navigate, useLocation } from "react-router";
import { SignOutButton } from "../features/auth/SignOutButton";
import { AppBar, Content, EmptyState, Screen } from "../design-system";
import { type StoredSession, useSession } from "../rpc/session";

/**
 * Route guard. Signed out goes to /login. Signed in with another role shows a "no tienes acceso" card.
 *
 *   { path: "/courses", element: <RequireAuth roles={["teacher"]}><CoursesPage /></RequireAuth> }
 */
export function RequireAuth({ roles, children }: { roles?: readonly StoredSession["role"][]; children: ReactNode }) {
  const current = useSession();
  const here = useLocation();
  // Signed out: go to /login but remember the page (path + query), so a refresh or a shared link lands where it should after login.
  if (!current) return <Navigate to={`/login?next=${encodeURIComponent(here.pathname + here.search)}`} replace />;
  if (roles && !roles.includes(current.role)) {
    return (
      <Screen>
        <AppBar right={<SignOutButton />} />
        <Content className="items-center justify-center">
          <EmptyState title="No tienes acceso" body="Tu cuenta no puede abrir esta pantalla." />
        </Content>
      </Screen>
    );
  }
  return <>{children}</>;
}
