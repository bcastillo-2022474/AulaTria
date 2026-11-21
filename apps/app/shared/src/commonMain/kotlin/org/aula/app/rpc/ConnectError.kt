package org.aula.app.rpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The Connect protocol's error codes (wire names in the JSON error body). Copied from Chassis. */
enum class ConnectCode(val wireName: String) {
    CANCELED("canceled"), UNKNOWN("unknown"), INVALID_ARGUMENT("invalid_argument"),
    DEADLINE_EXCEEDED("deadline_exceeded"), NOT_FOUND("not_found"), ALREADY_EXISTS("already_exists"),
    PERMISSION_DENIED("permission_denied"), RESOURCE_EXHAUSTED("resource_exhausted"),
    FAILED_PRECONDITION("failed_precondition"), ABORTED("aborted"), OUT_OF_RANGE("out_of_range"),
    UNIMPLEMENTED("unimplemented"), INTERNAL("internal"), UNAVAILABLE("unavailable"),
    DATA_LOSS("data_loss"), UNAUTHENTICATED("unauthenticated");

    companion object {
        private val byWireName = entries.associateBy(ConnectCode::wireName)
        /** Unknown/future codes degrade to [UNKNOWN] rather than throwing. */
        fun fromWireName(name: String?): ConnectCode = name?.let { byWireName[it] } ?: UNKNOWN
    }
}

/**
 * A structured error from the API. [code] says what KIND of failure it was; [domainCode] says WHICH one
 * (`USER_EMAIL_TAKEN`), read from the `Aula-Error-Code` header. Switch on [domainCode] with the generated
 * `org.aula.app.rpc.errors.v1.ErrorCode`, never on [message]. [domainCode] is null for failures that carry
 * none (a proxy error, an unexpected failure: the server hides those on purpose).
 */
class ConnectException(
    val code: ConnectCode,
    override val message: String,
    val httpStatus: Int,
    val domainCode: String? = null,
) : Exception("$code: $message")

@Serializable
internal data class ConnectErrorBody(@SerialName("code") val code: String? = null, @SerialName("message") val message: String? = null)

/** End-of-stream envelope of a Connect streaming response: `{}` or `{"error": {...}}`. */
@Serializable
internal data class EndStreamBody(@SerialName("error") val error: ConnectErrorBody? = null)
