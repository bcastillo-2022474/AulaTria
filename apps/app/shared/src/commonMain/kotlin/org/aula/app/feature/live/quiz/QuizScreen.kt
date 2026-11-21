package org.aula.app.feature.live.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaColors
import org.aula.app.designsystem.AulaSpacing
import org.aula.app.designsystem.AulaText
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.CountdownRow
import org.aula.app.designsystem.EmptyState
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.QuizOptionRow
import org.aula.app.designsystem.QuizOptionState
import org.aula.app.designsystem.QuizSlot
import org.aula.app.designsystem.StudentScreen
import org.aula.app.designsystem.formatClock
import org.aula.app.rpc.quizzes.v1.Quiz
import org.aula.app.rpc.quizzes.v1.QuizOption
import org.aula.app.rpc.quizzes.v1.QuizStatus
import org.aula.app.time.rememberCountdown

/** The binder: collects the state, maps taps to events. */
@Composable
fun QuizScreen(component: QuizComponent) {
    val state by component.state.collectAsState()
    QuizContent(state, component::onEvent)
}

@Composable
fun QuizContent(state: QuizState, onEvent: (QuizEvent) -> Unit) {
    val label = state.courseLabel.ifEmpty { "AULATRIA" }
    val quiz = state.quiz
    StudentScreen(courseLabel = label, stateLabel = "Quiz") {
        when {
            state.unavailable -> Unavailable("Este quiz todavía no está disponible", "Tu catedrático lo lanzará en un momento.", onEvent)
            quiz == null && state.loadError != null -> Unavailable("No pudimos cargar el quiz", state.loadError, onEvent)
            quiz == null -> HelperText("Cargando…")
            quiz.status == QuizStatus.CLOSED -> Result(state, quiz, onEvent)
            quiz.status == QuizStatus.LIVE -> Live(state, quiz, onEvent)
            else -> Unavailable("Este quiz todavía no está disponible", "Tu catedrático lo lanzará en un momento.", onEvent)
        }
    }
}

@Composable
private fun ColumnScope.Unavailable(title: String, body: String, onEvent: (QuizEvent) -> Unit) {
    EmptyState(title, body, Modifier.weight(1f))
    AulaButton("Volver a la clase", { onEvent(QuizEvent.BackClick) })
}

private fun slotOf(label: String): QuizSlot = QuizSlot.entries.firstOrNull { it.letter == label } ?: QuizSlot.A

@Composable
private fun ColumnScope.Live(state: QuizState, quiz: Quiz, onEvent: (QuizEvent) -> Unit) {
    val left = rememberCountdown(quiz.closesAt, state.serverTime)
    val progress = if (quiz.timeLimitSeconds > 0) left.toFloat() / quiz.timeLimitSeconds else 0f
    val mine = state.myLabel
    val answered = mine.isNotEmpty()
    // Not answerable: already answered, sent and waiting, or the time ran out.
    val locked = answered || state.locked || left == 0

    CountdownRow(formatClock(left), progress, spoken = "Quedan $left segundos")
    AulaText(quiz.question, AulaTheme.type.heading26)
    Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(AulaSpacing.OptionGap)) {
        quiz.options.forEach { o: QuizOption ->
            QuizOptionRow(
                slot = slotOf(o.label),
                text = o.text,
                onClick = { onEvent(QuizEvent.OnOptionClick(o.label)) },
                state = when {
                    answered && o.label == mine -> QuizOptionState.Selected
                    locked -> QuizOptionState.Disabled
                    else -> QuizOptionState.Idle
                },
            )
        }
    }
    when {
        answered && left == 0 -> HelperText("Se acabó el tiempo. Esperando el resultado…")
        answered -> HelperText("Respuesta enviada. Esperando el resultado…")
        state.locked -> HelperText("Esperando el resultado…")
        left == 0 -> HelperText("Se acabó el tiempo. Esperando el resultado…")
    }
    ToastLine(state.toast, onEvent)
}

@Composable
private fun ToastLine(text: String?, onEvent: (QuizEvent) -> Unit) {
    if (text == null) return
    LaunchedEffect(text) { delay(4000); onEvent(QuizEvent.ToastShown) }
    AulaToast(text, Modifier.fillMaxWidth())
}

@Composable
private fun ColumnScope.Result(state: QuizState, quiz: Quiz, onEvent: (QuizEvent) -> Unit) {
    val mine = quiz.myLabel
    val correct = mine.isNotEmpty() && mine == quiz.correctLabel
    Column(Modifier.padding(horizontal = AulaSpacing.TextInset), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AulaText(if (mine.isEmpty()) "No respondiste" else "Tu respuesta: $mine", AulaTheme.type.eyebrow)
        when {
            correct -> AulaText("Correcto", AulaTheme.type.hero44, color = AulaColors.OkDeep)
            mine.isEmpty() -> AulaText("Sin respuesta", AulaTheme.type.hero44)
            else -> AulaText("Incorrecto", AulaTheme.type.hero44, color = AulaColors.Danger)
        }
    }
    Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(AulaSpacing.OptionGap)) {
        quiz.options.forEach { o ->
            QuizOptionRow(
                slot = slotOf(o.label),
                text = o.text,
                onClick = {},
                compact = true,
                state = when {
                    o.label == quiz.correctLabel -> QuizOptionState.Correct
                    o.label == mine -> QuizOptionState.Wrong
                    else -> QuizOptionState.Dimmed
                },
            )
        }
    }
    HelperText("${quiz.correctPercent}% de la clase acertó.", style = AulaTheme.type.note)
    AulaButton("Volver a la clase", { onEvent(QuizEvent.BackClick) })
    ToastLine(state.toast, onEvent)
}
