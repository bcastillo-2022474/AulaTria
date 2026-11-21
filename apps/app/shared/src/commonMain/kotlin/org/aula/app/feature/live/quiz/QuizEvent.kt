package org.aula.app.feature.live.quiz

sealed interface QuizEvent {
    data class OnOptionClick(val label: String) : QuizEvent
    data object BackClick : QuizEvent
    data object ToastShown : QuizEvent
}
