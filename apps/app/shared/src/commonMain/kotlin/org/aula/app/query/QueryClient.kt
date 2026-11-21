package org.aula.app.query

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

// "React Query for Compose". Server state lives in a keyed cache; screens watch a Query and get a
// StateFlow; a Mutation invalidates the keys it makes stale. The descriptors (Query / Mutation) are
// GENERATED from the .proto (tools/protoc-gen-aula-kotlin); this file is the only hand-written core.

/** ["GetCourse", "<request>"]: invalidating ["GetCourse"] invalidates every GetCourse, any request. */
data class QueryKey(val parts: List<String>) {
    constructor(vararg p: String) : this(p.toList())
    fun startsWith(prefix: QueryKey) = parts.size >= prefix.parts.size && parts.take(prefix.parts.size) == prefix.parts
}

sealed interface QueryState<out T> {
    data object Loading : QueryState<Nothing>
    data class Success<T>(val data: T, val isRefetching: Boolean = false) : QueryState<T>
    /** [staleData] keeps the last good value so a flaky refetch does not blank the screen. */
    data class Failure<T>(val error: Throwable, val staleData: T? = null) : QueryState<T>
}

fun <T> QueryState<T>.dataOrNull(): T? = when (this) {
    is QueryState.Success -> data
    is QueryState.Failure -> staleData
    QueryState.Loading -> null
}

class Query<T>(
    val key: QueryKey,
    val fetch: suspend () -> T,
    val staleAfterMs: Long = 30_000,
    /** From the proto option `refetch_seconds`: poll while watched. Also the fallback when streaming is unavailable. */
    val refetchEveryMs: Long? = null,
)

class Mutation<I, O>(
    val run: suspend (I) -> O,
    /** From the proto option `invalidates`. */
    val invalidates: (I, O) -> List<QueryKey> = { _, _ -> emptyList() },
)

class QueryClient(private val scope: CoroutineScope, private val now: () -> Long) {
    private class Entry(val state: MutableStateFlow<QueryState<Any?>> = MutableStateFlow(QueryState.Loading)) {
        var watchers = 0
        var updatedAt = 0L
        var job: Job? = null
        var inFlight: Job? = null
        var refetchLoop: Job? = null
    }

    private val entries = HashMap<QueryKey, Entry>()

    /** A cold flow: collecting it starts fetching (deduped per key); the last collector leaving stops polling. */
    @Suppress("UNCHECKED_CAST")
    fun <T> watch(query: Query<T>): Flow<QueryState<T>> = channelFlow {
        val entry = entries.getOrPut(query.key) { Entry() }
        entry.watchers++
        if (entry.state.value is QueryState.Loading || now() - entry.updatedAt > query.staleAfterMs) refetch(query, entry)
        query.refetchEveryMs?.let { every ->
            if (entry.refetchLoop == null) entry.refetchLoop = scope.launch { while (true) { delay(every); refetch(query, entry) } }
        }
        try {
            (entry.state as StateFlow<QueryState<T>>).collect { send(it) }
        } finally {
            entry.watchers--
            if (entry.watchers == 0) { entry.refetchLoop?.cancel(); entry.refetchLoop = null }
        }
    }

    private fun <T> refetch(query: Query<T>, entry: Entry) {
        if (entry.inFlight?.isActive == true) return // dedupe
        entry.inFlight = scope.launch {
            val before = entry.state.value
            entry.state.value = (before as? QueryState.Success<Any?>)?.copy(isRefetching = true) ?: before
            try {
                entry.state.value = QueryState.Success(query.fetch(), false)
                entry.updatedAt = now()
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Throwable) {
                entry.state.value = QueryState.Failure(e, (before as? QueryState.Success<Any?>)?.data)
            }
        }
    }

    /** Marks every key starting with [prefix] stale. Watched ones refetch now; the rest on next watch. */
    fun invalidate(prefix: QueryKey) {
        entries.filterKeys { it.startsWith(prefix) }.forEach { (_, e) -> e.updatedAt = 0 }
    }

    /** Sign-out: nothing cached may leak to the next user. */
    fun clear() { entries.values.forEach { it.refetchLoop?.cancel() }; entries.clear() }

    /** Realtime layer: replace cached data (a snapshot arrived on the stream). */
    fun <T> setData(key: QueryKey, data: T) {
        val entry = entries.getOrPut(key) { Entry() }
        entry.state.value = QueryState.Success(data, false)
        entry.updatedAt = now()
    }

    suspend fun <I, O> mutate(mutation: Mutation<I, O>, input: I): O {
        val out = mutation.run(input)
        mutation.invalidates(input, out).forEach { invalidate(it) }
        return out
    }
}

val LocalQueryClient = compositionLocalOf<QueryClient> { error("provide a QueryClient with CompositionLocalProvider(LocalQueryClient provides ...)") }

@Composable
fun <T> Query<T>.collectAsState(client: QueryClient = LocalQueryClient.current): State<QueryState<T>> =
    client.watch(this).collectAsState(QueryState.Loading)
