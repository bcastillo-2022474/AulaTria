package org.aula.app.feature.live.attendance

import org.aula.app.platform.DeviceLocation

sealed interface AttendanceEvent {
    data class CodeChange(val text: String) : AttendanceEvent
    /** "Confirmar asistencia": the Screen asks the platform for the location, then sends [Located]. */
    data object ConfirmClick : AttendanceEvent
    /** The platform answered (null = no permission / no fix): the check-in goes to the server with or without it. */
    data class Located(val location: DeviceLocation?) : AttendanceEvent
    /** "Escanear de nuevo": back to the code field, the record is rewritten by the next try. */
    data object ScanAgainClick : AttendanceEvent
    /** Back to the class home. */
    data object BackClick : AttendanceEvent
}
