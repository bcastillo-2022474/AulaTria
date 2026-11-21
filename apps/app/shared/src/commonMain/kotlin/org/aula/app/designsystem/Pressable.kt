package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The "pressable edge" primitive behind every chunky student control: a face of [fill] over a darker bottom
 * [edge] of [edgeWidth] (CSS `border-bottom: Npx solid`). On press the face drops and the edge collapses to
 * [AulaSizes.EdgePressed]. [edge] is painted over the fill, so a translucent edge ([AulaColors.Edge]) darkens it.
 * Optional selected ring = `box-shadow: 0 0 0 3px gap, 0 0 0 6px ringColor`.
 */
@Composable
internal fun PressableFace(
    onClick: () -> Unit,
    fill: Color,
    edge: Color,
    edgeWidth: Dp,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
    minHeight: Dp = AulaSizes.MinTouch,
    enabled: Boolean = true,
    role: Role = Role.Button,
    fadeWhenDisabled: Boolean = true,
    ringColor: Color = Color.Unspecified,
    ringGap: Color = AulaColors.Paper,
    content: @Composable BoxScope.() -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val travel = if (pressed && enabled && edgeWidth > 0.dp) edgeWidth - AulaSizes.EdgePressed else 0.dp
    val fade = when {
        !enabled && fadeWhenDisabled -> AulaAlpha.Disabled
        pressed && edgeWidth == 0.dp -> AulaAlpha.Pressed
        else -> 1f
    }
    Box(
        modifier
            .heightIn(min = minHeight)
            .alpha(fade)
            .drawBehind {
                val r = cornerRadius.toPx()
                if (ringColor != Color.Unspecified) {
                    val outer = 6.dp.toPx()
                    val gap = 3.dp.toPx()
                    drawRoundRect(ringColor, Offset(-outer, -outer), Size(size.width + 2 * outer, size.height + 2 * outer), CornerRadius(r + outer))
                    drawRoundRect(ringGap, Offset(-gap, -gap), Size(size.width + 2 * gap, size.height + 2 * gap), CornerRadius(r + gap))
                }
                if (edgeWidth > 0.dp) {
                    drawRoundRect(fill, cornerRadius = CornerRadius(r))
                    drawRoundRect(edge, cornerRadius = CornerRadius(r))
                }
                if (focused) {
                    val g = AulaSizes.FocusRing.toPx()
                    val o = g + g / 2
                    drawRoundRect(
                        AulaColors.Primary, Offset(-o, -o), Size(size.width + 2 * o, size.height + 2 * o),
                        CornerRadius(r + o), style = Stroke(g),
                    )
                }
            }
            .clickable(interactionSource = source, indication = null, enabled = enabled, role = role, onClick = onClick)
            .padding(top = travel, bottom = edgeWidth - travel)
            .clip(RoundedCornerShape(cornerRadius))
            .background(fill),
        contentAlignment = Alignment.Center,
        content = content,
    )
}
