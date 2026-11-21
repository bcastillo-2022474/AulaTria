import { identity } from "@aula/rpc";
import { db } from "../../db/index.js";
import { signAccessToken } from "../../shared/context.js";
import type { Executor } from "../../shared/tx.js";
import type { User } from "./domain/user.js";
import { RefreshTokenRepository } from "./repositories.js";

const roleOf = (r: User["role"]) => ({ admin: identity.Role.ADMIN, teacher: identity.Role.TEACHER, student: identity.Role.STUDENT })[r];

/** The Session message returned by every sign-in path: a new access JWT + a freshly issued refresh token. */
export async function issueSession(user: User, executor: Executor = db) {
  const refreshToken = await new RefreshTokenRepository(executor).issue(user);
  // The school name is part of the response DTO: a one-row read, hardcoded here like any query.
  const school = await executor.selectFrom("school").select("name").where("id", "=", user.schoolId).executeTakeFirstOrThrow();
  return {
    accessToken: signAccessToken({ userId: user.id, schoolId: user.schoolId, role: user.role }),
    refreshToken,
    user: { id: user.id, name: user.name, email: user.email, role: roleOf(user.role), school: { id: user.schoolId, name: school.name } },
  };
}
