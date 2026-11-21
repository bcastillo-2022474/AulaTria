package org.aula.app.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Button kinds. Primary and Secondary are the ones on the student boards; Accent/Ok/Danger are the same
 * pressable model in the other palette colours (not used by a board yet).
 */
enum class ButtonKind(val fill: Color, val ink: Color, val edge: Color, val edgeWidth: Dp, val height: Dp) {
    Primary(AulaColors.Primary, AulaColors.OnFill, AulaColors.PrimaryEdge, AulaSizes.EdgeButton, AulaSizes.ButtonHeight),
    Secondary(AulaColors.Line, AulaColors.Ink, Color.Transparent, 0.dp, AulaSizes.SecondaryHeight),
    Accent(AulaColors.Accent, AulaColors.InkDeep, AulaColors.Edge, AulaSizes.EdgeButton, AulaSizes.ButtonHeight),
    Ok(AulaColors.OkDeep, AulaColors.OnFill, AulaColors.Edge, AulaSizes.EdgeButton, AulaSizes.ButtonHeight),
    Danger(AulaColors.Danger, AulaColors.OnFill, AulaColors.Edge, AulaSizes.EdgeButton, AulaSizes.ButtonHeight),
}

/** Full-width action button: 60dp (56dp for Secondary), radius 18, Nunito 800 17. Disabled = 50% opacity, no press. */
@Composable
fun AulaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    kind: ButtonKind = ButtonKind.Primary,
    enabled: Boolean = true,
    style: TextStyle = AulaTheme.type.button,
) {
    PressableFace(
        onClick = onClick,
        fill = kind.fill,
        edge = kind.edge,
        edgeWidth = kind.edgeWidth,
        cornerRadius = AulaShapes.Button,
        modifier = modifier.fillMaxWidth(),
        minHeight = kind.height,
        enabled = enabled,
    ) {
        Row(
            Modifier.padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) { AulaText(text, style, color = kind.ink, textAlign = TextAlign.Center) }
    }
}

/** Underlined tertiary action ("Ya tengo cuenta", "Ya es tu turno →"). 44dp tall as on the boards. */
@Composable
fun AulaTextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().heightIn(min = AulaSizes.LinkHeight).clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AulaText(text, AulaTheme.type.link.copy(textDecoration = TextDecoration.Underline), textAlign = TextAlign.Center)
    }
}
