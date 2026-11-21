package org.aula.app.platform

import kotlin.js.js
import kotlinx.browser.window

// Same host the page was opened from, port 3000 (works from localhost and from another device on the network).
actual val defaultApiBaseUrl: String = "${window.location.protocol}//${window.location.hostname}:3000"
actual fun currentTimeMillis(): Long = nowMs().toLong()

private fun nowMs(): Double = js("Date.now()")
