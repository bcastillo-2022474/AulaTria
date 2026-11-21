package org.aula.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import org.aula.app.platform.AndroidAppContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidAppContext.init(this) // before the first App(): session storage needs a Context
        // Once, on the main thread, before setContent: connects Decompose to the system back button (a pushed screen is popped,
        // the app closes only from the first screen) and restores the stack after rotation / process death.
        val componentContext = defaultComponentContext()
        setContent { App(componentContext, baseUrl = BuildConfig.API_BASE_URL) }
    }
}
