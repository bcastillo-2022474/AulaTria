package org.aula.app

import androidx.compose.ui.window.ComposeUIViewController

/** Called from iosApp/ContentView.swift. [apiBaseUrl] comes from the Xcode build settings (Config.xcconfig). */
fun MainViewController(apiBaseUrl: String) = ComposeUIViewController { App(baseUrl = apiBaseUrl) }
