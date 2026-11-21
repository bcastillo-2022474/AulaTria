package org.aula.app.errors

import org.aula.app.rpc.ConnectCode
import org.aula.app.rpc.ConnectException
import org.aula.app.rpc.errors.v1.ErrorCode

/** The domain code of a failed call, or null for transport/unknown failures. Switch on this, never on the message. */
fun Throwable.errorCode(): ErrorCode? =
    (this as? ConnectException)?.domainCode?.let { ErrorCode.fromName(it) }?.takeIf { it != ErrorCode.UNSPECIFIED && it !is ErrorCode.UNRECOGNIZED }

/** The Spanish sentence to show for any failed call. Never show the server's message. */
fun Throwable.userMessage(): String {
    errorCode()?.let { return it.userMessage() }
    val kind = (this as? ConnectException)?.code
    return when (kind) {
        ConnectCode.UNAVAILABLE, ConnectCode.DEADLINE_EXCEEDED -> "No pudimos conectar con el servidor. Revisa tu conexión."
        ConnectCode.INVALID_ARGUMENT -> "Revisa los datos e inténtalo de nuevo."
        else -> "Algo salió mal. Inténtalo de nuevo."
    }
}

/**
 * EXHAUSTIVE on purpose: add a code to errors.proto and this `when` stops compiling until you write its Spanish
 * message (ErrorCode is a sealed class). Same table as apps/web/src/rpc/errors.ts: keep the wording identical.
 */
fun ErrorCode.userMessage(): String = when (this) {
    ErrorCode.UNSPECIFIED, is ErrorCode.UNRECOGNIZED -> "Algo salió mal. Inténtalo de nuevo."
    ErrorCode.MALFORMED_ARGUMENT -> "Revisa los datos e inténtalo de nuevo."
    ErrorCode.FORBIDDEN -> "No tienes permiso para hacer esto."
    ErrorCode.UNAUTHENTICATED, ErrorCode.TOKEN_EXPIRED -> "Tu sesión terminó. Inicia sesión otra vez."
    ErrorCode.TENANT_MISMATCH -> "Tu sesión no corresponde a este colegio. Inicia sesión otra vez."
    ErrorCode.NOT_FOUND -> "No encontramos lo que buscas."
    ErrorCode.USER_INVALID_CREDENTIALS -> "Correo o contraseña incorrectos."
    ErrorCode.USER_EMAIL_TAKEN -> "Ya existe una cuenta con ese correo."
    ErrorCode.INVITATION_INVALID -> "Esta invitación ya no es válida. Pide una nueva."
    ErrorCode.COURSE_NOT_FOUND -> "No encontramos ese curso."
    ErrorCode.COURSE_JOIN_CODE_NOT_FOUND -> "No encontramos un curso con ese código."
    ErrorCode.COURSE_ALREADY_ENROLLED -> "Ya estás en este curso."
    ErrorCode.COURSE_NOT_ENROLLED -> "No estás inscrito en este curso."
    ErrorCode.LESSON_NOT_FOUND -> "No encontramos esa clase."
    ErrorCode.LESSON_ALREADY_ACTIVE -> "Este curso ya tiene una sala abierta."
    ErrorCode.LESSON_NOT_IN_LOBBY -> "La sala ya no está abierta."
    ErrorCode.LESSON_NOT_LIVE -> "La clase no está en curso."
    ErrorCode.MATERIAL_INVALID -> "No pudimos leer ese archivo."
    ErrorCode.DOUBT_ROUND_NOT_FOUND -> "No encontramos esa ronda de dudas."
    ErrorCode.DOUBT_ROUND_ALREADY_OPEN -> "Ya hay una ronda de dudas abierta."
    ErrorCode.DOUBT_ROUND_CLOSED -> "La ronda de dudas ya cerró."
    ErrorCode.FLOOR_ROUND_NOT_FOUND -> "No encontramos esa ronda."
    ErrorCode.FLOOR_ROUND_ALREADY_OPEN -> "Ya hay una ronda de palabra abierta."
    ErrorCode.FLOOR_ROUND_CLOSED -> "La ronda de palabra ya cerró."
    ErrorCode.FLOOR_NOT_YOUR_TURN -> "Todavía no es tu turno."
    ErrorCode.QUIZ_NOT_FOUND -> "No encontramos ese quiz."
    ErrorCode.QUIZ_ALREADY_LIVE -> "Ya hay un quiz en vivo."
    ErrorCode.QUIZ_NOT_DRAFT -> "Este quiz ya se lanzó."
    ErrorCode.QUIZ_CLOSED -> "El quiz ya cerró."
    ErrorCode.QUIZ_ALREADY_ANSWERED -> "Ya respondiste este quiz."
    ErrorCode.QUIZ_NO_MATERIAL -> "Sube el material de la clase para armar el quiz."
    ErrorCode.QUIZ_NOT_LIVE -> "Este quiz todavía no está en vivo."
    ErrorCode.ATTENDANCE_ROUND_NOT_FOUND -> "No encontramos esa asistencia."
    ErrorCode.ATTENDANCE_ROUND_ALREADY_OPEN -> "Ya hay una asistencia abierta."
    ErrorCode.ATTENDANCE_ROUND_CLOSED -> "La asistencia ya cerró."
    ErrorCode.ATTENDANCE_QR_INVALID -> "Ese código no es válido o ya venció. Prueba con el que ves ahora."
}
