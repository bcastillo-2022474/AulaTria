import { createHash, randomBytes } from "node:crypto";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

/**
 * Used by InviteTeacher, RegenerateTeacherInvite, AcceptInvitation. An invitation link ("Copiar enlace"): how an admin or a teacher gets their first access. Only the hash of the
 * token is stored; the raw token is returned once, by issue(). Pure: no I/O.
 *
 *   issued --accept--> accepted
 *   issued --revoke--> revoked   (regenerating the link revokes the previous one)
 */
export class Invitation {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly userId: string,
    readonly createdById: string | null,
    readonly tokenHash: string,
    readonly expiresAt: Date,
    private _acceptedAt: Date | null,
    private _revokedAt: Date | null,
    readonly createdAt: Date,
  ) {}

  /**
   * Issues an invitation for an (invited) user. Returns the entity and the raw token, which must be shown once.
   * TODO(contributor): the lifetime (expiresAt) is chosen by the command; the product has not set it yet.
   */
  static issue(p: { schoolId: string; userId: string; createdById: string | null; expiresAt: Date; now: Date }): { invitation: Invitation; rawToken: string } {
    const rawToken = randomBytes(32).toString("base64url");
    const invitation = new Invitation(newId(), p.schoolId, p.userId, p.createdById, Invitation.hashToken(rawToken), p.expiresAt, null, null, p.now);
    return { invitation, rawToken };
  }

  /** Stored and looked up by hash. The same function is used on both sides. */
  static hashToken(raw: string): string {
    return createHash("sha256").update(raw).digest("hex");
  }

  get acceptedAt() { return this._acceptedAt; }
  get revokedAt() { return this._revokedAt; }

  /** Expired, used or revoked: all three are the same answer on purpose (INVITATION_INVALID). */
  assertUsable(now: Date): void {
    if (this._revokedAt || this._acceptedAt || this.expiresAt.getTime() <= now.getTime()) throw new AppError(ErrorCode.INVITATION_INVALID);
  }

  /** AcceptInvitation: marks it used. The user's activation (password set, status active) is the command's job. */
  accept(now: Date): void {
    this.assertUsable(now);
    this._acceptedAt = now;
  }

  revoke(now: Date): void {
    this._revokedAt = this._revokedAt ?? now;
  }
}
