package org.aula.app.feature.live.floor

import androidx.compose.runtime.Composable
import org.aula.app.designsystem.ScreenParagraph
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen

@Composable
fun FloorScreen(component: FloorComponent) {
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "Toma de la palabra") {
        ScreenTitle("Pendiente")
        ScreenParagraph("Floor ${component.roundId}")
    }
}
