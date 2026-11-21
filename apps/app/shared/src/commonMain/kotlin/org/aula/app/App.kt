package org.aula.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.DesignSystemGallery
import org.aula.app.feature.RootComponent
import org.aula.app.feature.RootContent
import org.aula.app.platform.defaultApiBaseUrl
import org.aula.app.query.LocalQueryClient

/**
 * Root of the app on every platform (Android, iOS, browser). It builds the [AppContainer] once and provides it to the tree.
 * [gallery] shows the design system instead of the app (browser: add `?gallery` to the URL).
 */
@Composable
fun App(baseUrl: String = defaultApiBaseUrl, gallery: Boolean = false) {
    val container = remember(baseUrl) { AppContainer(baseUrl) }
    AulaTheme {
        CompositionLocalProvider(LocalAppContainer provides container, LocalQueryClient provides container.queries) {
            if (gallery) {
                DesignSystemGallery()
            } else {
                val root = remember(container) { RootComponent(DefaultComponentContext(LifecycleRegistry().apply { resume() }), container) }
                RootContent(root)
            }
        }
    }
}
