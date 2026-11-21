package org.aula.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import org.aula.app.designsystem.AulaTheme
import org.aula.app.designsystem.DesignSystemGallery
import org.aula.app.feature.RootComponent
import org.aula.app.feature.RootContent
import org.aula.app.platform.defaultApiBaseUrl
import org.aula.app.query.LocalQueryClient

/**
 * Root of the app on Android and iOS. The platform entry creates the root [componentContext] ONCE (Android:
 * `defaultComponentContext()`, which wires the system back button and survives rotation/process death) and passes it in.
 */
@Composable
fun App(componentContext: ComponentContext, baseUrl: String = defaultApiBaseUrl) {
    val container = remember(baseUrl) { AppContainer(baseUrl) }
    val root = remember(container) { RootComponent(componentContext, container) }
    App(container, root)
}

/**
 * Lower-level root: the caller owns the [container] and the [root] (the browser entry needs the root before composing, to connect
 * it to the browser history). [root] == null shows the design system gallery instead of the app (browser: `?gallery`).
 */
@Composable
fun App(container: AppContainer, root: RootComponent?) {
    AulaTheme {
        CompositionLocalProvider(LocalAppContainer provides container, LocalQueryClient provides container.queries) {
            if (root == null) DesignSystemGallery() else RootContent(root)
        }
    }
}
