package org.aula.app.feature.live.floor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaCard
import org.aula.app.designsystem.AulaColors
import org.aula.app.designsystem.AulaListCard
import org.aula.app.designsystem.AulaSpacing
import org.aula.app.designsystem.AulaText
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.ButtonKind
import org.aula.app.designsystem.CountdownBar
import org.aula.app.designsystem.CountdownReadout
import org.aula.app.designsystem.EmptyState
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.HeroParagraph
import org.aula.app.designsystem.ListDivider
import org.aula.app.designsystem.QueueRow
import org.aula.app.designsystem.RaiseHandButton
import org.aula.app.designsystem.ReadoutSize
import org.aula.app.designsystem.StatusHero
import org.aula.app.designsystem.StatusKind
import org.aula.app.designsystem.StudentScreen
import org.aula.app.designsystem.formatClock
import org.aula.app.rpc.floor.v1.FloorRound
import org.aula.app.rpc.floor.v1.FloorRoundStatus
import org.aula.app.rpc.floor.v1.FloorTurnState
import org.aula.app.time.rememberCountdown

/** The binder: collects the state and turns every tap into an Event. Draws nothing itself. Imports the design system, never rpc clients. */
@Composable
fun FloorScreen(component: FloorComponent) {
    val state by component.state.collectAsState()
    FloorContent(state = state, onEvent = component::onEvent)
}

/** Seconds of the advisory answer window (the proto: "advisory 15 s countdown"). Only the bar's scale depends on it. */
private const val ANSWER_SECONDS = 15

/** Which board of the design the student sees. Derived from `my_state` and the round status. */
private enum class FloorView { Raise, Queued, Answering, Answered, Correct, Incorrect, Closed }

private fun viewOf(round: FloorRound): FloorView = when (round.myState) {
    FloorTurnState.QUEUED -> FloorView.Queued
    FloorTurnState.ANSWERING -> FloorView.Answering
    FloorTurnState.ANSWERED -> FloorView.Answered
    FloorTurnState.CORRECT -> FloorView.Correct
    FloorTurnState.INCORRECT -> FloorView.Incorrect
    // Never had a turn (or withdrew): raise again while the round is open, otherwise it is over for this student.
    else -> if (round.status == FloorRoundStatus.CLOSED) FloorView.Closed else FloorView.Raise
}

/** Stateless: ALL the layout lives here, and it is what previews/the gallery draw. */
@Composable
fun FloorContent(state: FloorState, onEvent: (FloorEvent) -> Unit) {
    val round = state.round
    // Runs on the server's clock (serverTime from the lesson snapshot). Called unconditionally: it is a composable.
    val left = rememberCountdown(round?.current?.deadlineAt, state.serverTime)
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "Toma de la palabra") {
        when {
            round != null -> when (viewOf(round)) {
                FloorView.Raise -> RaiseBoard(round.prompt, onEvent)
                FloorView.Queued -> QueuedBoard(round, state, state.busy, onEvent)
                FloorView.Answering -> AnsweringBoard(round.prompt, left, state.busy, onEvent)
                FloorView.Answered -> AnsweredBoard(round.prompt)
                FloorView.Correct -> ResultBoard(correct = true, onEvent = onEvent)
                FloorView.Incorrect -> ResultBoard(correct = false, onEvent = onEvent)
                FloorView.Closed -> ClosedBoard(onEvent)
            }
            state.loading -> HelperText("Cargando…")
            else -> {
                EmptyState("No pudimos abrir la ronda", state.error.orEmpty())
                AulaButton("Volver a la clase", { onEvent(FloorEvent.BackClick) }, kind = ButtonKind.Secondary)
            }
        }
        state.error?.let { AulaToast(it) }
    }
}

/** Screen 1 (Estudiante-C-Levantar): the question and the big "Levantar la mano". */
@Composable
private fun ColumnScope.RaiseBoard(prompt: String, onEvent: (FloorEvent) -> Unit) {
    QuestionCard("Pregunta del catedrático", prompt, AulaTheme.type.heading26)
    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
        RaiseHandButton(onClick = { onEvent(FloorEvent.RaiseHandClick) })
    }
    AulaText(
        "El primero en pedirla responde. Si falla, sigue el siguiente.",
        AulaTheme.type.caption,
        Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}

