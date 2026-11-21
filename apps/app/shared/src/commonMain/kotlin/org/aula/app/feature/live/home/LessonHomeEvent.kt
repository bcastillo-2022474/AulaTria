package org.aula.app.feature.live.home

import org.aula.app.rpc.lessons.v1.PulseState

sealed interface LessonHomeEvent {
    data class PulseClick(val state: PulseState) : LessonHomeEvent
    data object OpenDoubtsClick : LessonHomeEvent
    data object OpenAttendanceClick : LessonHomeEvent
    data object OpenFloorClick : LessonHomeEvent
    data object OpenQuizClick : LessonHomeEvent
    /** Back to "Mis cursos" (the class is over or the room closed). */
    data object LeaveClick : LessonHomeEvent
}
