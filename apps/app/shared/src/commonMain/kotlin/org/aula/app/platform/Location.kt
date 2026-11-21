package org.aula.app.platform

import androidx.compose.runtime.Composable

/** Where the device is, as the platform reported it. [accuracyM] is the platform's radius of uncertainty, in metres. */
data class DeviceLocation(val lat: Double, val lng: Double, val accuracyM: Int)

/**
 * Asks the platform for the device location ONCE, showing its permission prompt when needed. [onResult] gets null when the
 * user said no, the platform has none, or it took too long: attendance then lands "por revisar", it never fails.
 */
fun interface LocationRequester {
    fun request(onResult: (DeviceLocation?) -> Unit)
}

/** A composable because Android needs an activity to ask for the permission; call it in the Screen, never in a component. */
@Composable
expect fun rememberLocationRequester(): LocationRequester
