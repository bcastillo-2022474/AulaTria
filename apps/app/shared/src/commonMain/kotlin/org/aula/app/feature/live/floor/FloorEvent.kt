package org.aula.app.feature.live.floor

/** One case per user action on the floor screen. */
sealed interface FloorEvent {
    data object RaiseHandClick : FloorEvent
    data object LowerHandClick : FloorEvent
    data object FinishMyTurnClick : FloorEvent
    data object BackClick : FloorEvent
}
