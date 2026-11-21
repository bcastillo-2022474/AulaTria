import { Code, ConnectError } from "@connectrpc/connect";
import { errors } from "@aula/rpc";

type Name = Exclude<keyof typeof errors.ErrorCode, "ERROR_CODE_UNSPECIFIED">;

/**
 * What the user reads for each domain error code. EXHAUSTIVE on purpose: add a code to errors.proto and this file
 * stops compiling until you write its Spanish message. Never show the server's `message`.
 */
const messages: Record<Name, string> = {
  MALFORMED_ARGUMENT: "Revisa los datos e inténtalo de nuevo.",
  FORBIDDEN: "No tienes permiso para hacer esto.",
  UNAUTHENTICATED: "Tu sesión terminó. Inicia sesión otra vez.",
  TOKEN_EXPIRED: "Tu sesión terminó. Inicia sesión otra vez.",
  TENANT_MISMATCH: "Tu sesión no corresponde a este colegio. Inicia sesión otra vez.",
  NOT_FOUND: "No encontramos lo que buscas.",
  USER_INVALID_CREDENTIALS: "Correo o contraseña incorrectos.",
  USER_EMAIL_TAKEN: "Ya existe una cuenta con ese correo.",
  INVITATION_INVALID: "Esta invitación ya no es válida. Pide una nueva.",
  COURSE_NOT_FOUND: "No encontramos ese curso.",
  COURSE_JOIN_CODE_NOT_FOUND: "No encontramos un curso con ese código.",
  COURSE_ALREADY_ENROLLED: "Ya estás en este curso.",
  COURSE_NOT_ENROLLED: "No estás inscrito en este curso.",
  LESSON_NOT_FOUND: "No encontramos esa clase.",
  LESSON_ALREADY_ACTIVE: "Este curso ya tiene una sala abierta.",
  LESSON_NOT_IN_LOBBY: "La sala ya no está abierta.",
  LESSON_NOT_LIVE: "La clase no está en curso.",
  MATERIAL_INVALID: "No pudimos leer ese archivo.",
  DOUBT_ROUND_NOT_FOUND: "No encontramos esa ronda de dudas.",
  DOUBT_ROUND_ALREADY_OPEN: "Ya hay una ronda de dudas abierta.",
  DOUBT_ROUND_CLOSED: "La ronda de dudas ya cerró.",
  FLOOR_ROUND_NOT_FOUND: "No encontramos esa ronda.",
  FLOOR_ROUND_ALREADY_OPEN: "Ya hay una ronda de palabra abierta.",
  FLOOR_ROUND_CLOSED: "La ronda de palabra ya cerró.",
  FLOOR_NOT_YOUR_TURN: "Todavía no es tu turno.",
  QUIZ_NOT_FOUND: "No encontramos ese quiz.",
  QUIZ_ALREADY_LIVE: "Ya hay un quiz en vivo.",
  QUIZ_NOT_DRAFT: "Este quiz ya se lanzó.",
  QUIZ_CLOSED: "El quiz ya cerró.",
  QUIZ_ALREADY_ANSWERED: "Ya respondiste este quiz.",
  QUIZ_NO_MATERIAL: "Sube el material de la clase para armar el quiz.",
  QUIZ_NOT_LIVE: "Este quiz todavía no está en vivo.",
  ATTENDANCE_ROUND_NOT_FOUND: "No encontramos esa asistencia.",
  ATTENDANCE_ROUND_ALREADY_OPEN: "Ya hay una asistencia abierta.",
  ATTENDANCE_ROUND_CLOSED: "La asistencia ya cerró.",
  ATTENDANCE_QR_INVALID: "Ese código no es válido o ya venció. Prueba con el que ves ahora.",
  ATTENDANCE_ALREADY_RESOLVED: "Ese registro ya no está pendiente de revisión.",
};

/** The domain code of a failed call (`USER_EMAIL_TAKEN`...), or undefined for transport/unknown failures. */
export function errorCode(error: unknown): Name | undefined {
  const c = ConnectError.from(error).metadata.get("Aula-Error-Code");
  return c && c in messages ? (c as Name) : undefined;
}

/** The Spanish sentence to show for any failed call. */
export function errorMessage(error: unknown): string {
  const code = errorCode(error);
  if (code) return messages[code];
  const kind = ConnectError.from(error).code;
  if (kind === Code.Unavailable || kind === Code.DeadlineExceeded) return "No pudimos conectar con el servidor. Revisa tu conexión.";
  if (kind === Code.InvalidArgument) return messages.MALFORMED_ARGUMENT;
  return "Algo salió mal. Inténtalo de nuevo.";
}
