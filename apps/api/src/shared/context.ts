import { ConnectError, createContextKey, type HandlerContext, type Interceptor } from "@connectrpc/connect";
import jwt from "jsonwebtoken";
import { identity } from "@aula/rpc";
import { AppError, ErrorCode, toConnectError } from "./errors.js";

export type RoleName = "admin" | "teacher" | "student";
/** Who is calling, and for which school. Every query takes `schoolId`: that is the tenant filter. */
export interface Actor { userId: string; schoolId: string; role: RoleName }

const actorKey = createContextKey<Actor | undefined>(undefined);
const SECRET = process.env.JWT_SECRET ?? "dev-secret";

export const signAccessToken = (a: Actor) => jwt.sign({ sid: a.schoolId, role: a.role }, SECRET, { subject: a.userId, expiresIn: "12h" });

/** Outermost: logs the raw error, then maps it once (Chassis' LoggingInterceptor). */
export const errorInterceptor: Interceptor = (next) => async (req) => {
  const started = Date.now();
  const where = `${req.service.typeName}/${req.method.name}`;
  try {
    const res = await next(req);
    if (res.stream) {
      const messages = res.message as AsyncIterable<unknown>;
      return { ...res, message: (async function* () {
        try { yield* messages; } catch (e) { logFailure(where, started, e); throw toConnectError(e); }
      })() } as typeof res;
    }
    console.log(`rpc ok ${where} ${Date.now() - started}ms`);
    return res;
  } catch (e) {
    logFailure(where, started, e);
    throw toConnectError(e);
  }
};
function logFailure(where: string, started: number, e: unknown) {
  if (e instanceof AppError) console.log(`rpc refused ${where} ${errorName(e)} ${Date.now() - started}ms`);
  else console.error(`rpc failed ${where}`, e);
}
const errorName = (e: AppError) => e.message;

/**
 * Reads Authorization + X-School-Id. Public RPCs send neither and still work: they never ask for the
 * actor. With a token, the tenant header MUST match the school inside it.
 */
export const authInterceptor: Interceptor = (next) => async (req) => {
  const header = req.header.get("authorization");
  if (header?.startsWith("Bearer ")) {
    let claims: jwt.JwtPayload;
    try {
      claims = jwt.verify(header.slice(7), SECRET) as jwt.JwtPayload;
    } catch (e) {
      throw new AppError(e instanceof jwt.TokenExpiredError ? ErrorCode.TOKEN_EXPIRED : ErrorCode.UNAUTHENTICATED);
    }
    const tenant = req.header.get("x-school-id");
    if (tenant !== claims.sid) throw new AppError(ErrorCode.TENANT_MISMATCH, "X-School-Id does not match the token");
    req.contextValues.set(actorKey, { userId: claims.sub!, schoolId: claims.sid, role: claims.role });
  }
  return next(req);
};

/** First line of every non-public handler: who may call it. `actor(ctx, "teacher")`. */
export function actor(ctx: HandlerContext, ...roles: RoleName[]): Actor {
  const a = ctx.values.get(actorKey);
  if (!a) throw new AppError(ErrorCode.UNAUTHENTICATED);
  if (roles.length && !roles.includes(a.role)) throw new AppError(ErrorCode.FORBIDDEN);
  return a;
}

export const toRole = (r: RoleName) =>
  ({ admin: identity.Role.ADMIN, teacher: identity.Role.TEACHER, student: identity.Role.STUDENT })[r];
export { ConnectError };
