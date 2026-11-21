import { identity } from "@aula/rpc";
import { Button } from "../../design-system";
import { useAulaMutation } from "../../rpc/hooks";
import { session } from "../../rpc/session";

/** Revokes the refresh token on the server, then forgets the session here (even if the call failed). */
export function SignOutButton() {
  const logout = useAulaMutation(identity.IdentityService.method.logout, {
    onSettled: () => session.clear(),
  });
  return (
    <Button
      variant="ghost"
      size="48"
      disabled={logout.isPending}
      onClick={() => {
        const current = session.get();
        if (current) logout.mutate({ refreshToken: current.refreshToken });
      }}
    >
      Salir
    </Button>
  );
}
