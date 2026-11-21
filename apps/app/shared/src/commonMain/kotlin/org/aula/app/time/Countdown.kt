package org.aula.app.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import org.aula.app.platform.currentTimeMillis
import pbandk.wkt.Timestamp

fun Timestamp.toMillis(): Long = seconds * 1000 + nanos / 1_000_000

/**
 * Time-boxed things (doubt round, quiz, floor turn, QR code) are DEADLINES, never "seconds left": count down to the
 * deadline on the SERVER's clock, so a wrong phone clock does not matter. Pass the snapshot's `serverTime`.
 *
 *   val left = rememberCountdown(round.closesAt, snapshot.serverTime)   // whole seconds, 0 when over
 */
@Composable
fun rememberCountdown(deadline: Timestamp?, serverTime: Timestamp?): Int {
    val skew = remember(serverTime) { (serverTime?.toMillis() ?: currentTimeMillis()) - currentTimeMillis() }
    var now by remember { mutableLongStateOf(currentTimeMillis()) }
    LaunchedEffect(deadline) { while (true) { now = currentTimeMillis(); delay(250) } }
    if (deadline == null) return 0
    return maxOf(0L, (deadline.toMillis() - (now + skew) + 999) / 1000).toInt()
}

fun formatClock(seconds: Int): String = "${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}"
