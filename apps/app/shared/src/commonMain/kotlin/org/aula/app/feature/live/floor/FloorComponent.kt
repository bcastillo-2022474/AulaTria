package org.aula.app.feature.live.floor

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.aula.app.AppContainer
import org.aula.app.errors.userMessage
import org.aula.app.feature.componentScope
import org.aula.app.query.MutationRunner
import org.aula.app.query.MutationState
import org.aula.app.query.QueryState
import org.aula.app.query.dataOrNull
import org.aula.app.realtime.LessonLive
import org.aula.app.rpc.floor.v1.FinishMyTurnRequest
import org.aula.app.rpc.floor.v1.GetFloorRoundRequest
import org.aula.app.rpc.floor.v1.LowerHandRequest
import org.aula.app.rpc.floor.v1.RaiseHandRequest

/**
 * State and logic of the student's floor screen ("Toma de la palabra"). NEVER imports androidx.compose.*.
 * The round is the cached GetFloorRound (polled every 2 s); the lesson stream (LessonLive) refreshes it at once.
 * Every action goes through a MutationRunner, so a double tap is one request and the round refetches by itself.
 */
class FloorComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    private val lessonId: String,
    private val roundId: String,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val scope: CoroutineScope = componentScope()
    private val live = LessonLive(container, lessonId, scope).also { it.start() }

    init { lifecycle.doOnDestroy { live.stop() } }

    private val round = container.queries.watch(container.api.floorQueries.getFloorRound(GetFloorRoundRequest(roundId = roundId)))
    private val raise = MutationRunner(container.queries, container.api.floorMutations.raiseHand, scope)
    private val lower = MutationRunner(container.queries, container.api.floorMutations.lowerHand, scope)
    private val finish = MutationRunner(container.queries, container.api.floorMutations.finishMyTurn, scope)

    val state: StateFlow<FloorState> = combine(round, live.snapshot, raise.state, lower.state, finish.state) { r, snap, a, b, c ->
        val mutations = listOf(a, b, c)
        val failed = mutations.firstNotNullOfOrNull { (it as? MutationState.Failure)?.error }
        val readFailed = (r as? QueryState.Failure)?.error
        FloorState(
            loading = r is QueryState.Loading,
            round = r.dataOrNull()?.round,
            serverTime = snap.dataOrNull()?.snapshot?.serverTime,
            userId = container.session.me.value?.userId.orEmpty(),
            busy = mutations.any { it is MutationState.Running },
            error = (failed ?: readFailed)?.userMessage(),
        )
    }.stateIn(scope, SharingStarted.Eagerly, FloorState())

    fun onEvent(event: FloorEvent) {
        when (event) {
            FloorEvent.RaiseHandClick -> raise.run(RaiseHandRequest(roundId = roundId))
            FloorEvent.LowerHandClick -> lower.run(LowerHandRequest(roundId = roundId))
            FloorEvent.FinishMyTurnClick -> finish.run(FinishMyTurnRequest(roundId = roundId))
            FloorEvent.BackClick -> onBack()
        }
    }
}
