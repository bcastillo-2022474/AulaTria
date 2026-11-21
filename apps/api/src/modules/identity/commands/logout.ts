import type { identity } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { RefreshTokenRepository } from "../repositories.js";

export async function logout(a: Actor, req: identity.LogoutRequest) {
  await new RefreshTokenRepository(db).revoke(a.schoolId, a.userId, req.refreshToken);
  return {};
}
