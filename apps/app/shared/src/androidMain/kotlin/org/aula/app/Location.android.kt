package org.aula.app.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
private const val TIMEOUT_MS = 10_000L

@Composable
actual fun rememberLocationRequester(): LocationRequester {
    val context = LocalContext.current
    var pending by remember { mutableStateOf<((DeviceLocation?) -> Unit)?>(null) }
    // Android 12+ lets the user grant only "approximate": either one is enough, the accuracy it reports tells the server how good it is.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
        val callback = pending
        pending = null
        if (callback != null) if (granted.values.any { it }) readLocation(context, callback) else callback(null)
    }
    return remember(context) {
        LocationRequester { callback ->
            if (PERMISSIONS.any { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) readLocation(context, callback)
            else { pending = callback; launcher.launch(PERMISSIONS) }
        }
    }
}

/** One fix from GPS (or the network when GPS is off), giving up after [TIMEOUT_MS]. Calls [onResult] exactly once, on the main thread. */
@SuppressLint("MissingPermission")
private fun readLocation(context: Context, onResult: (DeviceLocation?) -> Unit) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return onResult(null)
    val provider = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        .firstOrNull { runCatching { manager.isProviderEnabled(it) }.getOrDefault(false) } ?: return onResult(null)
    var done = false
    val finish = { fix: Location? ->
        if (!done) {
            done = true
            onResult(fix?.let { DeviceLocation(it.latitude, it.longitude, it.accuracy.roundToInt()) })
        }
    }
    Handler(Looper.getMainLooper()).postDelayed({ finish(null) }, TIMEOUT_MS)
    try {
        if (Build.VERSION.SDK_INT >= 30) {
            manager.getCurrentLocation(provider, null, context.mainExecutor) { finish(it) }
        } else {
            // Before API 30 the listener's other three methods are abstract at runtime: implement them all.
            @Suppress("DEPRECATION")
            manager.requestSingleUpdate(provider, object : LocationListener {
                override fun onLocationChanged(location: Location) { finish(location) }
                @Deprecated("Deprecated in Java") override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, Looper.getMainLooper())
        }
    } catch (e: SecurityException) {
        finish(null)
    }
}
