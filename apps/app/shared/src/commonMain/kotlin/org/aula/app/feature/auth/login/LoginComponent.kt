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
import org.aula.app.rpc.identity.v1.LoginRequest

/**
 * State and logic of the login screen. NEVER imports androidx.compose.* (that is what keeps it previewable and testable).
 * One public `onEvent`; the handlers behind it are private. REFERENCE IMPLEMENTATION: copy this shape for a form screen.
 */
class LoginComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    private val onLoggedIn: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutableState = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = mutableState.asStateFlow()

    init { lifecycle.doOnDestroy { scope.cancel() } }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> mutableState.update { it.copy(email = event.value, error = null) }
            is LoginEvent.OnPasswordChange -> mutableState.update { it.copy(password = event.value, error = null) }
            LoginEvent.SubmitClick -> submit()
        }
    }

    private fun submit() {
        val s = mutableState.value
        if (s.loading) return
        mutableState.update { it.copy(loading = true, error = null) }
        scope.launch {
            try {
                val res = container.api.identityClient.login(LoginRequest(email = s.email.trim(), password = s.password))
                container.session.start(res.session ?: error("empty session"))
                onLoggedIn()
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Throwable) {
                mutableState.update { it.copy(loading = false, error = e.userMessage()) }
            }
        }
    }
}
