package org.aula.app.feature.live.doubts

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaCard
import org.aula.app.designsystem.AulaMultilineField
import org.aula.app.designsystem.AulaSpacing
import org.aula.app.designsystem.AulaText
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.ButtonKind
import org.aula.app.designsystem.CountdownRow
import org.aula.app.designsystem.EmptyState
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.ReadoutSize
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StatusHero
import org.aula.app.designsystem.StatusKind
import org.aula.app.designsystem.StudentScreen
import org.aula.app.designsystem.formatClock
import org.aula.app.rpc.doubts.v1.DoubtRound
import org.aula.app.rpc.doubts.v1.DoubtRoundStatus
import org.aula.app.time.rememberCountdown
import org.aula.app.time.toMillis

/** The binder: collects the state and turns every tap into an Event. Draws nothing itself. */
@Composable
fun DoubtsScreen(component: DoubtsComponent) {
    val state by component.state.collectAsState()
    DoubtsContent(state = state, onEvent = component::onEvent)
}

/** Which board of the design the student sees. */
private enum class DoubtsView { Form, Sent, Closed }

private fun viewOf(state: DoubtsState, round: DoubtRound): DoubtsView = when {
    round.status != DoubtRoundStatus.OPEN -> DoubtsView.Closed
    round.myDoubt == null || state.editing -> DoubtsView.Form
    else -> DoubtsView.Sent
}

/** Stateless: ALL the layout lives here. Estudiante-C-Dudas, then DudaEnviada, then the closed board (DudaGrupo when grouped). */
@Composable
fun DoubtsContent(state: DoubtsState, onEvent: (DoubtsEvent) -> Unit) {
    val round = state.round
    // Both run on the server's clock and are called unconditionally (composables): the round deadline and my editing window.
    val roundLeft = rememberCountdown(round?.closesAt, state.serverTime)
    val editLeft = rememberCountdown(round?.myDoubt?.editableUntil ?: round?.closesAt, state.serverTime)
    StudentScreen(courseLabel = state.courseLabel.ifEmpty { "AULATRIA" }, stateLabel = "Ronda de dudas") {
        when {
            round != null -> when (viewOf(state, round)) {
                DoubtsView.Form -> FormBoard(state, round, roundLeft, onEvent)
                DoubtsView.Sent -> SentBoard(round, editLeft, onEvent)
                DoubtsView.Closed -> ClosedBoard(round, onEvent)
            }
            state.loading -> HelperText("Cargando…")
            else -> {
                EmptyState("No pudimos abrir la ronda", state.loadError.orEmpty())
                AulaButton("Volver a la clase", { onEvent(DoubtsEvent.BackClick) }, kind = ButtonKind.Secondary)
            }
        }
        state.toast?.let { AulaToast(it) }
    }
}

/** Estudiante-C-Dudas: countdown, the question, the field and "Enviar duda". */
@Composable
private fun ColumnScope.FormBoard(state: DoubtsState, round: DoubtRound, left: Int, onEvent: (DoubtsEvent) -> Unit) {
    // Total = closes_at - opened_at (it grows with every "+30 s"), so the bar needs no local state.
    val total = round.openedAt?.let { (round.closesAt?.toMillis() ?: 0L) - it.toMillis() }?.div(1000f) ?: 0f
    CountdownRow(
        formatClock(left), if (total > 0f) left / total else 0f, size = ReadoutSize.Chip,
        modifier = Modifier.padding(horizontal = AulaSpacing.TextInset), spoken = "$left segundos",
    )
    ScreenTitle("¿Qué te quedó sin entender?", style = AulaTheme.type.title32)
    AulaMultilineField(
        state.draft, { onEvent(DoubtsEvent.DraftChange(it)) }, "Tu duda", Modifier.weight(1f),
        footer = "${state.draft.length} / ${DoubtsState.MAX_LENGTH}",
    )
    HelperText("Se agrupa con las de tus compañeros para el catedrático.")
    AulaButton("Enviar duda", { onEvent(DoubtsEvent.SendClick) }, enabled = state.draft.isNotBlank() && !state.busy && left > 0)
}

/** Estudiante-C-DudaEnviada: confirmation, my text, how long I can still edit, and the way back to the class. */
@Composable
private fun ColumnScope.SentBoard(round: DoubtRound, editLeft: Int, onEvent: (DoubtsEvent) -> Unit) {
    StatusHero(StatusKind.Success, "Duda enviada", Modifier.weight(1f)) {
        AulaCard(Modifier.fillMaxWidth()) { AulaText(round.myDoubt?.text.orEmpty(), AulaTheme.type.paragraph) }
        if (editLeft > 0) AulaText("Puedes editarla ${formatClock(editLeft)} más", AulaTheme.type.small)
    }
    AulaButton("Volver a la clase", { onEvent(DoubtsEvent.BackClick) })
    if (editLeft > 0) AulaButton("Editar mi duda", { onEvent(DoubtsEvent.EditClick) }, kind = ButtonKind.Secondary)
}

/** The round is over (timer or teacher): Estudiante-C-DudaGrupo when my doubt was grouped, otherwise a plain closing board. */
@Composable
private fun ColumnScope.ClosedBoard(round: DoubtRound, onEvent: (DoubtsEvent) -> Unit) {
    val mine = round.myDoubt
    val group = round.myGroup
    StatusHero(if (mine != null) StatusKind.Success else StatusKind.Warning, "La ronda cerró", Modifier.weight(1f)) {
        if (mine != null) {
            AulaCard(Modifier.fillMaxWidth()) { AulaText(mine.text, AulaTheme.type.paragraph) }
            AulaText(
                if (group != null) "Tu duda quedó en un grupo: ${group.title}" else "Tu catedrático verá tu duda.",
                AulaTheme.type.small,
            )
        } else {
            AulaText("No enviaste una duda en esta ronda.", AulaTheme.type.paragraph)
        }
    }
    AulaButton("Volver a la clase", { onEvent(DoubtsEvent.BackClick) })
}
