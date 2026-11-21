import { Code, ConnectError, createClient, type Interceptor } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-web";
import { TransportProvider } from "@connectrpc/connect-query";
import { MutationCache, QueryCache, QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { identity } from "@aula/rpc";
import { API_URL } from "./config";
import { session } from "./session";

/**
 * The HTTP/RPC plumbing, written ONCE. Contributors never touch this file; they use the hooks in ./hooks.ts.
 *   - every authenticated call carries Authorization + X-School-Id (the tenant),
 *   - on TOKEN_EXPIRED the session is refreshed ONCE (single flight, even if ten calls fail together) and the call retried,
 *   - if the session cannot be recovered the user is signed out and the router sends them to /login.
 */
const rawTransport = createConnectTransport({ baseUrl: API_URL });

let refreshing: Promise<boolean> | null = null;
function refreshSession(): Promise<boolean> {
  refreshing ??= (async () => {
    const s = session.get();
    if (!s) return false;
    try {
      const res = await createClient(identity.IdentityService, rawTransport).refreshSession({ refreshToken: s.refreshToken });
      session.set(res.session!);
      return true;
    } catch {
      session.clear();
      return false;
    } finally {
      refreshing = null;
    }
  })();
  return refreshing;
}

const code = (e: unknown) => ConnectError.from(e).metadata.get("Aula-Error-Code");

const auth: Interceptor = (next) => async (req) => {
  const attach = () => {
    const s = session.get();
    if (s) { req.header.set("Authorization", `Bearer ${s.accessToken}`); req.header.set("X-School-Id", s.schoolId); }
  };
  attach();
  try {
    return await next(req);
  } catch (e) {
    if (code(e) === "TOKEN_EXPIRED" && (await refreshSession())) { attach(); return next(req); }
    throw e;
  }
};

const transport = createConnectTransport({ baseUrl: API_URL, interceptors: [auth] });

/** Anything that proves the session is dead signs the user out, whichever screen noticed first. */
const onGlobalError = (e: unknown) => {
  if (ConnectError.from(e).code === Code.Unauthenticated && code(e) !== "USER_INVALID_CREDENTIALS") session.clear();
};

const queryClient = new QueryClient({
  queryCache: new QueryCache({ onError: onGlobalError }),
  mutationCache: new MutationCache({ onError: onGlobalError }),
  defaultOptions: { queries: { staleTime: 10_000, retry: (n, e) => n < 1 && ConnectError.from(e).code === Code.Unavailable } },
});

export function RpcProvider({ children }: { children: React.ReactNode }) {
  return (
    <TransportProvider transport={transport}>
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    </TransportProvider>
  );
}
