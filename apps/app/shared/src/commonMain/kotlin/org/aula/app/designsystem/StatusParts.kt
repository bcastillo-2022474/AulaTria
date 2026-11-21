package org.aula.app.designsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class StatusKind(val fill: Color, val ink: Color) {
    Success(AulaColors.OkDeep, AulaColors.OnFill),
    Warning(AulaColors.Alert, AulaColors.Ink),
}

/** 96dp status disc with its icon (check on green, exclamation on amber). */
@Composable
fun StatusDisc(kind: StatusKind, modifier: Modifier = Modifier, description: String? = null) {
    Box(modifier.size(AulaSizes.StatusDisc).background(kind.fill, CircleShape), contentAlignment = Alignment.Center) {
        AulaIcon(if (kind == StatusKind.Success) AulaIcons.Check else AulaIcons.Alert, kind.ink, size = AulaSizes.StatusIcon, contentDescription = description)
    }
}

/**
 * Centred result block: disc, title, then [content] (paragraph, card, caption). Pass `Modifier.weight(1f)` so it
 * centres in the free space above the bottom buttons.
 */
@Composable
fun StatusHero(
    kind: StatusKind,
    title: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = AulaTheme.type.title34,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically)) {
        StatusDisc(kind)
        AulaText(title, titleStyle, textAlign = TextAlign.Center)
        content()
    }
}

/** Centred grey paragraph under a hero title (max 300dp wide). */
@Composable
fun HeroParagraph(text: String, modifier: Modifier = Modifier) {
    AulaText(text, AulaTheme.type.paragraph, modifier.padding(horizontal = 0.dp).heightIn(max = 400.dp).fillMaxWidth(0.77f), textAlign = TextAlign.Center)
}

enum class StepState { Done, Active, Pending }

/** Row of the "Confirmando tu asistencia" checklist: 60dp, a 28dp marker (check / spinner / empty ring) and a label. */
@Composable
fun StepRow(text: String, state: StepState, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().heightIn(min = AulaSizes.QueueRowHeight),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (state) {
            StepState.Done -> Box(Modifier.size(AulaSizes.StepDisc).background(AulaColors.Primary, CircleShape), contentAlignment = Alignment.Center) {
                AulaIcon(AulaIcons.CheckBold, AulaColors.OnFill, size = 16.dp)
            }
            StepState.Active -> Spinner()
            StepState.Pending -> Box(Modifier.size(AulaSizes.StepDisc).border(2.dp, AulaColors.InkSoft, CircleShape))
        }
        AulaText(text, AulaTheme.type.step, color = if (state == StepState.Pending) AulaColors.InkSoft else AulaColors.Ink)
    }
}

/** 28dp ring: Line track with a primary quarter, rotating. */
@Composable
fun Spinner(modifier: Modifier = Modifier) {
    val turn = rememberInfiniteTransition().animateFloat(0f, 360f, infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart))
    Canvas(modifier.size(AulaSizes.StepDisc).rotate(turn.value)) {
        val w = 3.dp.toPx()
        val inset = w / 2
        val s = Size(size.width - w, size.height - w)
        drawArc(AulaColors.Line, 0f, 360f, false, Offset(inset, inset), s, style = Stroke(w))
        drawArc(AulaColors.Primary, -90f, 90f, false, Offset(inset, inset), s, style = Stroke(w))
    }
}
