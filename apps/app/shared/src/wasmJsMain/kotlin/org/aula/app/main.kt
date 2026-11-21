package org.aula.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val params = window.location.search
    ComposeViewport { App(gallery = params.contains("gallery")) }
}
