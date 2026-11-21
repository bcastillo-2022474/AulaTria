package org.aula.app.feature.courses.courses

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.aula.app.designsystem.ActionBanner
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaColors
import org.aula.app.designsystem.AulaListCard
import org.aula.app.designsystem.ButtonKind
import org.aula.app.designsystem.CourseChip
import org.aula.app.designsystem.EmptyState
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.ListDivider
import org.aula.app.designsystem.ListRow
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen
import org.aula.app.errors.userMessage
import org.aula.app.query.QueryState
import org.aula.app.query.dataOrNull
import org.aula.app.rpc.courses.v1.CourseSummary
import org.aula.app.rpc.courses.v1.LessonStatus

/** The binder: read-only, so it only collects the cached query. REFERENCE for "a list of server data". */
@Composable
fun CoursesScreen(component: CoursesComponent) {
    val state by component.courses.collectAsState(QueryState.Loading)
    CoursesContent(
        state = state,
        onOpenLesson = component.onOpenLesson,
        onSignOut = component.onSignOut,
    )
}

@Composable
fun CoursesContent(state: QueryState<org.aula.app.rpc.courses.v1.ListCoursesResponse>, onOpenLesson: (String) -> Unit, onSignOut: () -> Unit) {
    val courses = state.dataOrNull()?.courses.orEmpty()
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "Mis cursos") {
        ScreenTitle("Mis cursos")
        when {
            state is QueryState.Loading -> HelperText("Cargando…")
            state is QueryState.Failure && courses.isEmpty() -> EmptyState("No pudimos cargar tus cursos", state.error.userMessage())
            courses.isEmpty() -> EmptyState("Aún no tienes cursos", "Cuando te unas a un curso con el código de tu catedrático, aparecerá aquí.")
            else -> Courses(courses, onOpenLesson)
        }
        AulaButton("Cerrar sesión", onSignOut, kind = ButtonKind.Secondary)
    }
}

@Composable
private fun Courses(courses: List<CourseSummary>, onOpenLesson: (String) -> Unit) {
    // "EN VIVO AHORA": a course whose lesson is in the lobby or live.
    courses.filter { it.liveLesson != null }.forEach { c ->
        ActionBanner(kicker = "EN VIVO AHORA", title = c.name, tall = true, onClick = { onOpenLesson(c.liveLesson!!.id) })
    }
    AulaListCard(Modifier.fillMaxWidth()) {
        courses.forEachIndexed { i, c ->
            if (i > 0) ListDivider()
            ListRow(
                title = c.name,
                subtitle = listOfNotNull(c.code.ifBlank { null }, if (c.liveLesson?.status == LessonStatus.LIVE) "En vivo" else null).joinToString(" · "),
                leading = { CourseChip(c.name.first().uppercase(), AulaColors.Primary) },
                onClick = c.liveLesson?.let { l -> { onOpenLesson(l.id) } },
            )
        }
    }
}
