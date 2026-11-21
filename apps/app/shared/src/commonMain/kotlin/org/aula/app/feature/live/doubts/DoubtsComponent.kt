package org.aula.app.feature.live.doubts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.aula.app.AppContainer
import org.aula.app.errors.userMessage
import org.aula.app.feature.componentScope
import org.aula.app.query.MutationRunner
import org.aula.app.query.MutationState
import org.aula.app.query.QueryState
import org.aula.app.query.dataOrNull
import org.aula.app.realtime.LessonLive
import org.aula.app.rpc.doubts.v1.DoubtRoundStatus
import org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest
import org.aula.app.rpc.doubts.v1.SubmitDoubtRequest

/**
 * Student doubt round (Estudiante-C-Dudas / DudaEnviada / DudaGrupo): ONE screen that renders by the round status and whether
 * I already sent a doubt. The cached `GetDoubtRound` (2 s poll + instant refresh from the lesson stream) is the source of
 * truth, so the screen turns into "La ronda cerró" by itself when the timer runs out or the teacher closes the round.
 * The only local state is the text field and the "editing" flag. NEVER imports androidx.compose.*.
 */
class DoubtsComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    val lessonId: String,
    val roundId: String,
    val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val live = LessonLive(container, lessonId, scope).also { it.start() }
    private val round = container.queries.watch(container.api.doubtsQueries.getDoubtRound(GetDoubtRoundRequest(roundId = roundId)))
    private val submit = MutationRunner(container.queries, container.api.doubtsMutations.submitDoubt, scope)

    private data class Local(val draft: String = "", val editing: Boolean = false)
    private val local = MutableStateFlow(Local())

    init { lifecycle.doOnDestroy { live.stop() } }

    val state: StateFlow<DoubtsState> = combine(round, live.snapshot, submit.state, local) { r, snap, sending, l ->
        val data = r.dataOrNull()?.round
        val snapshot = snap.dataOrNull()?.snapshot
        DoubtsState(
            loading = data == null && r !is QueryState.Failure,
            loadError = if (data == null) (r as? QueryState.Failure)?.error?.userMessage() else null,
            round = data,
            serverTime = snapshot?.serverTime,
            courseLabel = snapshot?.let { buildString { append(it.courseName.uppercase()); if (it.number > 0) append(" · CLASE ${it.number}") } }.orEmpty(),
            draft = l.draft,
            editing = l.editing,
            busy = sending is MutationState.Running,
            toast = (sending as? MutationState.Failure)?.error?.userMessage(),
        )
    }.stateIn(scope, SharingStarted.Eagerly, DoubtsState())

    fun onEvent(event: DoubtsEvent) {
        when (event) {
            is DoubtsEvent.DraftChange -> local.update { it.copy(draft = event.text.take(DoubtsState.MAX_LENGTH)) }
            DoubtsEvent.SendClick -> {
                val text = local.value.draft.trim()
                if (text.isEmpty() || state.value.round?.status != DoubtRoundStatus.OPEN) return
                // The round refetches by itself (proto `invalidates`): it then carries my_doubt and the screen shows "Duda enviada".
                submit.run(SubmitDoubtRequest(roundId = roundId, text = text)) { local.update { it.copy(editing = false) } }
            }
            DoubtsEvent.EditClick -> local.update { it.copy(draft = state.value.round?.myDoubt?.text.orEmpty(), editing = true) }
            DoubtsEvent.BackClick -> onBack()
        }
    }
}
