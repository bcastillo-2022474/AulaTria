package org.aula.app.platform

import kotlin.js.js

actual val defaultApiBaseUrl: String = "http://localhost:3000"
actual fun currentTimeMillis(): Long = nowMs().toLong()

private fun nowMs(): Double = js("Date.now()")
