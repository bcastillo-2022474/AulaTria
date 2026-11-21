import { useState, type FormEvent } from "react";
import { Navigate, useSearchParams } from "react-router";
import { identity } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Field, Heading, LogoSlot, Screen, Text } from "../../design-system";
import { errorMessage, useAulaMutation } from "../../rpc/hooks";
import { STUDENT_APP_URL } from "../../rpc/config";
import { session, useSession } from "../../rpc/session";
import { revokeSession } from "../../rpc/revoke";

/** Teacher/admin sign-in. Students are refused here: they use the phone app. */
export function LoginPage() {
  const current = useSession();
  const [params] = useSearchParams();
  // `?next=/courses/123` set by RequireAuth. Only same-site paths are accepted (never "//host" or a full URL).
  const requested = params.get("next");
  const next = requested && requested.startsWith("/") && !requested.startsWith("//") ? requested : "/";
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [studentBlocked, setStudentBlocked] = useState(false);

  const login = useAulaMutation(identity.IdentityService.method.login, {
    onSuccess: (res) => {
      const next = res.session;
      if (!next) return;
      if (next.user?.role === identity.Role.STUDENT) {
        // Never stored in this app: revoke the token we were just given and explain why.
        setStudentBlocked(true);
        // Logout needs an authenticated caller and no session is stored here, so call it with the token we were just given.
        void revokeSession({ accessToken: next.accessToken, refreshToken: next.refreshToken, schoolId: next.user?.school?.id ?? "" });
        return;
      }
      session.set(next); // from here on the guard below sends the user to "/"
    },
  });

  // Hooks above, early return below. Signed in (or just signed in) leaves this page.
  if (current) return <Navigate to={next} replace />;

  function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setStudentBlocked(false);
    login.mutate({ email: email.trim(), password });
  }

  return (
    <Screen>
      <AppBar brand={<LogoSlot />} left="Aula interactiva" />
      <Content className="items-center justify-center">
        <form onSubmit={submit} className="flex w-[460px] max-w-full flex-col gap-[22px]">
          <Heading size={44}>Iniciar sesión</Heading>
          <Text as="p" size={16}>Usa el correo y la contraseña de tu colegio.</Text>
          <Field
            label="Correo" type="email" autoComplete="username" required
            value={email} onChange={(e) => setEmail(e.target.value)}
          />
          <Field
            label="Contraseña" type="password" autoComplete="current-password" required
            value={password} onChange={(e) => setPassword(e.target.value)}
          />
          {login.error ? <Banner tone="danger">{errorMessage(login.error)}</Banner> : null}
          {studentBlocked ? (
            <div className="flex flex-col gap-3">
              <Banner tone="warn">
                Esta cuenta es de estudiante. Esta web es para docentes y administradores.
              </Banner>
              {/* The right door, not a dead end: the student app (also runs in the browser). */}
              <Button href={STUDENT_APP_URL} variant="primary" size="60" block>Ir a la app de estudiantes</Button>
            </div>
          ) : null}
          <Button type="submit" size="60" block disabled={login.isPending}>
            {login.isPending ? "Entrando…" : "Entrar"}
          </Button>
        </form>
      </Content>
    </Screen>
  );
}
