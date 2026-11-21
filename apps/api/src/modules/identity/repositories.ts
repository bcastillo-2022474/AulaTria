import { createHash, randomBytes } from "node:crypto";
import { isUniqueViolation } from "../../db/index.js";
import { AppError, ErrorCode } from "../../shared/errors.js";
import { newId } from "../../shared/ids.js";
import type { Executor } from "../../shared/tx.js";
import { User, type Role, type UserStatus } from "./domain/user.js";

// WRITE-SIDE persistence for the identity module. Concrete classes, no interfaces: a repository rebuilds
// entities and saves them, nothing else. Reads (GetMe, LookupJoinCode...) do NOT come through here: they are
// specialized queries in queries/.

const sha256 = (s: string) => createHash("sha256").update(s).digest("hex");

export class UserRepository {
  constructor(private db: Executor) {}

  /** Not tenant-scoped on purpose: login happens before a tenant is known; email is globally unique. */
  async findActiveByEmail(email: string): Promise<User | null> {
    const r = await this.db.selectFrom("app_user").selectAll().where("email", "=", email.trim().toLowerCase()).where("status", "=", "active").executeTakeFirst();
    return r ? toUser(r) : null;
  }

  async findById(schoolId: string, id: string): Promise<User | null> {
    const r = await this.db.selectFrom("app_user").selectAll().where("school_id", "=", schoolId).where("id", "=", id).executeTakeFirst();
    return r ? toUser(r) : null;
  }

  async insert(u: User): Promise<void> {
    try {
      await this.db.insertInto("app_user").values({
        id: u.id, school_id: u.schoolId, role: u.role, status: u.status, name: u.name, email: u.email,
        password_hash: u.passwordHash, created_at: new Date(), updated_at: new Date(),
      }).execute();
    } catch (e) {
      if (isUniqueViolation(e)) throw new AppError(ErrorCode.USER_EMAIL_TAKEN);
      throw e;
    }
  }
}

export class RefreshTokenRepository {
  constructor(private db: Executor) {}

  /** Stores the hash of a new token and returns the raw one (shown once). */
  async issue(u: User): Promise<string> {
    const raw = randomBytes(32).toString("base64url");
    await this.db.insertInto("refresh_token").values({
      id: newId(), school_id: u.schoolId, user_id: u.id, token_hash: sha256(raw),
      created_at: new Date(), expires_at: new Date(Date.now() + 30 * 864e5),
    }).execute();
    return raw;
  }

  /** Rotation: a token can be used once. Returns who it belonged to, or null if unknown/used/expired. */
  async consume(raw: string): Promise<{ userId: string; schoolId: string } | null> {
    const row = await this.db.selectFrom("refresh_token").selectAll().where("token_hash", "=", sha256(raw)).executeTakeFirst();
    if (!row || row.revoked_at || row.expires_at < new Date()) return null;
    await this.db.updateTable("refresh_token").set({ revoked_at: new Date() }).where("id", "=", row.id).execute();
    return { userId: row.user_id, schoolId: row.school_id };
  }

  async revoke(schoolId: string, userId: string, raw: string): Promise<void> {
    await this.db.updateTable("refresh_token").set({ revoked_at: new Date() })
      .where("school_id", "=", schoolId).where("user_id", "=", userId).where("token_hash", "=", sha256(raw)).execute();
  }
}

export class DeviceRepository {
  constructor(private db: Executor) {}

  async upsert(schoolId: string, userId: string, platform: "ios" | "android", pushToken: string): Promise<void> {
    await this.db.insertInto("device").values({ id: newId(), school_id: schoolId, user_id: userId, platform, push_token: pushToken, updated_at: new Date() })
      .onConflict((oc) => oc.column("push_token").doUpdateSet({ user_id: userId, updated_at: new Date() })).execute();
  }
}

type UserRow = { id: string; school_id: string; role: Role; status: UserStatus; name: string; email: string; password_hash: string | null };
const toUser = (r: UserRow) => new User(r.id, r.school_id, r.role, r.status, r.name, r.email, r.password_hash);
