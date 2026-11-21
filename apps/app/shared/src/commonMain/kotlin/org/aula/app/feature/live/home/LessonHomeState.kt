package org.aula.app.feature.live.home

import org.aula.app.rpc.courses.v1.LessonStatus
import org.aula.app.rpc.lessons.v1.DoubtRoundRef
import org.aula.app.rpc.lessons.v1.PulseState
import pbandk.wkt.Timestamp

/** What the student's class home draws. Everything but the countdown (computed in the Screen from [doubtRound].closesAt and [serverTime]). */
data class LessonHomeState(
    val loading: Boolean = true,
    /** The class could not be read and nothing is cached, as a Spanish sentence. */
    val loadError: String? = null,
    /** "BASES DE DATOS 2 · CLASE 12" (empty until the first snapshot). */
    val courseLabel: String = "",
    val status: LessonStatus = LessonStatus.UNSPECIFIED,
    val serverTime: Timestamp? = null,
    /** What I chose: the server's, else the tap the server has not confirmed yet. */
    val myPulse: PulseState = PulseState.UNSPECIFIED,
    /** The doubt round that is OPEN now (null otherwise: a closed round never shows here). Opens the doubts screen. */
    val doubtRound: DoubtRoundRef? = null,
    /** The attendance round that is OPEN now (null otherwise). Opens the check-in screen. */
    val attendanceRoundId: String? = null,
    val floorRoundId: String? = null,
    val quizId: String? = null,
    /** Transient message for an AulaToast. */
    val toast: String? = null,
)
