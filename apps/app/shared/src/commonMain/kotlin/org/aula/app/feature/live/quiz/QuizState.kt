package org.aula.app.feature.live.quiz

import org.aula.app.rpc.quizzes.v1.Quiz
import pbandk.wkt.Timestamp

/** What the quiz screen draws. The countdown itself is computed in the Screen from [quiz].closesAt and [serverTime]. */
data class QuizState(
    val loading: Boolean = true,
    /** Draft quizzes are invisible to students (QUIZ_NOT_FOUND): "todavía no está disponible". */
    val unavailable: Boolean = false,
    /** A failure with nothing to show (no quiz yet), as a Spanish sentence. */
    val loadError: String? = null,
    val quiz: Quiz? = null,
    val serverTime: Timestamp? = null,
    /** "INTELIGENCIA ARTIFICIAL · CLASE 12" (empty until the lesson snapshot arrives). */
    val courseLabel: String = "",
    /** The letter I just tapped, shown as Selected before the server confirms (optimistic). */
    val pendingLabel: String = "",
    /** The answer was sent (or the server says it is already recorded / closed) but the server copy has not come back: keep options locked. */
    val locked: Boolean = false,
    /** Transient message for an AulaToast. */
    val toast: String? = null,
) {
    /** My letter: the server's, else the optimistic one. Empty = not answered. */
    val myLabel: String get() = quiz?.myLabel.orEmpty().ifEmpty { pendingLabel }
}
