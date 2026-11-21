package org.aula.app.feature

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * A coroutine scope that lives exactly as long as the component (cancelled when its screen is destroyed). Use it for every
 * launch in a component (it runs on the MAIN dispatcher: navigation and state updates must; network calls suspend, they do not block): `private val scope = componentScope()`. Never GlobalScope.
 */
fun LifecycleOwner.componentScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    lifecycle.doOnDestroy { scope.cancel() }
    return scope
}
