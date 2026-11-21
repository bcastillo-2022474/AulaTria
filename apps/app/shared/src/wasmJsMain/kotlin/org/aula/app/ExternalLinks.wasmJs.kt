package org.aula.app.platform

import kotlinx.browser.window

actual fun openExternalUrl(url: String) {
    window.location.href = url
}
