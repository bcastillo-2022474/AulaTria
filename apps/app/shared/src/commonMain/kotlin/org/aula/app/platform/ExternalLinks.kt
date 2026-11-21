package org.aula.app.platform

/** Opens [url] outside the app (system browser on Android/iOS, navigates the page in the browser build). */
expect fun openExternalUrl(url: String)
