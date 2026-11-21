package org.aula.app.feature.auth.login

/** Everything the login screen draws, and nothing it does not. */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    /** Spanish sentence for the last failure, or null. */
    val error: String? = null,
    /** Set when the account signed in fine but belongs to the web app (teacher/admin); the session was NOT kept. */
    val wrongApp: WrongApp? = null,
)

enum class WrongAppRole { Teacher, Admin }

/** A non-student account tried the student app: which role it is and where it should go instead. */
data class WrongApp(val role: WrongAppRole, val url: String)
