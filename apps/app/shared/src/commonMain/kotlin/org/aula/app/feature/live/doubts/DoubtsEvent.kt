package org.aula.app.feature.live.doubts

sealed interface DoubtsEvent {
    data class DraftChange(val text: String) : DoubtsEvent
    data object SendClick : DoubtsEvent
    data object EditClick : DoubtsEvent
    /** Back to the class home. */
    data object BackClick : DoubtsEvent
}
