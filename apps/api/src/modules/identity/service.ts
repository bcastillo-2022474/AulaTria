import type { ServiceImpl } from "@connectrpc/connect";
import type { identity } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { login } from "./commands/login.js";
import { logout } from "./commands/logout.js";
import { refreshSession } from "./commands/refreshSession.js";
import { registerDevice } from "./commands/registerDevice.js";
import { registerStudent } from "./commands/registerStudent.js";
import { getMe } from "./queries/getMe.js";
import { lookupJoinCode } from "./queries/lookupJoinCode.js";
import { getInvitation } from "./queries/getInvitation.js";
import { acceptInvitation } from "./commands/acceptInvitation.js";

/**
 * RPC -> use case. Deliberately thin: this file is the ROLE TABLE of the module (who may call what) and nothing
 * else. Commands (writes) live in commands/, queries (reads) in queries/.
 */
export const identityService: Partial<ServiceImpl<typeof identity.IdentityService>> = {
  // public (no actor)
  login: (req) => login(req),
  refreshSession: (req) => refreshSession(req),
  lookupJoinCode: (req) => lookupJoinCode(req),
  registerStudent: (req) => registerStudent(req),
  // authenticated
  logout: (req, ctx) => logout(actor(ctx), req),
  getMe: (_req, ctx) => getMe(actor(ctx)),
  registerDevice: (req, ctx) => registerDevice(actor(ctx, "teacher", "student"), req),
  getInvitation: (req) => getInvitation(req),
  acceptInvitation: (req) => acceptInvitation(req),
};
