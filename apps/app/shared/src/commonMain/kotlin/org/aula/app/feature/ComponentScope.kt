package org.aula.app.feature

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * A coroutine scope that lives exactly as long as the component (cancelled when its screen is destroyed). Use it for every
 * launch in a component: `private val scope = componentScope()`. Never GlobalScope.
 */
fun LifecycleOwner.componentScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    lifecycle.doOnDestroy { scope.cancel() }
    return scope
}
