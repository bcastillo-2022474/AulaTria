package org.aula.app.feature

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.aula.app.AppContainer
import org.aula.app.feature.auth.login.LoginComponent
import org.aula.app.feature.courses.courses.CoursesComponent
import org.aula.app.feature.live.floor.FloorComponent
import org.aula.app.feature.live.home.LessonHomeComponent
import org.aula.app.feature.live.quiz.QuizComponent
import com.arkivanov.decompose.router.stack.pop

/**
 * The router: which screen is on top. Screens do NOT navigate; they receive callbacks (`onLoggedIn`, `onOpenLesson`) and
 * this class decides what they mean, so the whole flow of the app reads in this one file.
 *
 * Adding a screen = four edits (the compiler fails if you forget one): a `Config`, a `Child`, a case in `child()`,
 * a case in RootContent. Always `pushNew`, never `push`: Decompose throws on a duplicate configuration (double tap).
 */
class RootComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    /** DEV deep link (browser build: `?go=floor/<lessonId>/<roundId>` or `?go=quiz/<lessonId>/<quizId>`): opened right after login. */
    private val startAt: Config? = null,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = when {
            container.session.me.value == null -> Config.Login
            startAt != null -> startAt
            else -> Config.Courses
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    companion object {
        /** `floor/<lessonId>/<roundId>` or `quiz/<lessonId>/<quizId>` -> a Config, else null. Dev aid until the live home screen links to them. */
        fun parseDeepLink(go: String?): Config? {
            val p = go?.split("/") ?: return null
            return when {
                p.size == 3 && p[0] == "floor" -> Config.Floor(p[1], p[2])
                p.size == 3 && p[0] == "quiz" -> Config.Quiz(p[1], p[2])
                else -> null
            }
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable data object Login : Config
        @Serializable data object Courses : Config
        @Serializable data class Lesson(val lessonId: String) : Config
        @Serializable data class Floor(val lessonId: String, val roundId: String) : Config
        @Serializable data class Quiz(val lessonId: String, val quizId: String) : Config
        // ROUTES: add yours here
    }

    sealed interface Child {
        class Login(val component: LoginComponent) : Child
        class Courses(val component: CoursesComponent) : Child
        class Lesson(val component: LessonHomeComponent) : Child
        class Floor(val component: FloorComponent) : Child
        class Quiz(val component: QuizComponent) : Child
        // CHILDREN: one per Config; each wraps exactly ONE component (even a screen with no state gets one).
    }

    private fun child(config: Config, ctx: ComponentContext): Child = when (config) {
        Config.Login -> Child.Login(LoginComponent(ctx, container, onLoggedIn = { if (startAt != null) navigation.replaceAll(Config.Courses, startAt) else navigation.replaceAll(Config.Courses) }))
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
                onOpenFloor = { navigation.pushNew(Config.Floor(config.lessonId, it)) },
                onOpenQuiz = { navigation.pushNew(Config.Quiz(config.lessonId, it)) },
                onLeave = { navigation.pop() },
            ),
        )
        is Config.Floor -> Child.Floor(FloorComponent(ctx, container, config.lessonId, config.roundId, onBack = { navigation.pop() }))
        is Config.Quiz -> Child.Quiz(QuizComponent(ctx, container, config.lessonId, config.quizId, onBack = { navigation.pop() }))
    }
}
