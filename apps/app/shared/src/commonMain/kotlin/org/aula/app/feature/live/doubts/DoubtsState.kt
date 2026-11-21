package org.aula.app.feature.live.doubts

import org.aula.app.rpc.doubts.v1.DoubtRound
import pbandk.wkt.Timestamp

/** What the student's doubt screen draws. The countdowns are computed in the Screen from [round] and [serverTime]. */
data class DoubtsState(
    val loading: Boolean = true,
    /** The round could not be read and nothing is cached, as a Spanish sentence. */
    val loadError: String? = null,
    val round: DoubtRound? = null,
    val serverTime: Timestamp? = null,
    /** "BASES DE DATOS 2 · CLASE 12" (empty until the lesson snapshot arrives). */
    val courseLabel: String = "",
    /** The text in the field. */
    val draft: String = "",
    /** The student pressed "Editar mi duda": the form shows again, with their text. */
    val editing: Boolean = false,
    val busy: Boolean = false,
    /** Transient message for an AulaToast (a refused send, as a Spanish sentence). */
    val toast: String? = null,
) {
    companion object {
        /** `max_len` of SubmitDoubtRequest.text in the proto. */
        const val MAX_LENGTH = 500
    }
}
