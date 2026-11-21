import type { Executor } from "../../shared/tx.js";
import { Invitation } from "./domain/invitation.js";

/**
 * WRITE-SIDE persistence of invitations. Concrete class, no interface.
 * Used by: InviteTeacher, RegenerateTeacherInvite, AcceptInvitation (and the first-admin setup).
 * The teacher's invited app_user row is created by the command in the same transaction, through UserRepository.
 */
export class InvitationRepository {
  constructor(private db: Executor) {}

  /**
   * Not tenant-scoped, on purpose: the public GetInvitation and AcceptInvitation know only the token, and the token is
   * what tells us the school. Same rule as UserRepository.findActiveByEmail. The returned entity carries its schoolId.
   */
  async findByTokenHash(tokenHash: string): Promise<Invitation | null> {
    const r = await this.db.selectFrom("invitation").selectAll().where("token_hash", "=", tokenHash).executeTakeFirst();
    return r ? toInvitation(r) : null;
  }

  async insert(inv: Invitation): Promise<void> {
    await this.db.insertInto("invitation").values({
      id: inv.id, school_id: inv.schoolId, user_id: inv.userId, created_by_id: inv.createdById, token_hash: inv.tokenHash,
      expires_at: inv.expiresAt, accepted_at: inv.acceptedAt, revoked_at: inv.revokedAt, created_at: inv.createdAt,
    }).execute();
  }

  /** Persists accepted_at / revoked_at. */
  async save(inv: Invitation): Promise<void> {
    await this.db.updateTable("invitation").set({ accepted_at: inv.acceptedAt, revoked_at: inv.revokedAt })
      .where("school_id", "=", inv.schoolId).where("id", "=", inv.id).execute();
  }

  /** Revokes every still-valid invitation of a user (RegenerateTeacherInvite revokes the previous link). */
  async revokeAllForUser(schoolId: string, userId: string, at: Date): Promise<void> {
    await this.db.updateTable("invitation").set({ revoked_at: at })
      .where("school_id", "=", schoolId).where("user_id", "=", userId).where("revoked_at", "is", null).execute();
  }
}

type InvitationRow = {
  id: string; school_id: string; user_id: string; created_by_id: string | null; token_hash: string; expires_at: Date;
  accepted_at: Date | null; revoked_at: Date | null; created_at: Date;
};
const toInvitation = (r: InvitationRow) => new Invitation(r.id, r.school_id, r.user_id, r.created_by_id, r.token_hash, r.expires_at, r.accepted_at, r.revoked_at, r.created_at);
