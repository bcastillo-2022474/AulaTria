package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class BadgeTone(val fill: Color, val ink: Color) {
    /** "En vivo" on the board. */
    Live(AulaColors.OkDeep, AulaColors.OnFill),
    Neutral(AulaColors.Line, AulaColors.InkSoft),
    Primary(AulaColors.PrimaryTint, AulaColors.Primary),
    Warn(AulaColors.Alert, AulaColors.Ink),
    Danger(AulaColors.Danger, AulaColors.OnFill),
}

/** Small pill label: Nunito 800 12, padding 3x10. */
@Composable
fun AulaBadge(text: String, modifier: Modifier = Modifier, tone: BadgeTone = BadgeTone.Live) {
    Box(modifier.background(tone.fill, CircleShape).padding(horizontal = 10.dp, vertical = 3.dp)) {
        AulaText(text, AulaTheme.type.badge, color = tone.ink)
    }
}
