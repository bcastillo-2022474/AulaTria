package org.aula.app.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * NOT IMPLEMENTED on iOS yet (needs CLLocationManager + a delegate + NSLocationWhenInUseUsageDescription in the iOS app,
 * and Xcode to compile and test it). It reports "no location", so on iOS every check-in lands "por revisar" for the teacher.
 */
@Composable
actual fun rememberLocationRequester(): LocationRequester = remember { LocationRequester { it(null) } }
