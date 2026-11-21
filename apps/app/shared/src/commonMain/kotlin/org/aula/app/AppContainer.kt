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
class AppContainer(baseUrl: String) {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val session = SessionHolder()

    private val http = HttpClient {
        install(HttpTimeout) { requestTimeoutMillis = 20_000; connectTimeoutMillis = 10_000 }
    }
    val transport = ConnectTransport(http, baseUrl, session)
    val api = AulaApi(transport)
    val queries = QueryClient(scope, ::currentTimeMillis)

    init { session.identity = api.identityClient }

    fun signOut() { session.end(); queries.clear() }
}

val LocalAppContainer = compositionLocalOf<AppContainer> { error("wrap the UI in App { } which provides the AppContainer") }
