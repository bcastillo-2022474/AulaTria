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
import androidx.compose.ui.unit.dp

/**
 * Full-width call to action on a primary fill: "ABIERTA AHORA / Ronda de dudas · 30 s" + [trailing].
 * [tall] is the "Mis cursos" variant (88dp, 22sp title); default is the in-class banner (76dp, 21sp).
 * [enabled] = false is a banner that shows a state but has no screen to go to yet: dimmed and not pressable.
 */
@Composable
fun ActionBanner(
    kicker: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tall: Boolean = false,
    enabled: Boolean = true,
    trailing: @Composable () -> Unit = { ArrowDisc() },
) {
    PressableFace(
        onClick = onClick,
        fill = AulaColors.Primary,
        edge = AulaColors.PrimaryEdge,
        edgeWidth = AulaSizes.EdgeBanner,
        cornerRadius = AulaShapes.Banner,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        minHeight = if (tall) AulaSizes.BannerHeightTall else AulaSizes.BannerHeight,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(start = if (tall) 22.dp else 20.dp, end = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                AulaText(kicker, AulaTheme.type.kicker)
                AulaText(title, if (tall) AulaTheme.type.bannerTitleTall else AulaTheme.type.bannerTitle, color = AulaColors.OnFill)
            }
            trailing()
        }
    }
}

/** The yellow circle with the arrow at the end of the banner. */
@Composable
fun ArrowDisc(modifier: Modifier = Modifier) {
    Box(modifier.size(AulaSizes.ArrowDisc).background(AulaColors.Accent, CircleShape), contentAlignment = Alignment.Center) {
        AulaIcon(AulaIcons.Arrow, AulaColors.InkDeep, size = 24.dp)
    }
}

/** Yellow text pill ("Entrar") for the banner's [ActionBanner.trailing] slot. */
@Composable
fun PillLabel(text: String, modifier: Modifier = Modifier) {
    Box(modifier.background(AulaColors.Accent, CircleShape).padding(horizontal = 18.dp, vertical = 10.dp)) {
        AulaText(text, AulaTheme.type.pill, color = AulaColors.InkDeep)
    }
}
