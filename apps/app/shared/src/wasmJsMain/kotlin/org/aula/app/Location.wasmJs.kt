package org.aula.app.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.math.roundToInt

@Composable
actual fun rememberLocationRequester(): LocationRequester = remember {
    LocationRequester { callback ->
        requestBrowserPosition({ lat, lng, accuracy -> callback(DeviceLocation(lat, lng, accuracy.roundToInt())) }, { callback(null) })
    }
}

/** navigator.geolocation (the browser shows its own permission prompt); [onFail] when there is none, it is denied or it times out. */
private fun requestBrowserPosition(onOk: (Double, Double, Double) -> Unit, onFail: () -> Unit): Unit = js(
    """{
        if (!navigator.geolocation) { onFail(); return; }
        navigator.geolocation.getCurrentPosition(
            function (p) { onOk(p.coords.latitude, p.coords.longitude, p.coords.accuracy); },
            function () { onFail(); },
            { enableHighAccuracy: true, timeout: 10000, maximumAge: 10000 }
        );
    }""",
)
