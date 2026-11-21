package org.aula.app.feature.live.home

import androidx.compose.runtime.Composable
import org.aula.app.designsystem.ScreenParagraph
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen

@Composable
fun LessonHomeScreen(component: LessonHomeComponent) {
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "En clase") {
        ScreenTitle("Pendiente")
        ScreenParagraph("LessonHome ${component.lessonId}")
    }
}
