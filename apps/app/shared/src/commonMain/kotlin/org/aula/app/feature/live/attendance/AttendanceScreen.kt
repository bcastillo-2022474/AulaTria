package org.aula.app.feature.live.attendance

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.ButtonKind
import org.aula.app.designsystem.CodeField
import org.aula.app.designsystem.HeroParagraph
import org.aula.app.designsystem.HelperText
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StatusHero
import org.aula.app.designsystem.StatusKind
import org.aula.app.designsystem.StepRow
import org.aula.app.designsystem.StepState
import org.aula.app.designsystem.StudentScreen
import org.aula.app.platform.rememberLocationRequester
import org.aula.app.rpc.attendance.v1.ReviewReason

/** The binder: collects the state, turns every tap into an Event, and (on "Confirmar") asks the platform for the location. */
@Composable
fun AttendanceScreen(component: AttendanceComponent) {
    val state by component.state.collectAsState()
    val locate = rememberLocationRequester()
    AttendanceContent(state) { event ->
        component.onEvent(event)
        if (event == AttendanceEvent.ConfirmClick) locate.request { component.onEvent(AttendanceEvent.Located(it)) }
    }
}

/** Stateless: ALL the layout lives here. */
@Composable
fun AttendanceContent(state: AttendanceState, onEvent: (AttendanceEvent) -> Unit) {
    StudentScreen(courseLabel = state.courseLabel.ifEmpty { "AULATRIA" }, stateLabel = "Asistencia") {
        when (state.phase) {
            AttendancePhase.Entry -> EntryBoard(state, onEvent)
            AttendancePhase.Locating, AttendancePhase.Checking -> CheckingBoard(state)
            AttendancePhase.Present -> PresentBoard(state, onEvent)
            AttendancePhase.NeedsReview -> NeedsReviewBoard(state, onEvent)
        }
        state.error?.let { AulaToast(it) }
    }
}

/** Estudiante-C-Escanear, without the camera: the code the teacher shows on screen, and the location notice. */
@Composable
private fun ColumnScope.EntryBoard(state: AttendanceState, onEvent: (AttendanceEvent) -> Unit) {
    ScreenTitle("Escribe el código")
    HelperText("Lo ves en la pantalla de tu catedrático, debajo del QR.")
    CodeField(state.code, { onEvent(AttendanceEvent.CodeChange(it)) }, "Código de asistencia")
    HelperText("Usamos tu ubicación solo para confirmar que estás en el aula.")
    HelperText("El escáner con cámara todavía no está disponible.")
    Spacer(Modifier.weight(1f))
    AulaButton("Confirmar asistencia", { onEvent(AttendanceEvent.ConfirmClick) }, enabled = state.code.length >= AttendanceState.MIN_CODE)
    AulaButton("Volver a la clase", { onEvent(AttendanceEvent.BackClick) }, kind = ButtonKind.Secondary)
}

/** Estudiante-C-Verificando: what is happening right now (reading the location, then talking to the server). */
@Composable
private fun ColumnScope.CheckingBoard(state: AttendanceState) {
    ScreenTitle("Confirmando tu asistencia")
    val locating = state.phase == AttendancePhase.Locating
    StepRow("Leyendo tu ubicación", if (locating) StepState.Active else StepState.Done, Modifier.fillMaxWidth())
    StepRow("Registrando tu asistencia", if (locating) StepState.Pending else StepState.Active, Modifier.fillMaxWidth())
}

/** Estudiante-C-Presente. */
@Composable
private fun ColumnScope.PresentBoard(state: AttendanceState, onEvent: (AttendanceEvent) -> Unit) {
    StatusHero(StatusKind.Success, "Presente", Modifier.weight(1f)) {
        if (state.courseLabel.isNotEmpty()) HeroParagraph(state.courseLabel)
    }
    AulaButton("Listo", { onEvent(AttendanceEvent.BackClick) })
}

/** Estudiante-C-PorRevisar: the attendance IS recorded; the teacher decides. "Escanear de nuevo" rewrites it. */
@Composable
private fun ColumnScope.NeedsReviewBoard(state: AttendanceState, onEvent: (AttendanceEvent) -> Unit) {
    val (title, text) = when (state.reason) {
        ReviewReason.TOO_FAR -> "Parece que estás lejos del aula" to "Tu asistencia quedó registrada y tu catedrático la revisará. Si estabas en el aula, escanea de nuevo."
        ReviewReason.LOW_ACCURACY -> "No pudimos precisar tu ubicación" to "Tu asistencia quedó registrada y tu catedrático la revisará. Si estabas en el aula, escanea de nuevo."
        else -> "Falta confirmar tu ubicación" to "Tu asistencia quedó registrada y tu catedrático la revisará. Si estabas en el aula, escanea de nuevo."
    }
    StatusHero(StatusKind.Warning, title, Modifier.weight(1f)) { HeroParagraph(text) }
    AulaButton("Escanear de nuevo", { onEvent(AttendanceEvent.ScanAgainClick) })
    AulaButton("Seguir en clase", { onEvent(AttendanceEvent.BackClick) }, kind = ButtonKind.Secondary)
}
