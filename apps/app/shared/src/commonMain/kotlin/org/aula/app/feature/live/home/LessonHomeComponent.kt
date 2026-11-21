package org.aula.app.feature.live.home

import com.arkivanov.decompose.ComponentContext
import org.aula.app.AppContainer

/**
 * PLACEHOLDER. TODO(contributor): Estudiante-C-Home (+ joining the room: JoinRoom). Skill: new-mobile-screen.
 * Navigation callbacks are fixed by RootComponent: do not rename them.
 */
class LessonHomeComponent(
    componentContext: ComponentContext,
    val container: AppContainer,
    val lessonId: String,
    val onOpenFloor: (roundId: String) -> Unit,
    val onOpenQuiz: (quizId: String) -> Unit,
    val onLeave: () -> Unit,
) : ComponentContext by componentContext
