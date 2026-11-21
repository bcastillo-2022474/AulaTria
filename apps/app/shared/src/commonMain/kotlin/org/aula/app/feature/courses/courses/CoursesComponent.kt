package org.aula.app.feature.courses.courses

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import org.aula.app.AppContainer
import org.aula.app.query.QueryState
import org.aula.app.rpc.courses.v1.CourseSummary
import org.aula.app.rpc.courses.v1.ListCoursesResponse

/**
 * A READ-ONLY screen: the component is thin on purpose. It exposes the cached query (the cache IS the state) and the
 * navigation callbacks, nothing else. REFERENCE IMPLEMENTATION: copy this shape for any screen that only shows server data.
 * The query polls (`refetch_seconds` in the proto) so "EN VIVO AHORA" appears by itself.
 */
class CoursesComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    val onOpenLesson: (lessonId: String) -> Unit,
    val onSignOut: () -> Unit,
) : ComponentContext by componentContext {

    val courses: Flow<QueryState<ListCoursesResponse>> = container.queries.watch(container.api.coursesQueries.listCourses())

    val userName: String get() = container.session.me.value?.name.orEmpty()

    /** The course that is live right now, if any, for the "EN VIVO AHORA" card. */
    fun liveCourse(all: List<CourseSummary>): CourseSummary? = all.firstOrNull { it.liveLesson != null }
}
