package org.aula.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.webhistory.withWebHistory
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.browser.window
import org.aula.app.feature.RootComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.aula.app.platform.defaultApiBaseUrl

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class, ExperimentalResourceApi::class)
fun main() {
    // The URL is a real path per screen (/login, /courses, /lessons/<id>, ...) handled by RootComponent + Decompose web history.
    // Query parameters live next to the path and are kept on every navigation:
    //   ?gallery  shows the design system;  ?api=http://192.168.0.24:3000  points the browser build at another API.
    val query = window.location.search.removePrefix("?").split("&").filter { it.isNotEmpty() }
        .associate { decodeUri(it.substringBefore("=")) to decodeUri(it.substringAfter("=", "")) }
    // Fonts and other resources are fetched from the site root, not relative to the page: the page lives at nested URLs.
    // Behind the gateway the app lives under /estudiantes (it strips the prefix before serving files), so resources need it too.
    val basePath = webBasePath()
    configureWebResources { resourcePathMapping { path -> "$basePath/$path" } }
    val container = AppContainer(query["api"]?.takeIf { it.isNotBlank() } ?: defaultApiBaseUrl)
    val root = if ("gallery" in query) null else withWebHistory { stateKeeper, deepLink ->
        RootComponent(
            DefaultComponentContext(LifecycleRegistry().apply { resume() }, stateKeeper = stateKeeper),
            container,
            // The router only knows paths without the prefix: strip it from the incoming URL (the mapper adds it back).
            deepLink = deepLink?.replaceFirst("${window.location.origin}$basePath/", "${window.location.origin}/"),
            webParameters = query, webPathPrefix = basePath.removePrefix("/"),
        )
    }
    ComposeViewport { App(container, root) }
}

/** `/estudiantes` when the page is served under that prefix, else empty (dev server at `/`). */
private fun webBasePath(): String {
    val p = window.location.pathname
    return if (p == "/estudiantes" || p.startsWith("/estudiantes/")) "/estudiantes" else ""
}

private fun decodeUri(s: String): String = js("decodeURIComponent(s.replace(/\\+/g, ' '))")
