package org.aula.app

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume

/**
 * Called from iosApp/ContentView.swift. [apiBaseUrl] comes from the Xcode build settings (Config.xcconfig).
 * The lifecycle is created here and resumed for good: the controller lives as long as the app (same as Chassis).
 * No swipe-back gesture is wired yet: screens pop through their own back buttons.
 */
fun MainViewController(apiBaseUrl: String) = ComposeUIViewController {
    val context = androidx.compose.runtime.remember { DefaultComponentContext(LifecycleRegistry().apply { resume() }) }
    App(context, baseUrl = apiBaseUrl)
}
