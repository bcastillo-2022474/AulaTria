package org.aula.app.platform

import android.content.Context

/** Call `AndroidAppContext.init(applicationContext)` once, in MainActivity.onCreate, before the first App(). */
object AndroidAppContext {
    lateinit var app: Context
    fun init(context: Context) { app = context.applicationContext }
}

actual class TokenStorage actual constructor() {
    private val prefs get() = AndroidAppContext.app.getSharedPreferences("aula.session", Context.MODE_PRIVATE)

    actual fun load(): StoredSession? {
        val p = prefs
        val access = p.getString("access", null) ?: return null
        return StoredSession(
            accessToken = access, refreshToken = p.getString("refresh", "") ?: "", userId = p.getString("userId", "") ?: "",
            name = p.getString("name", "") ?: "", roleValue = p.getInt("role", 0), schoolId = p.getString("schoolId", "") ?: "",
            schoolName = p.getString("schoolName", "") ?: "",
        )
    }

    actual fun save(session: StoredSession) {
        prefs.edit().putString("access", session.accessToken).putString("refresh", session.refreshToken).putString("userId", session.userId)
            .putString("name", session.name).putInt("role", session.roleValue).putString("schoolId", session.schoolId)
            .putString("schoolName", session.schoolName).apply()
    }

    actual fun clear() { prefs.edit().clear().apply() }
}
