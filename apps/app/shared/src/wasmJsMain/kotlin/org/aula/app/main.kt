package org.aula.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // ?go=floor/<lessonId>/<roundId> or ?go=quiz/<lessonId>/<quizId> opens that screen right after login (dev aid).
    // ?gallery shows the design system; ?api=http://192.168.0.24:3000 points the browser build at another API.
    val query = window.location.search.removePrefix("?").split("&").filter { it.isNotEmpty() }.associate { it.substringBefore("=") to it.substringAfter("=", "") }
    val api = query["api"]?.takeIf { it.isNotBlank() }
    val go = query["go"]?.replace("%2F", "/")
    ComposeViewport { if (api != null) App(baseUrl = api, gallery = "gallery" in query, go = go) else App(gallery = "gallery" in query, go = go) }
}
