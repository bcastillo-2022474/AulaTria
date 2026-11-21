package org.aula.app.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import org.aula.app.designsystem.AulaButton
import org.aula.app.designsystem.AulaCard
import org.aula.app.designsystem.AulaTextField
import org.aula.app.designsystem.AulaToast
import org.aula.app.designsystem.ScreenParagraph
import org.aula.app.designsystem.ScreenTitle
import org.aula.app.designsystem.StudentScreen

/** The binder: collects the state and turns every tap into an Event. Draws nothing itself. Imports the design system, never rpc.* */
@Composable
fun LoginScreen(component: LoginComponent) {
    val state by component.state.collectAsState()
    LoginContent(state = state, onEvent = component::onEvent)
}

/** Stateless: ALL the layout lives here, and it is what previews/the gallery draw. */
@Composable
fun LoginContent(state: LoginState, onEvent: (LoginEvent) -> Unit) {
    StudentScreen(courseLabel = "AULATRIA", stateLabel = "Iniciar sesión") {
        ScreenTitle("Entra a tu cuenta")
        ScreenParagraph("Usa el correo y la contraseña de tu colegio.")
        AulaCard(Modifier.fillMaxWidth(), spacing = androidx.compose.ui.unit.Dp(16f)) {
            AulaTextField(state.email, { onEvent(LoginEvent.OnEmailChange(it)) }, label = "Correo", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            AulaTextField(state.password, { onEvent(LoginEvent.OnPasswordChange(it)) }, label = "Contraseña", password = true)
        }
        state.error?.let { AulaToast(it) }
        state.wrongApp?.let { wrong ->
            val who = if (wrong.role == WrongAppRole.Admin) "de administrador" else "de docente"
            AulaCard(Modifier.fillMaxWidth(), spacing = androidx.compose.ui.unit.Dp(16f)) {
                ScreenParagraph("Esta cuenta es $who. La app de estudiantes es solo para estudiantes.")
                AulaButton("Ir a la app de docentes", { onEvent(LoginEvent.OpenTeacherAppClick) })
            }
        }
        AulaButton(if (state.loading) "Entrando…" else "Entrar", { onEvent(LoginEvent.SubmitClick) }, enabled = !state.loading && state.email.isNotBlank() && state.password.isNotBlank())
    }
}
