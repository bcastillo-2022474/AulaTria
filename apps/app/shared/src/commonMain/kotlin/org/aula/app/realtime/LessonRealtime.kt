package org.aula.app.realtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.aula.app.query.QueryClient
import org.aula.app.query.QueryKey
import org.aula.app.rpc.AulaApi
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse
import org.aula.app.rpc.lessons.v1.WatchLessonRequest

/**
 * Keeps the GetLessonSnapshot cache fresh from the WatchLesson stream while a lesson screen is alive.
 * The server pushes a full snapshot on every change, so the cache entry is replaced. If the stream drops or
 * the platform cannot stream (UNVERIFIED on iOS/wasm), the query's own polling (`refetch_seconds` in the
 * proto) keeps the screen correct, and this loop keeps trying to reconnect.
 *
 * Usage (in a component of a live screen): `val live = LessonRealtime(container.api, container.queries, scope).start(lessonId)`
 * and `live.cancel()` when the screen goes away.
 */
class LessonRealtime(private val api: AulaApi, private val queries: QueryClient, private val scope: CoroutineScope) {
    fun start(lessonId: String): Job = scope.launch {
        val snapshotKey = api.lessonsQueries.getLessonSnapshot(GetLessonSnapshotRequest(lessonId = lessonId)).key
        var lastRevision = -1
        while (true) {
            try {
                api.lessonsClient.watchLesson(WatchLessonRequest(lessonId = lessonId)).collect { event ->
                    (event.event as? org.aula.app.rpc.lessons.v1.LessonEvent.Event.Snapshot)?.let { snap ->
                        queries.setData(snapshotKey, GetLessonSnapshotResponse(snapshot = snap.value))
                        // The server bumps `revision` on every change of the lesson: refresh whatever round screen is open NOW.
                        if (snap.value.revision != lastRevision) {
                            lastRevision = snap.value.revision
                            ROUND_QUERIES.forEach { queries.invalidate(QueryKey(it)) }
                        }
                    }
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (_: Exception) {
                // dropped: fall through to the retry delay; polling covers the gap
            }
            delay(1_500)
        }
    }

    private companion object {
        /** The RPCs whose cached data depends on what happens in a live lesson (names from the proto). */
        val ROUND_QUERIES = listOf("GetFloorRound", "GetQuiz", "GetDoubtRound", "GetAttendanceRound")
    }
}
