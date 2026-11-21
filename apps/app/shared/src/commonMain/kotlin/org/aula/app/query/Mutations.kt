package org.aula.app.query

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface MutationState<out O> {
    data object Idle : MutationState<Nothing>
    data object Running : MutationState<Nothing>
    data class Success<O>(val data: O) : MutationState<O>
    data class Failure(val error: Throwable) : MutationState<Nothing>
}

/**
 * Runs one generated [Mutation] and exposes its state. On success the queries named in the proto `invalidates`
 * option are refetched automatically: callers never invalidate by hand.
 *
 * - In a Compose screen: `val create = rememberMutation(container.api.coursesMutations.createCourse)`, then
 *   `create.state.collectAsState()` for the UI and `create.run(request) { navigate() }` from a click handler.
 * - In a Decompose component (no Compose imports allowed): `MutationRunner(queries, mutation, scope)`.
 * Show `error.userMessage()` (org.aula.app.errors) for a failure; never the server's message.
 */
class MutationRunner<I, O>(private val client: QueryClient, private val mutation: Mutation<I, O>, private val scope: CoroutineScope) {
    private val mutableState = MutableStateFlow<MutationState<O>>(MutationState.Idle)
    val state: StateFlow<MutationState<O>> = mutableState

    fun run(input: I, onSuccess: (O) -> Unit = {}) {
        if (mutableState.value is MutationState.Running) return // a double tap is one request
        scope.launch {
            mutableState.value = MutationState.Running
            try {
                val out = client.mutate(mutation, input)
                mutableState.value = MutationState.Success(out)
                onSuccess(out)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                mutableState.value = MutationState.Failure(e)
            }
        }
    }

    fun reset() { mutableState.value = MutationState.Idle }
}

@Composable
fun <I, O> rememberMutation(mutation: Mutation<I, O>, client: QueryClient = LocalQueryClient.current): MutationRunner<I, O> {
    val scope = rememberCoroutineScope()
    return remember(mutation, client) { MutationRunner(client, mutation, scope) }
}
