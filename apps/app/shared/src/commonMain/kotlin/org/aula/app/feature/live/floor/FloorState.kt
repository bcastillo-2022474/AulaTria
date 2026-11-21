package org.aula.app.feature.live.floor

import org.aula.app.rpc.floor.v1.FloorRound
import pbandk.wkt.Timestamp

/**
 * Everything the floor screen draws. `round` is null until it has been read (or when it cannot be read at all).
 * `serverTime` lets the countdown run on the server's clock; `userId` finds the student's own row in the queue.
 */
data class FloorState(
    val loading: Boolean = true,
    val round: FloorRound? = null,
    val serverTime: Timestamp? = null,
    val userId: String = "",
    val busy: Boolean = false,
    val error: String? = null,
)
