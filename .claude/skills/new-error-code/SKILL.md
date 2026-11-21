---
name: new-error-code
description: Add a domain error code to AulaTria (errors.proto + the code-to-status table). Use when a handler needs to refuse something with a specific, client-actionable reason.
---

# New error code

How errors work here (copied from Chassis): the Connect status says what KIND of failure it was; the **code** says WHICH one, in the `Aula-Error-Code` response header. Clients switch on the code, never on the message. A code with no row in the status table is reported as `internal` with no message (so a forgotten mapping fails closed, not open).

## Steps
1. **Name it specific and prefixed by area**: `COURSE_ALREADY_ENROLLED`, `QUIZ_ALREADY_ANSWERED`. Never generic (`ALREADY_EXISTS`, `INVALID`). Ask: does the client know what to do from this code alone?
2. Add it to `proto/aula/errors/v1/errors.proto` in its area block with the next free number, **append only** (never renumber or reuse a number). Comment when it is raised.
3. Add its row to `codeToConnect` in `apps/api/src/shared/errors.ts`. Choose the status by meaning: `NotFound` (missing or not yours: same answer on purpose), `AlreadyExists` (unique violated), `FailedPrecondition` (state forbids it: round closed, lesson not live), `InvalidArgument` (semantic input problem the proto rules cannot express), `PermissionDenied` (right user, wrong role/membership), `Unauthenticated`.
4. Raise it: `throw new AppError(ErrorCode.QUIZ_ALREADY_ANSWERED)`. Optional message is for logs only.
5. Document it on the RPC in the `.proto` comment (`// ALREADY_ANSWERED, QUIZ_CLOSED.`) so the client authors know.
6. `mage proto:gen` (updates TS and Kotlin enums).
7. Client handling: web `rpcErrorCode(error)` from `apps/web/src/rpc/hooks.ts`; mobile `ConnectException.domainCode` compared with the generated `ErrorCode` enum names. Show a **Spanish** message per code in the screen, never the server message.

## Never
Rely on message text; reuse a code for two meanings; leak internals (DB errors map to nothing → `internal`); put a code only in TS (it must be in the proto so Kotlin gets it).
