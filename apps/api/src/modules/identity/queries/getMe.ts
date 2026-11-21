import { identity } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";

/** READ: a specialized query, hardcoded here. No repository, no entity. */
export async function getMe(a: Actor) {
  const u = await db.selectFrom("app_user").innerJoin("school", "school.id", "app_user.school_id")
    .select(["app_user.id", "app_user.name", "app_user.email", "app_user.role", "school.id as school_id", "school.name as school_name"])
    .where("app_user.school_id", "=", a.schoolId).where("app_user.id", "=", a.userId).executeTakeFirst();
  if (!u) throw new AppError(ErrorCode.UNAUTHENTICATED);
  const role = { admin: identity.Role.ADMIN, teacher: identity.Role.TEACHER, student: identity.Role.STUDENT }[u.role];
  return { user: { id: u.id, name: u.name, email: u.email, role, school: { id: u.school_id, name: u.school_name } } };
}
