package org.aula.app.feature.auth.login

/** One case per thing the user can do. `OnXChange` for a field, `XClick` for a tap. */
sealed interface LoginEvent {
    data class OnEmailChange(val value: String) : LoginEvent
    data class OnPasswordChange(val value: String) : LoginEvent
    data object SubmitClick : LoginEvent
}
