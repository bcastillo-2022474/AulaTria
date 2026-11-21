package org.aula.app.feature.live.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.aula.app.designsystem.ActionBanner
import org.aula.app.designsystem.ActionChip
import org.aula.app.designsystem.ActionChipText
import org.aula.app.designsystem.AulaAlpha
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaIcons
import org.aula.app.designsystem.AulaListCard
import org.aula.app.designsystem.AulaSizes
import org.aula.app.designsystem.AulaSpacing
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.ButtonKind
import org.aula.app.designsystem.EmptyState
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.ListDivider
import org.aula.app.designsystem.ListRow
import org.aula.app.designsystem.PillLabel
import org.aula.app.designsystem.PulseTile
import org.aula.app.designsystem.PulseTone
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen
import org.aula.app.designsystem.formatClock
import org.aula.app.rpc.courses.v1.LessonStatus
import org.aula.app.rpc.lessons.v1.PulseState
import org.aula.app.time.rememberCountdown

/** The binder: collects the state and turns every tap into an Event. Draws nothing itself. */
@Composable
fun LessonHomeScreen(component: LessonHomeComponent) {
    val state by component.state.collectAsState()
    LessonHomeContent(state = state, onEvent = component::onEvent)
}

/** Stateless: ALL the layout lives here. Estudiante-C-Home while the class is live; a waiting / over board otherwise. */
@Composable
fun LessonHomeContent(state: LessonHomeState, onEvent: (LessonHomeEvent) -> Unit) {
    val stateLabel = when (state.status) {
        LessonStatus.LOBBY -> "Sala abierta"
        LessonStatus.LIVE -> "En clase"
        LessonStatus.ENDED -> "Clase terminada"
        LessonStatus.CANCELLED -> "Sala cerrada"
        else -> "En clase"
    }
    StudentScreen(courseLabel = state.courseLabel.ifEmpty { "AULATRIA" }, stateLabel = stateLabel) {
        when {
            state.status == LessonStatus.LIVE -> LiveBoard(state, onEvent)
            state.status == LessonStatus.LOBBY -> {
                ScreenTitle("Ya estás en la sala")
                HelperText("Tu catedrático empezará la clase en un momento. Esta pantalla cambia sola.")
            }
            state.status == LessonStatus.ENDED || state.status == LessonStatus.CANCELLED -> {
                val cancelled = state.status == LessonStatus.CANCELLED
                EmptyState(
                    if (cancelled) "La sala se cerró" else "La clase terminó",
                    if (cancelled) "Tu catedrático cerró la sala sin empezar la clase." else "Gracias por participar.",
                )
                AulaButton("Volver a mis cursos", { onEvent(LessonHomeEvent.LeaveClick) }, kind = ButtonKind.Secondary)
            }
            state.loading -> HelperText("Cargando…")
            else -> {
                EmptyState("No pudimos abrir la clase", state.loadError.orEmpty())
                AulaButton("Volver a mis cursos", { onEvent(LessonHomeEvent.LeaveClick) }, kind = ButtonKind.Secondary)
            }
        }
        state.toast?.let { AulaToast(it) }
    }
}

/** Estudiante-C-Home: the open round (if any), "¿Cómo vas?" and the two pulse tiles, and the shortcuts. */
@Composable
private fun ColumnScope.LiveBoard(state: LessonHomeState, onEvent: (LessonHomeEvent) -> Unit) {
    // Runs on the server's clock. Called unconditionally: it is a composable (0 when there is no round).
    val doubtLeft = rememberCountdown(state.doubtRound?.closesAt, state.serverTime)

    if (state.floorRoundId != null) {
        ActionBanner("ABIERTA AHORA", "Toma de la palabra", onClick = { onEvent(LessonHomeEvent.OpenFloorClick) })
    }
    if (state.quizId != null) {
        ActionBanner("ABIERTA AHORA", "Quiz", onClick = { onEvent(LessonHomeEvent.OpenQuizClick) })
    }
    // The doubts screen is a later mission: the banner tells the student a round is open, but does not go anywhere.
    state.doubtRound?.let {
        ActionBanner(
            "ABIERTA AHORA", "Ronda de dudas · ${formatClock(doubtLeft)}", onClick = {}, enabled = false,
            trailing = { PillLabel("Pronto") },
        )
    }
    ScreenTitle("¿Cómo vas?", style = AulaTheme.type.title34)
    Column(Modifier.weight(1f).padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(AulaSpacing.TileGap)) {
        PulseTile(
            PulseTone.Ok, "Voy bien", "Todo claro", state.myPulse == PulseState.OK,
            { onEvent(LessonHomeEvent.PulseClick(PulseState.OK)) }, Modifier.weight(1f),
        )
        PulseTile(
            PulseTone.Lost, "Me perdí", "Necesito que repitas", state.myPulse == PulseState.LOST,
            { onEvent(LessonHomeEvent.PulseClick(PulseState.LOST)) }, Modifier.weight(1f),
        )
    }
    HelperText("Tu catedrático ve el total de la clase.")
    // Attendance and doubts have no student screen yet: the rows stay visible (as in the design) but dimmed, with no tap.
    AulaListCard(Modifier.fillMaxWidth().alpha(AulaAlpha.Disabled)) {
        ListRow(
            "Escanear asistencia", subtitle = "Disponible pronto", trailing = null,
            minHeight = AulaSizes.RowHeight, titleStyle = AulaTheme.type.rowTitle, leading = { ActionChip(AulaIcons.Qr) },
        )
        ListDivider()
        ListRow(
            "Dejar una duda en la diapositiva", subtitle = "Disponible pronto", trailing = null,
            minHeight = AulaSizes.RowHeight, titleStyle = AulaTheme.type.rowTitle, leading = { ActionChipText("?") },
        )
    }
}
