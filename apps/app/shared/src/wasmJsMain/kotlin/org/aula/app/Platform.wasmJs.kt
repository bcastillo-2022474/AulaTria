package org.aula.app.platform

import kotlin.js.js
import kotlinx.browser.window

private val isLocalHost: Boolean = window.location.hostname.let {
    it == "localhost" || it == "127.0.0.1" || it == "[::1]" || Regex("^\\d+\\.\\d+\\.\\d+\\.\\d+$").matches(it)
}

/** Dev: the API on the same host, port 3000. Anywhere else (the deployed gateway): the page's own origin under /api. */
actual val defaultApiBaseUrl: String =
    if (isLocalHost) "${window.location.protocol}//${window.location.hostname}:3000" else "${window.location.origin}/api"

actual fun currentTimeMillis(): Long = nowMs().toLong()

private fun nowMs(): Double = js("Date.now()")
