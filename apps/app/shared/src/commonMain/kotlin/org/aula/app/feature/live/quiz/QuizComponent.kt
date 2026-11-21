package org.aula.app.feature.live.quiz

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aula.app.AppContainer
import org.aula.app.errors.errorCode
import org.aula.app.errors.userMessage
import org.aula.app.feature.componentScope
import org.aula.app.query.MutationRunner
import org.aula.app.query.MutationState
import org.aula.app.query.QueryState
import org.aula.app.query.dataOrNull
import org.aula.app.realtime.LessonLive
import org.aula.app.rpc.errors.v1.ErrorCode
import org.aula.app.rpc.quizzes.v1.AnswerQuizRequest
import org.aula.app.rpc.quizzes.v1.GetQuizRequest
import org.aula.app.rpc.quizzes.v1.Quiz
import org.aula.app.rpc.quizzes.v1.QuizStatus

/**
 * Student quiz (Estudiante-C-Quiz / Estudiante-C-QuizResultado): ONE screen that renders by the quiz status and my answer.
 * The cached `GetQuiz` (2 s poll + instant refresh from the lesson stream) is the source; the only local state is the
 * optimistic letter. The student's quiz never carries the correct answer before CLOSED (the server projects it away).
 */
class QuizComponent(
    componentContext: ComponentContext,
    private val container: AppContainer,
    val lessonId: String,
    val quizId: String,
    val onBack: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val live = LessonLive(container, lessonId, scope).also { it.start() }
    private val answer = MutationRunner(container.queries, container.api.quizMutations.answerQuiz, scope)
    private val getQuiz = container.api.quizQueries.getQuiz(GetQuizRequest(quizId = quizId))

    private val mutableState = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = mutableState.asStateFlow()

    init {
        lifecycle.doOnDestroy { live.stop() }
        scope.launch {
            container.queries.watch(getQuiz).collect { q ->
                mutableState.update { s ->
                    when (q) {
                        QueryState.Loading -> s
                        is QueryState.Success -> s.withQuiz(q.data.quiz)
                        is QueryState.Failure ->
                            if (q.error.errorCode() == ErrorCode.QUIZ_NOT_FOUND) s.copy(loading = false, unavailable = true)
                            else q.staleData?.quiz?.let { s.withQuiz(it) } ?: s.copy(loading = false, loadError = q.error.userMessage())
                    }
                }
            }
        }
        scope.launch {
            live.snapshot.collect { q ->
                val snap = q.dataOrNull()?.snapshot ?: return@collect
                val label = buildString {
                    append(snap.courseName.uppercase())
                    if (snap.number > 0) append(" · CLASE ${snap.number}")
                }
                mutableState.update { it.copy(serverTime = snap.serverTime, courseLabel = label) }
            }
        }
    }

    private fun QuizState.withQuiz(quiz: Quiz?): QuizState {
        if (quiz == null) return copy(loading = false, unavailable = true)
        // Once the server reports my answer (or the quiz closed) the optimistic copy is no longer needed.
        val confirmed = quiz.myLabel.isNotEmpty() || quiz.status == QuizStatus.CLOSED
        return copy(
            loading = false, unavailable = false, loadError = null, quiz = quiz,
            pendingLabel = if (confirmed) "" else pendingLabel, locked = if (confirmed) false else locked,
        )
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.OnOptionClick -> answerWith(event.label)
            QuizEvent.BackClick -> onBack()
            QuizEvent.ToastShown -> mutableState.update { it.copy(toast = null) }
        }
    }

    private fun answerWith(label: String) {
        val s = mutableState.value
        val quiz = s.quiz ?: return
        if (quiz.status != QuizStatus.LIVE || s.myLabel.isNotEmpty() || s.locked) return
        mutableState.update { it.copy(pendingLabel = label, locked = true) }
        answer.reset()
        answer.run(AnswerQuizRequest(quizId = quizId, label = label))
        scope.launch {
            val outcome = answer.state.first { it is MutationState.Success || it is MutationState.Failure }
            if (outcome is MutationState.Failure) onAnswerFailed(outcome.error)
        }
    }

    private fun onAnswerFailed(error: Throwable) {
        when (error.errorCode()) {
            // Already answered (maybe from another device) or closed meanwhile: the server copy is the truth.
            // Keep the options locked (waiting state) until it arrives; refetch now instead of waiting for the poll.
            ErrorCode.QUIZ_ALREADY_ANSWERED, ErrorCode.QUIZ_CLOSED -> {
                mutableState.update { it.copy(pendingLabel = "", locked = true) }
                container.queries.invalidate(getQuiz.key)
            }
            else -> mutableState.update { it.copy(pendingLabel = "", locked = false, toast = error.userMessage()) }
        }
    }
}
