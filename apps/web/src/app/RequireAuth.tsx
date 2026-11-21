import type { ReactNode } from "react";
import { Navigate } from "react-router";
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
  if (!current) return <Navigate to="/login" replace />;
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
