package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** White card with the soft 1dp shadow (`0 1px 2px rgba(0,0,0,.06)`). */
@Composable
fun AulaCard(
    modifier: Modifier = Modifier,
    padding: Dp = AulaSpacing.CardPadding,
    radius: Dp = AulaShapes.Card,
    spacing: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .drawBehind { drawRoundRect(AulaColors.ShadowSoft, Offset(0f, 1.dp.toPx()), size, CornerRadius(radius.toPx())) }
            .clip(RoundedCornerShape(radius))
            .background(AulaColors.Surface)
            .padding(padding),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
        content = content,
    )
}
