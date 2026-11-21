package org.aula.app.feature.auth.login

/** Everything the login screen draws, and nothing it does not. */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    /** Spanish sentence for the last failure, or null. */
    val error: String? = null,
)
