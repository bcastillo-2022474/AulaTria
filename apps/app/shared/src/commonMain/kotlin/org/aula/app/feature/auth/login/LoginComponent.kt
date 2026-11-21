package org.aula.app.feature.auth.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aula.app.AppContainer
import org.aula.app.errors.userMessage
import org.aula.app.platform.openExternalUrl
import org.aula.app.rpc.identity.v1.LoginRequest
import org.aula.app.rpc.identity.v1.Role

/**
 * State and logic of the login screen. NEVER imports androidx.compose.* (that is what keeps it previewable and testable).
 * One public `onEvent`; the handlers behind it are private. REFERENCE IMPLEMENTATION: copy this shape for a form screen.
 */
class LoginComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    private val onLoggedIn: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mutableState = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = mutableState.asStateFlow()

    init { lifecycle.doOnDestroy { scope.cancel() } }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> mutableState.update { it.copy(email = event.value, error = null, wrongApp = null) }
            is LoginEvent.OnPasswordChange -> mutableState.update { it.copy(password = event.value, error = null) }
            LoginEvent.SubmitClick -> submit()
            LoginEvent.OpenTeacherAppClick -> openExternalUrl(container.teacherAppUrl)
        }
    }

    private fun submit() {
        val s = mutableState.value
        if (s.loading) return
        mutableState.update { it.copy(loading = true, error = null, wrongApp = null) }
        scope.launch {
            try {
                val res = container.api.identityClient.login(LoginRequest(email = s.email.trim(), password = s.password))
                val session = res.session ?: error("empty session")
                val wrongRole = when (session.user?.role) {
                    Role.TEACHER -> WrongAppRole.Teacher
                    Role.ADMIN -> WrongAppRole.Admin
                    else -> null
                }
                if (wrongRole != null) {
                    // Never kept: revoke the refresh token the server just issued and send them to the web app.
                    try { container.revokeUnkeptSession(session) } catch (_: Exception) {}
                    mutableState.update { it.copy(loading = false, wrongApp = WrongApp(wrongRole, container.teacherAppUrl)) }
                    return@launch
                }
                container.session.start(session)
                onLoggedIn()
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Throwable) {
                mutableState.update { it.copy(loading = false, error = e.userMessage()) }
            }
        }
    }
}
