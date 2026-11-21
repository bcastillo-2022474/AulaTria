package org.aula.app.feature

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.aula.app.feature.auth.login.LoginScreen
import org.aula.app.feature.courses.courses.CoursesScreen
import org.aula.app.feature.live.floor.FloorScreen
import org.aula.app.feature.live.home.LessonHomeScreen
import org.aula.app.feature.live.quiz.QuizScreen

/** Draws whatever screen the router says is on top. One `when` over Child: the compiler fails if a Child has no screen. */
@Composable
fun RootContent(root: RootComponent) {
    Children(stack = root.stack) { child ->
        when (val c = child.instance) {
            is RootComponent.Child.Login -> LoginScreen(c.component)
            is RootComponent.Child.Courses -> CoursesScreen(c.component)
            is RootComponent.Child.Lesson -> LessonHomeScreen(c.component)
            is RootComponent.Child.Floor -> FloorScreen(c.component)
            is RootComponent.Child.Quiz -> QuizScreen(c.component)
        }
    }
}
