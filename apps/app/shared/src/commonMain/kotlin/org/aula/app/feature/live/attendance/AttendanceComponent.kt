package org.aula.app.feature.live.attendance

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aula.app.AppContainer
import org.aula.app.errors.userMessage
import org.aula.app.feature.componentScope
import org.aula.app.query.MutationRunner
import org.aula.app.query.MutationState
import org.aula.app.query.dataOrNull
import org.aula.app.realtime.LessonLive
import org.aula.app.rpc.attendance.v1.AttendanceStatus
import org.aula.app.rpc.attendance.v1.CheckInRequest

/**
 * Student attendance (Estudiante-C-Escanear -> Verificando -> Presente / PorRevisar) as ONE screen that follows [AttendancePhase].
 * The student types the code the teacher shows (CheckIn accepts the QR payload or the typable code); there is NO camera
 * scanner yet and the screen says so. The location is optional: without it the server answers "por revisar", not an error.
 * A refused attempt (wrong or expired code, closed round) returns to the field with the reason. NEVER imports androidx.compose.*.
 */
class AttendanceComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    val lessonId: String,
    val roundId: String,
    val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val live = LessonLive(container, lessonId, scope).also { it.start() }
    private val checkIn = MutationRunner(container.queries, container.api.attendanceMutations.checkIn, scope)

    private data class Local(val phase: AttendancePhase = AttendancePhase.Entry, val code: String = "", val error: String? = null, val reason: org.aula.app.rpc.attendance.v1.ReviewReason = org.aula.app.rpc.attendance.v1.ReviewReason.UNSPECIFIED)
    private val local = MutableStateFlow(Local())

    init {
        lifecycle.doOnDestroy { live.stop() }
        // A refused check-in goes back to the field with the reason; the typed code stays so a typo is one edit away.
        scope.launch {
            checkIn.state.collect { s ->
                if (s is MutationState.Failure) local.update { it.copy(phase = AttendancePhase.Entry, error = s.error.userMessage()) }
            }
        }
    }

    val state: StateFlow<AttendanceState> = combine(live.snapshot, local) { snap, l ->
        val s = snap.dataOrNull()?.snapshot
        AttendanceState(
            courseLabel = s?.let { buildString { append(it.courseName.uppercase()); if (it.number > 0) append(" · CLASE ${it.number}") } }.orEmpty(),
            phase = l.phase, code = l.code, error = l.error, reason = l.reason,
        )
    }.stateIn(scope, SharingStarted.Eagerly, AttendanceState())

    fun onEvent(event: AttendanceEvent) {
        when (event) {
            is AttendanceEvent.CodeChange -> local.update {
                it.copy(code = event.text.filter { c -> c.isLetterOrDigit() }.uppercase().take(AttendanceState.MAX_CODE), error = null)
            }
            AttendanceEvent.ConfirmClick -> local.update {
                if (it.phase == AttendancePhase.Entry && it.code.length >= AttendanceState.MIN_CODE) it.copy(phase = AttendancePhase.Locating, error = null) else it
            }
            is AttendanceEvent.Located -> {
                if (local.value.phase != AttendancePhase.Locating) return
                local.update { it.copy(phase = AttendancePhase.Checking) }
                val where = event.location
                checkIn.run(
                    CheckInRequest(
                        roundId = roundId, code = local.value.code, hasLocation = where != null,
                        lat = where?.lat ?: 0.0, lng = where?.lng ?: 0.0, accuracyM = where?.accuracyM ?: 0,
                    ),
                ) { res ->
                    local.update {
                        it.copy(phase = if (res.status == AttendanceStatus.PRESENT) AttendancePhase.Present else AttendancePhase.NeedsReview, reason = res.reason)
                    }
                }
            }
            AttendanceEvent.ScanAgainClick -> local.value = Local()
            AttendanceEvent.BackClick -> onBack()
        }
    }
}
