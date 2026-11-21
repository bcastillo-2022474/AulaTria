import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

export type Role = "admin" | "teacher" | "student";
export type UserStatus = "invited" | "active" | "disabled";

/** A person of a school. Pure: no I/O. Rules about who they are live here, persistence lives in UserRepository. */
export class User {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly role: Role,
    readonly status: UserStatus,
    readonly name: string,
    readonly email: string,
    readonly passwordHash: string | null,
  ) {}

  /** A student creating their own account with a course join code (the school comes from the course). */
  static registerStudent(p: { schoolId: string; name: string; email: string; passwordHash: string }): User {
    const name = p.name.trim();
    const email = p.email.trim().toLowerCase();
    if (!name || name.length > 100) throw new AppError(ErrorCode.MALFORMED_ARGUMENT, "name");
    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) throw new AppError(ErrorCode.MALFORMED_ARGUMENT, "email");
    return new User(newId(), p.schoolId, "student", "active", name, email, p.passwordHash);
  }

  canSignIn(): boolean {
    return this.status === "active" && this.passwordHash !== null;
  }
}
