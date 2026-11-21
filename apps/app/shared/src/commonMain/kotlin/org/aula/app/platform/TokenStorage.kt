package org.aula.app.platform

/** What survives an app restart: enough to restore the signed-in state without a network round trip. */
data class StoredSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val name: String,
    val roleValue: Int,
    val schoolId: String,
    val schoolName: String,
)

/**
 * Persistent storage for the session, per platform (Android SharedPreferences, iOS NSUserDefaults, browser localStorage).
 * MVP-grade on purpose: tokens are not in the Keychain/Keystore yet. Swap the actuals when that matters; nothing else changes.
 */
expect class TokenStorage() {
    fun load(): StoredSession?
    fun save(session: StoredSession)
    fun clear()
}
