package org.aula.app.feature.live.quiz

import androidx.compose.runtime.Composable
import org.aula.app.designsystem.ScreenParagraph
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen

@Composable
fun QuizScreen(component: QuizComponent) {
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "Quiz") {
        ScreenTitle("Pendiente")
        ScreenParagraph("Quiz ${component.quizId}")
    }
}
