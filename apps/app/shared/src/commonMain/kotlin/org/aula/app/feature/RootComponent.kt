package org.aula.app.feature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStackWebNavigation
import com.arkivanov.decompose.router.webhistory.WebNavigation
import com.arkivanov.decompose.router.webhistory.WebNavigationOwner
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.aula.app.AppContainer
import org.aula.app.feature.auth.login.LoginComponent
import org.aula.app.feature.courses.courses.CoursesComponent
import org.aula.app.feature.live.attendance.AttendanceComponent
import org.aula.app.feature.live.doubts.DoubtsComponent
import org.aula.app.feature.live.floor.FloorComponent
import org.aula.app.feature.live.home.LessonHomeComponent
import org.aula.app.feature.live.quiz.QuizComponent
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop

/**
 * The router: which screen is on top. Screens do NOT navigate; they receive callbacks (`onLoggedIn`, `onOpenLesson`) and
 * this class decides what they mean, so the whole flow of the app reads in this one file.
 *
 * Adding a screen = four edits (the compiler fails if you forget one): a `Config`, a `Child`, a case in `child()`,
 * a case in RootContent. Always `pushNew`, never `push`: Decompose throws on a duplicate configuration (double tap).
 */
@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    /**
     * The URL (or just the path) the app was opened at: browser build only (`/lessons/<id>`, `/lessons/<id>/floor/<roundId>`, `/lessons/<id>/doubts/<roundId>`, `/lessons/<id>/attendance/<roundId>`,
     * `/lessons/<id>/quiz/<quizId>`); null on Android/iOS. Signed out, it is kept and opened right after login.
     */
    deepLink: String? = null,
    /** Query parameters of the page (`api`, `gallery`): kept on every URL the browser history writes. Empty off the browser. */
    private val webParameters: Map<String, String> = emptyMap(),
    /** Browser build served under a URL prefix (`estudiantes`, no slashes): written in front of every path; "" when served at the root. */
    private val webPathPrefix: String = "",
) : ComponentContext by componentContext, WebNavigationOwner {

    private val navigation = StackNavigation<Config>()

    /** Where the user asked to go while signed out: opened after login. */
    private var pendingAfterLogin: List<Config>? = null

    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = {
            val target = parseDeepLink(deepLink)
            if (container.session.me.value == null) {
                pendingAfterLogin = target
                listOf(Config.Login)
            } else {
                target ?: listOf(Config.Courses)
            }
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    /** Browser build only: keeps the address bar, the back/forward buttons and reload in sync with [stack]. Unused elsewhere. */
    override val webNavigation: WebNavigation<*> = childStackWebNavigation(
        navigator = navigation,
        stack = stack,
        serializer = Config.serializer(),
        pathMapper = { (if (webPathPrefix.isEmpty()) "" else "$webPathPrefix/") + path(it.configuration) },
        parametersMapper = { webParameters },
        // Signed out, the browser buttons cannot take the user back to authenticated screens (e.g. after logout).
        onBeforeNavigate = { container.session.me.value != null },
    )

    companion object {
        /** `/lessons/<id>[/floor/<roundId> | /attendance/<roundId> | /quiz/<quizId>]` -> the stack to build (Courses at the bottom), else null (login, courses, unknown). */
        fun parseDeepLink(link: String?): List<Config>? {
            if (link == null) return null
            val afterHost = if ("://" in link) link.substringAfter("://").substringAfter("/", "") else link
            val p = afterHost.substringBefore("#").substringBefore("?").split("/").filter { it.isNotEmpty() }
            if (p.size < 2 || p[0] != "lessons") return null
            val lesson = Config.Lesson(p[1])
            return when {
                p.size == 2 -> listOf(Config.Courses, lesson)
                p.size == 4 && p[2] == "doubts" -> listOf(Config.Courses, lesson, Config.Doubts(p[1], p[3]))
                p.size == 4 && p[2] == "attendance" -> listOf(Config.Courses, lesson, Config.Attendance(p[1], p[3]))
                p.size == 4 && p[2] == "floor" -> listOf(Config.Courses, lesson, Config.Floor(p[1], p[3]))
                p.size == 4 && p[2] == "quiz" -> listOf(Config.Courses, lesson, Config.Quiz(p[1], p[3]))
                else -> null
            }
        }

        /** The URL path of a screen (browser build). Inverse of [parseDeepLink]. */
        fun path(config: Config): String = when (config) {
            Config.Login -> "login"
            Config.Courses -> "courses"
            is Config.Lesson -> "lessons/${config.lessonId}"
            is Config.Doubts -> "lessons/${config.lessonId}/doubts/${config.roundId}"
            is Config.Attendance -> "lessons/${config.lessonId}/attendance/${config.roundId}"
            is Config.Floor -> "lessons/${config.lessonId}/floor/${config.roundId}"
            is Config.Quiz -> "lessons/${config.lessonId}/quiz/${config.quizId}"
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable data object Login : Config
        @Serializable data object Courses : Config
        @Serializable data class Lesson(val lessonId: String) : Config
        @Serializable data class Doubts(val lessonId: String, val roundId: String) : Config
        @Serializable data class Attendance(val lessonId: String, val roundId: String) : Config
        @Serializable data class Floor(val lessonId: String, val roundId: String) : Config
        @Serializable data class Quiz(val lessonId: String, val quizId: String) : Config
        // ROUTES: add yours here
    }

    sealed interface Child {
        class Login(val component: LoginComponent) : Child
        class Courses(val component: CoursesComponent) : Child
        class Lesson(val component: LessonHomeComponent) : Child
        class Doubts(val component: DoubtsComponent) : Child
        class Attendance(val component: AttendanceComponent) : Child
        class Floor(val component: FloorComponent) : Child
        class Quiz(val component: QuizComponent) : Child
        // CHILDREN: one per Config; each wraps exactly ONE component (even a screen with no state gets one).
    }

    private fun child(config: Config, ctx: ComponentContext): Child = when (config) {
        Config.Login -> Child.Login(LoginComponent(ctx, container, onLoggedIn = { val next = pendingAfterLogin ?: listOf(Config.Courses); pendingAfterLogin = null; navigation.navigate { next } }))
        Config.Courses -> Child.Courses(
            CoursesComponent(
                ctx, container,
                onOpenLesson = { navigation.pushNew(Config.Lesson(it)) },
                onSignOut = { container.signOut(); navigation.replaceAll(Config.Login) },
            ),
        )
        is Config.Lesson -> Child.Lesson(
            LessonHomeComponent(
                ctx, container, config.lessonId,
                onOpenDoubts = { navigation.pushNew(Config.Doubts(config.lessonId, it)) },
                onOpenAttendance = { navigation.pushNew(Config.Attendance(config.lessonId, it)) },
                onOpenFloor = { navigation.pushNew(Config.Floor(config.lessonId, it)) },
                onOpenQuiz = { navigation.pushNew(Config.Quiz(config.lessonId, it)) },
                onLeave = { navigation.pop() },
            ),
        )
        is Config.Doubts -> Child.Doubts(DoubtsComponent(ctx, container, config.lessonId, config.roundId, onBack = { navigation.pop() }))
        is Config.Attendance -> Child.Attendance(AttendanceComponent(ctx, container, config.lessonId, config.roundId, onBack = { navigation.pop() }))
        is Config.Floor -> Child.Floor(FloorComponent(ctx, container, config.lessonId, config.roundId, onBack = { navigation.pop() }))
        is Config.Quiz -> Child.Quiz(QuizComponent(ctx, container, config.lessonId, config.quizId, onBack = { navigation.pop() }))
    }
}
