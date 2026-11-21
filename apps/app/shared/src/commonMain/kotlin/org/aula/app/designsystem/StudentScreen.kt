package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/**
 * Student screen frame: Paper background, 56dp top / 20dp sides / 28dp bottom, 22dp between blocks, and the
 * header ("CC3057 · CLASE 12" left, the screen state right). On a device replace [AulaSpacing.ScreenTop]
 * by the safe-area inset (the 56dp of the boards is the mock status bar).
 */
@Composable
fun StudentScreen(
    courseLabel: String,
    stateLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier.fillMaxSize().background(AulaColors.Paper)
            .padding(start = AulaSpacing.ScreenSide, end = AulaSpacing.ScreenSide, top = AulaSpacing.ScreenTop, bottom = AulaSpacing.ScreenBottom),
        verticalArrangement = Arrangement.spacedBy(AulaSpacing.ScreenGap),
    ) {
        ScreenHeader(courseLabel, stateLabel)
        content()
    }
}

@Composable
fun ScreenHeader(left: String, right: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().padding(horizontal = AulaSpacing.TextInset), horizontalArrangement = Arrangement.SpaceBetween) {
        AulaText(left, AulaTheme.type.eyebrow)
        AulaText(right, AulaTheme.type.eyebrow)
    }
}

/** Screen title, inset 6dp. */
@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier, style: TextStyle = AulaTheme.type.title34, textAlign: TextAlign? = null) {
    AulaText(text, style, modifier.padding(horizontal = AulaSpacing.TextInset), textAlign = textAlign)
}

/** Grey explanatory paragraph under a title (Inter 16/1.5). */
@Composable
fun ScreenParagraph(text: String, modifier: Modifier = Modifier) {
    AulaText(text, AulaTheme.type.paragraph, modifier.padding(horizontal = AulaSpacing.TextInset))
}

/** Small grey helper line (Inter 13), inset 6dp. */
@Composable
fun HelperText(text: String, modifier: Modifier = Modifier, style: TextStyle = AulaTheme.type.small) {
    AulaText(text, style, modifier.padding(horizontal = AulaSpacing.TextInset))
}