/** Screen 2 (Estudiante-C-Turno): my place in the line, who answers now, the queue, and "Salir de la fila". */
@Composable
private fun ColumnScope.QueuedBoard(round: FloorRound, state: FloorState, busy: Boolean, onEvent: (FloorEvent) -> Unit) {
    val responder = round.current
    QuestionCard("PREGUNTA DEL CATEDRÁTICO", round.prompt, AulaTheme.type.heading24)
    Row(horizontalArrangement = Arrangement.spacedBy(AulaSpacing.OptionGap), verticalAlignment = Alignment.Bottom) {
        AulaText("${round.myPosition}.º", AulaTheme.type.display100)
        AulaText(
            "en la fila.\n" + (responder?.let { "Responde ahora ${it.name}." } ?: "Esperando a quien responde."),
            AulaTheme.type.paragraph,
        )
    }
    AulaListCard(Modifier.fillMaxWidth()) {
        // Position 1 is the responder when there is one; the waiting students follow in order.
        val offset = if (responder != null) 2 else 1
        responder?.let { QueueRow(1, it.name, "respondiendo", self = it.studentId == state.userId) }
        round.queue.forEachIndexed { i, entry ->
            if (responder != null || i > 0) ListDivider()
            val self = entry.studentId == state.userId
            QueueRow(
                position = i + offset,
                name = if (self) "Tú" else entry.name,
                status = if (self) "sigues si falla" else "",
                self = self,
            )
        }
    }
    Box(Modifier.weight(1f))
    AulaButton("Salir de la fila", { onEvent(FloorEvent.LowerHandClick) }, kind = ButtonKind.Secondary, enabled = !busy)
}

/** Screen 3 (Estudiante-C-TuTurno): the 15 s advisory countdown, the question and "Ya respondí". */
@Composable
private fun ColumnScope.AnsweringBoard(prompt: String, left: Int, busy: Boolean, onEvent: (FloorEvent) -> Unit) {
    Column(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AulaSpacing.ScreenGap, Alignment.CenterVertically)) {
        AulaText("ES TU TURNO", AulaTheme.type.eyebrow.copy(color = AulaColors.Ink, fontWeight = FontWeight.ExtraBold))
        CountdownReadout(formatClock(left), size = ReadoutSize.Hero, spoken = "$left segundos")
        CountdownBar(progress = left / ANSWER_SECONDS.toFloat(), color = AulaColors.Ink)
        AulaText("Responde en voz alta", AulaTheme.type.title34)
        AulaText(prompt, AulaTheme.type.note)
    }
    AulaButton("Ya respondí", { onEvent(FloorEvent.FinishMyTurnClick) }, enabled = !busy)
}

/** After "Ya respondí": same frame, no button, no clock (the teacher decides; the 15 s is only advisory). */
@Composable
private fun ColumnScope.AnsweredBoard(prompt: String) {
    Column(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AulaSpacing.ScreenGap, Alignment.CenterVertically)) {
        AulaText("YA RESPONDISTE", AulaTheme.type.eyebrow.copy(color = AulaColors.Ink, fontWeight = FontWeight.ExtraBold))
        AulaText("Tu catedrático está revisando tu respuesta", AulaTheme.type.title34)
        AulaText(prompt, AulaTheme.type.note)
    }
}

/** Screen 5 (Estudiante-C-ResultadoTurno): the verdict. Correct = green check; incorrect = the amber Warning disc. */
@Composable
private fun ColumnScope.ResultBoard(correct: Boolean, onEvent: (FloorEvent) -> Unit) {
    StatusHero(
        kind = if (correct) StatusKind.Success else StatusKind.Warning,
        title = if (correct) "Respuesta correcta" else "No fue correcta",
        modifier = Modifier.weight(1f),
    ) {
        HeroParagraph(
            if (correct) "Tu catedrático marcó tu respuesta como correcta." else "Tu catedrático marcó tu respuesta como incorrecta.",
        )
    }
    AulaButton("Volver a la clase", { onEvent(FloorEvent.BackClick) })
}

/** The round closed while this student never had a turn: a quiet end, no alarm. */
@Composable
private fun ColumnScope.ClosedBoard(onEvent: (FloorEvent) -> Unit) {
    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
        EmptyState("La ronda terminó", "Tu catedrático cerró la palabra para esta pregunta.")
    }
    AulaButton("Volver a la clase", { onEvent(FloorEvent.BackClick) }, kind = ButtonKind.Secondary)
}

/** The question card of screens 1 to 3: a small kicker over the prompt. */
@Composable
private fun QuestionCard(kicker: String, prompt: String, promptStyle: TextStyle) {
    AulaCard(Modifier.fillMaxWidth(), spacing = AulaSpacing.OptionGap) {
        AulaText(kicker, AulaTheme.type.eyebrow)
        AulaText(prompt, promptStyle)
    }
}
