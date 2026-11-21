import { createClient } from "@connectrpc/connect";
import { createConnectTransport } from "@connectrpc/connect-web";
import { identity } from "@aula/rpc";
import { API_URL } from "./config";

/** Revokes a session that was never stored (the wrong app for that role). Best effort: a failure only leaves it to expire. */
export async function revokeSession(s: { accessToken: string; refreshToken: string; schoolId: string }) {
  const transport = createConnectTransport({
    baseUrl: API_URL,
    interceptors: [(next) => (req) => {
      req.header.set("Authorization", `Bearer ${s.accessToken}`);
      req.header.set("X-School-Id", s.schoolId);
      return next(req);
    }],
  });
  try { await createClient(identity.IdentityService, transport).logout({ refreshToken: s.refreshToken }); } catch { /* ignore */ }
}
