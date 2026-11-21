import type { identity } from "@aula/rpc";
import { db } from "../../../db/index.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { verifyPassword } from "../passwords.js";
import { UserRepository } from "../repositories.js";
import { issueSession } from "../session.js";

/** public: no actor, the tenant is learned from the user. */
export async function login(req: identity.LoginRequest) {
  const user = await new UserRepository(db).findActiveByEmail(req.email);
  if (!user?.canSignIn() || !(await verifyPassword(req.password, user.passwordHash!))) {
    throw new AppError(ErrorCode.USER_INVALID_CREDENTIALS);
  }
  return { session: await issueSession(user) };
}
