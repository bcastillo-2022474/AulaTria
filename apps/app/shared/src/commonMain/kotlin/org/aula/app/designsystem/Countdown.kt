package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** "0:27" for [seconds]. */
fun formatClock(seconds: Int): String = "${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}"

/** Track (Line) + fill, 12dp, radius 6. [progress] 0..1 is the filled part. */
@Composable
fun CountdownBar(progress: Float, modifier: Modifier = Modifier, color: Color = AulaColors.Primary) {
    val shape = RoundedCornerShape(AulaShapes.Bar)
    Box(modifier.fillMaxWidth().height(AulaSizes.BarHeight).background(AulaColors.Line, shape)) {
        Box(Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxHeight().background(color, shape))
    }
}

enum class ReadoutSize { Inline, Chip, Hero }

/** The big time readout: Inline = 40sp (quiz), Chip = 36sp on a yellow pill (doubts round), Hero = 110sp (your turn). */
@Composable
fun CountdownReadout(text: String, modifier: Modifier = Modifier, size: ReadoutSize = ReadoutSize.Inline, spoken: String? = null) {
    val a11y = if (spoken != null) Modifier.semantics { contentDescription = spoken } else Modifier
    when (size) {
        ReadoutSize.Inline -> AulaText(text, AulaTheme.type.timer40, modifier.then(a11y))
        ReadoutSize.Hero -> AulaText(text, AulaTheme.type.display110, modifier.then(a11y))
        ReadoutSize.Chip -> Box(modifier.then(a11y).background(AulaColors.Accent, CircleShape).padding(horizontal = 18.dp, vertical = 4.dp)) {
            AulaText(text, AulaTheme.type.title36, color = AulaColors.InkDeep)
        }
    }
}

/** Readout + bar on one line, as on the quiz and doubts boards. */
@Composable
fun CountdownRow(text: String, progress: Float, modifier: Modifier = Modifier, size: ReadoutSize = ReadoutSize.Inline, spoken: String? = null) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(if (size == ReadoutSize.Chip) 14.dp else 16.dp), verticalAlignment = Alignment.CenterVertically) {
        CountdownReadout(text, size = size, spoken = spoken)
        CountdownBar(progress, Modifier.weight(1f))
    }
}
