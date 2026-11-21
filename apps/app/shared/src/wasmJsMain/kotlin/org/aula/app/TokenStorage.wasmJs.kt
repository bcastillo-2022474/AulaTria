package org.aula.app.platform

import kotlinx.browser.localStorage

actual class TokenStorage actual constructor() {
    actual fun load(): StoredSession? {
        val access = localStorage.getItem("aula.access") ?: return null
        return StoredSession(
            accessToken = access, refreshToken = localStorage.getItem("aula.refresh") ?: "", userId = localStorage.getItem("aula.userId") ?: "",
            name = localStorage.getItem("aula.name") ?: "", roleValue = localStorage.getItem("aula.role")?.toIntOrNull() ?: 0,
            schoolId = localStorage.getItem("aula.schoolId") ?: "", schoolName = localStorage.getItem("aula.schoolName") ?: "",
        )
    }

    actual fun save(session: StoredSession) {
        localStorage.setItem("aula.access", session.accessToken); localStorage.setItem("aula.refresh", session.refreshToken)
        localStorage.setItem("aula.userId", session.userId); localStorage.setItem("aula.name", session.name)
        localStorage.setItem("aula.role", session.roleValue.toString()); localStorage.setItem("aula.schoolId", session.schoolId)
        localStorage.setItem("aula.schoolName", session.schoolName)
    }

    actual fun clear() { listOf("access", "refresh", "userId", "name", "role", "schoolId", "schoolName").forEach { localStorage.removeItem("aula.$it") } }
}
