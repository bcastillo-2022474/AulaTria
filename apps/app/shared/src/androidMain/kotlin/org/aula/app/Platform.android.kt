package org.aula.app.platform

actual val defaultApiBaseUrl: String = "http://10.0.2.2:3000"
actual fun currentTimeMillis(): Long = System.currentTimeMillis()
