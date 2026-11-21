package org.aula.app.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import org.aula.app.resources.Res
import org.aula.app.resources.logo_icon
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Empty state: title + explanation + optional action. NOT on a student board (derived from the same tokens;
 * the teacher board "CursosVacio" is the nearest reference).
 */
@Composable
fun EmptyState(title: String, body: String, modifier: Modifier = Modifier, action: @Composable () -> Unit = {}) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)) {
        AulaText(title, AulaTheme.type.heading24, textAlign = TextAlign.Center)
        AulaText(body, AulaTheme.type.paragraph, textAlign = TextAlign.Center)
        action()
    }
}

/** Transient message pill on Ink. NOT on a board (derived); announced politely to screen readers. */
@Composable
fun AulaToast(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier.semantics { liveRegion = LiveRegionMode.Polite }
            .background(AulaColors.Ink, RoundedCornerShape(AulaShapes.Option)).heightIn(min = AulaSizes.MinTouch).padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) { AulaText(text, AulaTheme.type.step, color = AulaColors.OnFill) }
}

/** The AulaTria mark (docs/brand/logo-icon.svg exported to composeResources/drawable/logo_icon.png). */
@Composable
fun BrandMarkSlot(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 56.dp) {
    Image(
        painter = painterResource(Res.drawable.logo_icon),
        contentDescription = "AulaTria",
        modifier = modifier.size(size).clip(RoundedCornerShape(AulaShapes.Input)),
    )
}
