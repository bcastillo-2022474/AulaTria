package org.aula.app.platform

import android.content.Intent
import android.net.Uri

actual fun openExternalUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try { AndroidAppContext.app.startActivity(intent) } catch (_: Throwable) { /* no browser installed: nothing to do */ }
}
