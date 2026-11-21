package org.aula.app.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.aula.app.platform.StoredSession
import org.aula.app.platform.TokenStorage
import org.aula.app.rpc.SessionHeaders
import org.aula.app.rpc.identity.v1.IdentityServiceClient
import org.aula.app.rpc.identity.v1.RefreshSessionRequest
import org.aula.app.rpc.identity.v1.Role
import org.aula.app.rpc.identity.v1.Session as SessionMessage

/** What the UI needs to know about who is signed in. */
data class Me(val userId: String, val name: String, val role: Role, val schoolId: String, val schoolName: String)

/**
 * The signed-in user: access token + tenant + refresh token, persisted through [TokenStorage] and observable as [me].
 * It implements [SessionHeaders], so the transport attaches Authorization + X-School-Id by itself and, when a call
 * fails with TOKEN_EXPIRED, calls [refresh] once and retries. Screens use `container.session.me`; they never touch tokens.
 */
class SessionHolder(private val storage: TokenStorage = TokenStorage()) : SessionHeaders {
    private val state = MutableStateFlow<Me?>(null)
    val me: StateFlow<Me?> = state
    private var access: String? = null
    private var refreshToken: String? = null
    private var school: String? = null
    var identity: IdentityServiceClient? = null // set by AppContainer (the client needs the transport that needs this holder)

    init {
        storage.load()?.let { s ->
            access = s.accessToken; refreshToken = s.refreshToken; school = s.schoolId
            state.value = Me(s.userId, s.name, Role.fromValue(s.roleValue), s.schoolId, s.schoolName)
        }
    }

    /** After Login / RefreshSession / RegisterStudent / AcceptInvitation. */
    fun start(s: SessionMessage) {
        val u = s.user ?: return
        access = s.accessToken; refreshToken = s.refreshToken; school = u.school?.id
        val me = Me(u.id, u.name, u.role, u.school?.id.orEmpty(), u.school?.name.orEmpty())
        state.value = me
        storage.save(StoredSession(s.accessToken, s.refreshToken, me.userId, me.name, me.role.value, me.schoolId, me.schoolName))
    }

    fun end() { access = null; refreshToken = null; school = null; state.value = null; storage.clear() }

    /** For the logout call only; nothing else should read the refresh token. */
    fun refreshTokenOrNull() = refreshToken

    override fun accessToken() = access
    override fun schoolId() = school
    override suspend fun refresh(): Boolean {
        val token = refreshToken ?: return false
        return try {
            start(identity!!.refreshSession(RefreshSessionRequest(refreshToken = token)).session ?: return false)
            true
        } catch (_: Exception) { end(); false }
    }
}

