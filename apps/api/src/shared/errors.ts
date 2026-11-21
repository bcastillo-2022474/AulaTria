import { Code, ConnectError } from "@connectrpc/connect";
import { errors } from "@aula/rpc";

const { ErrorCode } = errors;
export { ErrorCode };

/**
 * A domain refusal. Chassis' rule: the code is SPECIFIC (COURSE_ALREADY_ENROLLED, not ALREADY_EXISTS).
 * Throw it from anywhere; the error interceptor is the only place that turns it into a Connect error.
 */
export class AppError extends Error {
  constructor(readonly code: errors.ErrorCode, message?: string) {
    super(message ?? errors.ErrorCode[code]);
  }
}

/** What kind of failure each code is. A code with no row here is reported as `internal`, with no message. */
const codeToConnect: Partial<Record<errors.ErrorCode, Code>> = {
  [ErrorCode.MALFORMED_ARGUMENT]: Code.InvalidArgument,
  [ErrorCode.FORBIDDEN]: Code.PermissionDenied,
  [ErrorCode.TENANT_MISMATCH]: Code.PermissionDenied,
  [ErrorCode.UNAUTHENTICATED]: Code.Unauthenticated,
  [ErrorCode.TOKEN_EXPIRED]: Code.Unauthenticated,
  [ErrorCode.NOT_FOUND]: Code.NotFound,
  [ErrorCode.USER_INVALID_CREDENTIALS]: Code.Unauthenticated,
  [ErrorCode.USER_EMAIL_TAKEN]: Code.AlreadyExists,
  [ErrorCode.INVITATION_INVALID]: Code.NotFound,
  [ErrorCode.COURSE_NOT_FOUND]: Code.NotFound,
  [ErrorCode.COURSE_JOIN_CODE_NOT_FOUND]: Code.NotFound,
  [ErrorCode.COURSE_ALREADY_ENROLLED]: Code.AlreadyExists,
  [ErrorCode.COURSE_NOT_ENROLLED]: Code.PermissionDenied,
  [ErrorCode.LESSON_NOT_FOUND]: Code.NotFound,
  [ErrorCode.LESSON_ALREADY_ACTIVE]: Code.AlreadyExists,
  [ErrorCode.LESSON_NOT_IN_LOBBY]: Code.FailedPrecondition,
  [ErrorCode.LESSON_NOT_LIVE]: Code.FailedPrecondition,
  [ErrorCode.MATERIAL_INVALID]: Code.InvalidArgument,
  [ErrorCode.DOUBT_ROUND_NOT_FOUND]: Code.NotFound,
  [ErrorCode.DOUBT_ROUND_ALREADY_OPEN]: Code.AlreadyExists,
  [ErrorCode.DOUBT_ROUND_CLOSED]: Code.FailedPrecondition,
  [ErrorCode.FLOOR_ROUND_NOT_FOUND]: Code.NotFound,
  [ErrorCode.FLOOR_ROUND_ALREADY_OPEN]: Code.AlreadyExists,
  [ErrorCode.FLOOR_ROUND_CLOSED]: Code.FailedPrecondition,
  [ErrorCode.FLOOR_NOT_YOUR_TURN]: Code.FailedPrecondition,
  [ErrorCode.QUIZ_NOT_FOUND]: Code.NotFound,
  [ErrorCode.QUIZ_ALREADY_LIVE]: Code.AlreadyExists,
  [ErrorCode.QUIZ_NOT_DRAFT]: Code.FailedPrecondition,
  [ErrorCode.QUIZ_CLOSED]: Code.FailedPrecondition,
  [ErrorCode.QUIZ_ALREADY_ANSWERED]: Code.AlreadyExists,
  [ErrorCode.QUIZ_NO_MATERIAL]: Code.FailedPrecondition,
  [ErrorCode.QUIZ_NOT_LIVE]: Code.FailedPrecondition,
  [ErrorCode.ATTENDANCE_ROUND_NOT_FOUND]: Code.NotFound,
  [ErrorCode.ATTENDANCE_ROUND_ALREADY_OPEN]: Code.AlreadyExists,
  [ErrorCode.ATTENDANCE_ROUND_CLOSED]: Code.FailedPrecondition,
  [ErrorCode.ATTENDANCE_QR_INVALID]: Code.InvalidArgument,
};

/** Carries the domain code on the response itself, so a client reads it without opening the body. */
export const ERROR_CODE_HEADER = "Aula-Error-Code";

export function toConnectError(err: unknown): ConnectError {
  if (err instanceof ConnectError) return err;
  if (err instanceof AppError) {
    const kind = codeToConnect[err.code];
    if (kind !== undefined) {
      return new ConnectError(err.message, kind, { [ERROR_CODE_HEADER]: ErrorCode[err.code] });
    }
  }
  // Unknown, or a code nobody mapped: never leak the message.
  return new ConnectError("internal error", Code.Internal);
}

/** The body of every RPC that has a contract but no implementation yet. Contributors replace calls to this. */
export function notImplemented(rpc: string): never {
  throw new ConnectError(`${rpc} is not implemented yet`, Code.Unimplemented);
}
