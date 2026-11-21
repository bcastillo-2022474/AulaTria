package org.aula.app.platform

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual val defaultApiBaseUrl: String = "http://localhost:3000"
actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
