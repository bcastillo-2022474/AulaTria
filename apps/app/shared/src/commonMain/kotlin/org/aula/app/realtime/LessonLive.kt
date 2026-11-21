package org.aula.app.realtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import org.aula.app.AppContainer
import org.aula.app.query.QueryState
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse

/**
 * The component-side twin of `rememberLessonSnapshot` (that one is for composables; components may not import Compose).
 * `snapshot` is the cached GetLessonSnapshot (polled as a fallback); `start()` opens the WatchLesson stream that keeps it
 * fresh and refetches round queries when the lesson's `revision` moves; `stop()` closes it.
 *
 *   private val live = LessonLive(container, lessonId, scope).also { it.start() }   // in the component
 *   val snapshot = live.snapshot                                                       // Flow<QueryState<GetLessonSnapshotResponse>>
 *   init { lifecycle.doOnDestroy { live.stop() } }
 */
class LessonLive(private val container: AppContainer, private val lessonId: String, private val scope: CoroutineScope) {
    val snapshot: Flow<QueryState<GetLessonSnapshotResponse>> =
        container.queries.watch(container.api.lessonsQueries.getLessonSnapshot(GetLessonSnapshotRequest(lessonId = lessonId)))

    private var job: Job? = null

    fun start() { if (job == null) job = LessonRealtime(container.api, container.queries, scope).start(lessonId) }
    fun stop() { job?.cancel(); job = null }
}
