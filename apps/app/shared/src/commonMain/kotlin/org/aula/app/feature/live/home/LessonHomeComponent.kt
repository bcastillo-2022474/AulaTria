package org.aula.app.feature.live.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.aula.app.AppContainer
import org.aula.app.errors.userMessage
import org.aula.app.feature.componentScope
import org.aula.app.query.MutationRunner
import org.aula.app.query.MutationState
import org.aula.app.query.QueryState
import org.aula.app.query.dataOrNull
import org.aula.app.realtime.LessonLive
import org.aula.app.rpc.courses.v1.LessonStatus
import org.aula.app.rpc.lessons.v1.JoinRoomRequest
import org.aula.app.rpc.lessons.v1.PulseState
import org.aula.app.rpc.lessons.v1.SetPulseRequest

/**
 * The student's class home (Estudiante-C-Home), the screen the router opens from "Mis cursos". It enters the room once
 * (JoinRoom is idempotent) and then only READS the lesson snapshot, which the WatchLesson stream keeps fresh (polled as a
 * fallback): status, my pulse, the open doubt round and the floor/quiz rounds all arrive through it.
 * Navigation callbacks are fixed by RootComponent.
 */
class LessonHomeComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    val lessonId: String,
    val onOpenDoubts: (roundId: String) -> Unit,
    val onOpenAttendance: (roundId: String) -> Unit,
    val onOpenFloor: (roundId: String) -> Unit,
    val onOpenQuiz: (quizId: String) -> Unit,
    val onLeave: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val live = LessonLive(container, lessonId, scope).also { it.start() }
    private val join = MutationRunner(container.queries, container.api.lessonsMutations.joinRoom, scope)
    private val pulse = MutationRunner(container.queries, container.api.lessonsMutations.setPulse, scope)

    /** The pulse I just tapped, shown at once; the server's copy replaces it when it comes back. */
    private val pendingPulse = MutableStateFlow<PulseState>(PulseState.UNSPECIFIED)

    init {
        lifecycle.doOnDestroy { live.stop() }
        // A refused tap (the class just ended...) must not leave a pulse on screen that was never recorded.
        scope.launch { pulse.state.collect { if (it is MutationState.Failure) pendingPulse.value = PulseState.UNSPECIFIED } }
        // Enter the room once, as soon as the snapshot says the lesson takes participants (lobby or live).
        scope.launch {
            var joined = false
            live.snapshot.collect { q ->
                val status = q.dataOrNull()?.snapshot?.status
                if (!joined && (status == LessonStatus.LOBBY || status == LessonStatus.LIVE)) {
                    joined = true
                    join.run(JoinRoomRequest(lessonId = lessonId))
                }
            }
        }
    }

    val state: StateFlow<LessonHomeState> = combine(live.snapshot, join.state, pulse.state, pendingPulse) { q, joining, pulsing, pending ->
        val snap = q.dataOrNull()?.snapshot
        val failed = (joining as? MutationState.Failure)?.error ?: (pulsing as? MutationState.Failure)?.error
        LessonHomeState(
            loading = snap == null && q !is QueryState.Failure,
            loadError = if (snap == null) (q as? QueryState.Failure)?.error?.userMessage() else null,
            courseLabel = snap?.let { buildString { append(it.courseName.uppercase()); if (it.number > 0) append(" · CLASE ${it.number}") } }.orEmpty(),
            status = snap?.status ?: LessonStatus.UNSPECIFIED,
            serverTime = snap?.serverTime,
            myPulse = if (pending != PulseState.UNSPECIFIED) pending else snap?.myPulse ?: PulseState.UNSPECIFIED,
            doubtRound = snap?.activeDoubtRound,
            attendanceRoundId = snap?.activeAttendanceRound?.id,
            floorRoundId = snap?.activeFloorRound?.id,
            quizId = snap?.activeQuiz?.id,
            toast = failed?.userMessage(),
        )
    }.stateIn(scope, SharingStarted.Eagerly, LessonHomeState())

    fun onEvent(event: LessonHomeEvent) {
        when (event) {
            is LessonHomeEvent.PulseClick -> {
                pendingPulse.value = event.state
                pulse.run(SetPulseRequest(lessonId = lessonId, state = event.state))
            }
            LessonHomeEvent.OpenDoubtsClick -> state.value.doubtRound?.id?.let(onOpenDoubts)
            LessonHomeEvent.OpenAttendanceClick -> state.value.attendanceRoundId?.let(onOpenAttendance)
            LessonHomeEvent.OpenFloorClick -> state.value.floorRoundId?.let(onOpenFloor)
            LessonHomeEvent.OpenQuizClick -> state.value.quizId?.let(onOpenQuiz)
            LessonHomeEvent.LeaveClick -> onLeave()
        }
    }
}
