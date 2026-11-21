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

/**
 * The router: which screen is on top. Screens do NOT navigate; they receive callbacks (`onLoggedIn`, `onOpenLesson`) and
 * this class decides what they mean, so the whole flow of the app reads in this one file.
 *
 * Adding a screen = four edits (the compiler fails if you forget one): a `Config`, a `Child`, a case in `child()`,
 * a case in RootContent. Always `pushNew`, never `push`: Decompose throws on a duplicate configuration (double tap).
 */
class RootComponent(componentContext: ComponentContext, private val container: AppContainer) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = if (container.session.me.value == null) Config.Login else Config.Courses,
        handleBackButton = true,
        childFactory = ::child,
    )

    @Serializable
    sealed interface Config {
        @Serializable data object Login : Config
        @Serializable data object Courses : Config
        // ROUTES: add yours here (e.g. @Serializable data class Lesson(val lessonId: String) : Config)
    }

    sealed interface Child {
        class Login(val component: LoginComponent) : Child
        class Courses(val component: CoursesComponent) : Child
        // CHILDREN: one per Config; each wraps exactly ONE component (even a screen with no state gets one).
    }

    private fun child(config: Config, ctx: ComponentContext): Child = when (config) {
        Config.Login -> Child.Login(LoginComponent(ctx, container, onLoggedIn = { navigation.replaceAll(Config.Courses) }))
        Config.Courses -> Child.Courses(
            CoursesComponent(
                ctx, container,
                onOpenLesson = { /* TODO(contributor): navigation.pushNew(Config.Lesson(it)) once the lesson screens exist */ },
                onSignOut = { container.signOut(); navigation.replaceAll(Config.Login) },
            ),
        )
    }
}
