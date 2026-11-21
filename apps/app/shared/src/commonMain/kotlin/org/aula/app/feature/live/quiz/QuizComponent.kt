package org.aula.app.feature.live.quiz

import com.arkivanov.decompose.ComponentContext
import org.aula.app.AppContainer

/** PLACEHOLDER. TODO(contributor): Estudiante-C-{Quiz,QuizResultado}. Skill: new-mobile-screen. */
class QuizComponent(
    componentContext: ComponentContext,
    val container: AppContainer,
    val lessonId: String,
    val quizId: String,
    val onBack: () -> Unit,
) : ComponentContext by componentContext
