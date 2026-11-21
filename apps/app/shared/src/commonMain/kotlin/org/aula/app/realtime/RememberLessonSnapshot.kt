package org.aula.app.realtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import org.aula.app.AppContainer
import org.aula.app.LocalAppContainer
import org.aula.app.query.QueryState
import org.aula.app.query.collectAsState
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest
import org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse

/**
 * THE way a live screen gets "the class right now": the cached `GetLessonSnapshot` (polled as a fallback) kept fresh
 * by the WatchLesson stream for as long as the screen is on screen.
 *
 *   val snapshot by rememberLessonSnapshot(lessonId)
 *   when (val s = snapshot) { is QueryState.Success -> Pulse(s.data.snapshot?.pulse) ... }
 */
@Composable
fun rememberLessonSnapshot(lessonId: String, container: AppContainer = LocalAppContainer.current): State<QueryState<GetLessonSnapshotResponse>> {
    val scope = rememberCoroutineScope()
    DisposableEffect(lessonId) {
        val job = LessonRealtime(container.api, container.queries, scope).start(lessonId)
        onDispose { job.cancel() }
    }
    return container.api.lessonsQueries.getLessonSnapshot(GetLessonSnapshotRequest(lessonId = lessonId)).collectAsState(container.queries)
}
