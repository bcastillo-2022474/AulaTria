import type { identity } from "@aula/rpc";
import { inTx } from "../../../shared/tx.js";
import { AppError, ErrorCode } from "../../../shared/errors.js";
import { RefreshTokenRepository, UserRepository } from "../repositories.js";
import { issueSession } from "../session.js";

export async function refreshSession(req: identity.RefreshSessionRequest) {
  return inTx(async (trx) => {
    const owner = await new RefreshTokenRepository(trx).consume(req.refreshToken);
    const user = owner && (await new UserRepository(trx).findById(owner.schoolId, owner.userId));
    if (!user?.canSignIn()) throw new AppError(ErrorCode.UNAUTHENTICATED);
    return { session: await issueSession(user, trx) };
  });
}
