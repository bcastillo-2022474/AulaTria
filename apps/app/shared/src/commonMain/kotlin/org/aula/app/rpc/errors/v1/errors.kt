@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.errors.v1

@pbandk.Export
public sealed class ErrorCode(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.errors.v1.ErrorCode && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "ErrorCode.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : ErrorCode(0, "ERROR_CODE_UNSPECIFIED")
    public object MALFORMED_ARGUMENT : ErrorCode(1, "MALFORMED_ARGUMENT")
    public object FORBIDDEN : ErrorCode(2, "FORBIDDEN")
    public object UNAUTHENTICATED : ErrorCode(3, "UNAUTHENTICATED")
    public object TOKEN_EXPIRED : ErrorCode(4, "TOKEN_EXPIRED")
    public object TENANT_MISMATCH : ErrorCode(5, "TENANT_MISMATCH")
    public object NOT_FOUND : ErrorCode(6, "NOT_FOUND")
    public object USER_INVALID_CREDENTIALS : ErrorCode(10, "USER_INVALID_CREDENTIALS")
    public object USER_EMAIL_TAKEN : ErrorCode(11, "USER_EMAIL_TAKEN")
    public object INVITATION_INVALID : ErrorCode(12, "INVITATION_INVALID")
    public object COURSE_NOT_FOUND : ErrorCode(20, "COURSE_NOT_FOUND")
    public object COURSE_JOIN_CODE_NOT_FOUND : ErrorCode(21, "COURSE_JOIN_CODE_NOT_FOUND")
    public object COURSE_ALREADY_ENROLLED : ErrorCode(22, "COURSE_ALREADY_ENROLLED")
    public object COURSE_NOT_ENROLLED : ErrorCode(23, "COURSE_NOT_ENROLLED")
    public object LESSON_NOT_FOUND : ErrorCode(30, "LESSON_NOT_FOUND")
    public object LESSON_ALREADY_ACTIVE : ErrorCode(31, "LESSON_ALREADY_ACTIVE")
    public object LESSON_NOT_IN_LOBBY : ErrorCode(32, "LESSON_NOT_IN_LOBBY")
    public object LESSON_NOT_LIVE : ErrorCode(33, "LESSON_NOT_LIVE")
    public object MATERIAL_INVALID : ErrorCode(34, "MATERIAL_INVALID")
    public object DOUBT_ROUND_NOT_FOUND : ErrorCode(40, "DOUBT_ROUND_NOT_FOUND")
    public object DOUBT_ROUND_ALREADY_OPEN : ErrorCode(41, "DOUBT_ROUND_ALREADY_OPEN")
    public object DOUBT_ROUND_CLOSED : ErrorCode(42, "DOUBT_ROUND_CLOSED")
    public object FLOOR_ROUND_NOT_FOUND : ErrorCode(50, "FLOOR_ROUND_NOT_FOUND")
    public object FLOOR_ROUND_ALREADY_OPEN : ErrorCode(51, "FLOOR_ROUND_ALREADY_OPEN")
    public object FLOOR_ROUND_CLOSED : ErrorCode(52, "FLOOR_ROUND_CLOSED")
    public object FLOOR_NOT_YOUR_TURN : ErrorCode(53, "FLOOR_NOT_YOUR_TURN")
    public object QUIZ_NOT_FOUND : ErrorCode(60, "QUIZ_NOT_FOUND")
    public object QUIZ_ALREADY_LIVE : ErrorCode(61, "QUIZ_ALREADY_LIVE")
    public object QUIZ_NOT_DRAFT : ErrorCode(62, "QUIZ_NOT_DRAFT")
    public object QUIZ_CLOSED : ErrorCode(63, "QUIZ_CLOSED")
    public object QUIZ_ALREADY_ANSWERED : ErrorCode(64, "QUIZ_ALREADY_ANSWERED")
    public object QUIZ_NO_MATERIAL : ErrorCode(65, "QUIZ_NO_MATERIAL")
    public object QUIZ_NOT_LIVE : ErrorCode(66, "QUIZ_NOT_LIVE")
    public object ATTENDANCE_ROUND_NOT_FOUND : ErrorCode(70, "ATTENDANCE_ROUND_NOT_FOUND")
    public object ATTENDANCE_ROUND_ALREADY_OPEN : ErrorCode(71, "ATTENDANCE_ROUND_ALREADY_OPEN")
    public object ATTENDANCE_ROUND_CLOSED : ErrorCode(72, "ATTENDANCE_ROUND_CLOSED")
    public object ATTENDANCE_QR_INVALID : ErrorCode(73, "ATTENDANCE_QR_INVALID")
    public class UNRECOGNIZED(value: Int) : ErrorCode(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.errors.v1.ErrorCode> {
        public val values: List<org.aula.app.rpc.errors.v1.ErrorCode> by lazy { listOf(UNSPECIFIED, MALFORMED_ARGUMENT, FORBIDDEN, UNAUTHENTICATED, TOKEN_EXPIRED, TENANT_MISMATCH, NOT_FOUND, USER_INVALID_CREDENTIALS, USER_EMAIL_TAKEN, INVITATION_INVALID, COURSE_NOT_FOUND, COURSE_JOIN_CODE_NOT_FOUND, COURSE_ALREADY_ENROLLED, COURSE_NOT_ENROLLED, LESSON_NOT_FOUND, LESSON_ALREADY_ACTIVE, LESSON_NOT_IN_LOBBY, LESSON_NOT_LIVE, MATERIAL_INVALID, DOUBT_ROUND_NOT_FOUND, DOUBT_ROUND_ALREADY_OPEN, DOUBT_ROUND_CLOSED, FLOOR_ROUND_NOT_FOUND, FLOOR_ROUND_ALREADY_OPEN, FLOOR_ROUND_CLOSED, FLOOR_NOT_YOUR_TURN, QUIZ_NOT_FOUND, QUIZ_ALREADY_LIVE, QUIZ_NOT_DRAFT, QUIZ_CLOSED, QUIZ_ALREADY_ANSWERED, QUIZ_NO_MATERIAL, QUIZ_NOT_LIVE, ATTENDANCE_ROUND_NOT_FOUND, ATTENDANCE_ROUND_ALREADY_OPEN, ATTENDANCE_ROUND_CLOSED, ATTENDANCE_QR_INVALID) }
        override fun fromValue(value: Int): org.aula.app.rpc.errors.v1.ErrorCode = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.errors.v1.ErrorCode = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No ErrorCode with name: $name")
    }
}
