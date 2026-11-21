package org.aula.app

import androidx.compose.runtime.compositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.aula.app.platform.currentTimeMillis
import org.aula.app.query.QueryClient
import org.aula.app.rpc.AulaApi
import org.aula.app.rpc.ConnectTransport
import org.aula.app.session.SessionHolder

/**
 * The composition root, with NO dependency-injection framework: this class builds the few long-lived things once and
 * screens/components receive exactly what they use from it. It is created once, in [App].
 *
 *   container.api        generated: `api.coursesQueries.listCourses()`, `api.coursesMutations.createCourse`, raw clients
 *   container.queries    the cache (QueryClient); screens usually go through `Query.collectAsState()` instead
 *   container.session    who is signed in (`session.me`), sign in/out
 */
class AppContainer(private val baseUrl: String) {
    /** Where teachers/admins belong: the web app, on the API's host at port 5173. */
    val teacherAppUrl: String = teacherAppUrlFor(baseUrl)

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val session = SessionHolder()

    private val http = HttpClient {
        install(HttpTimeout) { requestTimeoutMillis = 20_000; connectTimeoutMillis = 10_000 }
    }
    val transport = ConnectTransport(http, baseUrl, session)
    val api = AulaApi(transport)
    val queries = QueryClient(scope, ::currentTimeMillis)

    init { session.identity = api.identityClient }

    /**
     * Revokes a session that was issued but must NOT be kept (e.g. a teacher signing in to the student app). Logout needs an
     * authenticated caller, so it goes through a throwaway transport carrying that session's token; the app's own session is untouched.
     */
    suspend fun revokeUnkeptSession(issued: org.aula.app.rpc.identity.v1.Session) {
        val headers = object : org.aula.app.rpc.SessionHeaders {
            override fun accessToken() = issued.accessToken
            override fun schoolId() = issued.user?.school?.id
        }
        org.aula.app.rpc.AulaApi(ConnectTransport(http, baseUrl, headers)).identityClient
            .logout(org.aula.app.rpc.identity.v1.LogoutRequest(refreshToken = issued.refreshToken))
    }

    fun signOut() { session.end(); queries.clear() }
}

val LocalAppContainer = compositionLocalOf<AppContainer> { error("wrap the UI in App { } which provides the AppContainer") }

/**
 * The web app runs on the same host as the API, port 5173 (api http://10.0.0.5:3000 -> http://10.0.0.5:5173).
 * Keeps scheme and host, drops path/query. Never throws: if the URL has an unexpected shape, the port is swapped best-effort.
 */
fun teacherAppUrlFor(apiBaseUrl: String): String {
    val webPort = "5173"
    val trimmed = apiBaseUrl.trim()
    // Production: the API is "<teacher host>/api" (native apps) or "<estudiantes.teacher host>/api" (browser build).
    // The teacher app is that host without a leading "estudiantes.". Local/dev hosts keep the port swap below.
    productionTeacherUrl(trimmed)?.let { return it }
    val schemeEnd = trimmed.indexOf("://")
    val scheme = if (schemeEnd > 0) trimmed.substring(0, schemeEnd + 3) else ""
    val rest = trimmed.substring(scheme.length)
    val authority = rest.substringBefore('/').substringBefore('?').substringBefore('#')
    // IPv6 literals look like [::1]:3000: the port separator is the colon after the closing bracket.
    val host = if (authority.startsWith("[")) authority.substringBefore(']') + "]" else authority.substringBefore(':')
    return if (host.isEmpty()) trimmed.replace(Regex(":\\d+"), ":$webPort") else "$scheme$host:$webPort"
}

private fun productionTeacherUrl(apiBaseUrl: String): String? {
    val schemeEnd = apiBaseUrl.indexOf("://")
    if (schemeEnd <= 0) return null
    val scheme = apiBaseUrl.substring(0, schemeEnd + 3)
    val authority = apiBaseUrl.substring(scheme.length).substringBefore('/').substringBefore('?').substringBefore('#')
    val host = authority.substringBefore(':')
    val local = host == "localhost" || host.startsWith("[") || Regex("^\\d+\\.\\d+\\.\\d+\\.\\d+$").matches(host) || !host.contains('.')
    if (local || !apiBaseUrl.removePrefix(scheme).removePrefix(authority).startsWith("/api")) return null
    return scheme + host.removePrefix("estudiantes.")
}
