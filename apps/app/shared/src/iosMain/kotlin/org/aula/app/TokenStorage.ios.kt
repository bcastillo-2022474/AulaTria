package org.aula.app.platform

import platform.Foundation.NSUserDefaults

actual class TokenStorage actual constructor() {
    private val d = NSUserDefaults.standardUserDefaults

    actual fun load(): StoredSession? {
        val access = d.stringForKey("aula.access") ?: return null
        return StoredSession(
            accessToken = access, refreshToken = d.stringForKey("aula.refresh") ?: "", userId = d.stringForKey("aula.userId") ?: "",
            name = d.stringForKey("aula.name") ?: "", roleValue = d.integerForKey("aula.role").toInt(),
            schoolId = d.stringForKey("aula.schoolId") ?: "", schoolName = d.stringForKey("aula.schoolName") ?: "",
        )
    }

    actual fun save(session: StoredSession) {
        d.setObject(session.accessToken, "aula.access"); d.setObject(session.refreshToken, "aula.refresh"); d.setObject(session.userId, "aula.userId")
        d.setObject(session.name, "aula.name"); d.setInteger(session.roleValue.toLong(), "aula.role")
        d.setObject(session.schoolId, "aula.schoolId"); d.setObject(session.schoolName, "aula.schoolName")
    }

    actual fun clear() {
        listOf("access", "refresh", "userId", "name", "role", "schoolId", "schoolName").forEach { d.removeObjectForKey("aula.$it") }
    }
}
