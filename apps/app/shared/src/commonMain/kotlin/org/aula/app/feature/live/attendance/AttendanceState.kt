package org.aula.app.feature.live.attendance

import org.aula.app.rpc.attendance.v1.ReviewReason

/** Which board of the design the student sees. */
enum class AttendancePhase { Entry, Locating, Checking, Present, NeedsReview }

/** What the attendance screen draws. */
data class AttendanceState(
    /** "BASES DE DATOS 2 · CLASE 12" (empty until the lesson snapshot arrives). */
    val courseLabel: String = "",
    val phase: AttendancePhase = AttendancePhase.Entry,
    /** The code in the field (the one on the teacher's screen). */
    val code: String = "",
    /** Why the last attempt did not register (a wrong or expired code, a closed round), as a Spanish sentence. */
    val error: String? = null,
    /** Only for [AttendancePhase.NeedsReview]: why the teacher has to look at it. */
    val reason: ReviewReason = ReviewReason.UNSPECIFIED,
) {
    companion object {
        /** The typable code is 6 characters; the QR payload (a future camera scan) is 16. */
        const val MIN_CODE = 6
        const val MAX_CODE = 16
    }
}
