package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

enum class PulseTone(val fill: Color, val ink: Color, val pillInk: Color) {
    /** "Voy bien" */
    Ok(AulaColors.OkDeep, AulaColors.OnFill, AulaColors.OkDeep),
    /** "Me perdí" */
    Lost(AulaColors.Accent, AulaColors.InkDeep, AulaColors.InkDeep),
}

/**
 * Big pulse choice (Voy bien / Me perdí). Give it `Modifier.weight(1f)` in a Column so both tiles stretch to
 * fill the screen. Selected = ring in the tile colour (3dp paper gap, 3dp colour) + "Elegido" pill.
 */
@Composable
fun PulseTile(
    tone: PulseTone,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLabel: String = "Elegido",
) {
    PressableFace(
        onClick = onClick,
        fill = tone.fill,
        edge = AulaColors.Edge,
        edgeWidth = AulaSizes.EdgeBanner,
        cornerRadius = AulaShapes.Banner,
        modifier = modifier.fillMaxWidth().semantics { role = Role.RadioButton; this.selected = selected },
        role = Role.RadioButton,
        ringColor = if (selected) tone.fill else Color.Unspecified,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(AulaSizes.PulseDisc).background(AulaColors.OnFillDisc, CircleShape), contentAlignment = Alignment.Center) {
                when (tone) {
                    PulseTone.Ok -> AulaIcon(AulaIcons.Check, tone.ink, size = 32.dp)
                    PulseTone.Lost -> AulaText("?", AulaTheme.type.glyph28, color = tone.ink)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                AulaText(title, AulaTheme.type.pulseTitle, color = tone.ink)
                AulaText(subtitle, AulaTheme.type.note.copy(lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified), color = tone.ink)
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                if (selected) {
                    Box(Modifier.background(AulaColors.OnFill, CircleShape).padding(horizontal = 14.dp, vertical = 5.dp)) {
                        AulaText(selectedLabel, AulaTheme.type.badgeLarge, color = tone.pillInk)
                    }
                }
            }
        }
    }
}
