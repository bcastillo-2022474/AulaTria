package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Draws the focus ring of the boards (`outline: 3px solid primary; outline-offset: 3px`) outside the bounds. */
private fun Modifier.focusRing(focused: Boolean, radius: Dp): Modifier = drawBehind {
    if (focused) {
        val g = AulaSizes.FocusRing.toPx()
        val o = g + g / 2
        drawRoundRect(AulaColors.Primary, Offset(-o, -o), Size(size.width + 2 * o, size.height + 2 * o), CornerRadius(radius.toPx() + o), style = Stroke(g))
    }
}

/** Labelled single-line field (Registro): 56dp, 1.5dp #C7C7CC border, radius 16, Inter 600 17. */
@Composable
fun AulaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    password: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null,
) {
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val shape = RoundedCornerShape(AulaShapes.Input)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AulaText(label, AulaTheme.type.fieldLabel, Modifier.padding(horizontal = AulaSpacing.TextInset))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            interactionSource = source,
            textStyle = AulaTheme.type.fieldText,
            cursorBrush = SolidColor(AulaColors.Primary),
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth().focusRing(focused, AulaShapes.Input),
            decorationBox = { inner ->
                Box(
                    Modifier.fillMaxWidth().height(AulaSizes.FieldHeight).clip(shape).background(AulaColors.Surface)
                        .border(AulaSizes.FieldBorder, if (error != null) AulaColors.Danger else AulaColors.LineStrong, shape)
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) AulaText(placeholder, AulaTheme.type.fieldText, color = AulaColors.InkSoft)
                    inner()
                }
            },
        )
        if (error != null) AulaText(error, AulaTheme.type.small, Modifier.padding(horizontal = AulaSpacing.TextInset), color = AulaColors.Danger)
    }
}

/** Course-code field: 76dp, 2dp primary border, radius 20, centred Nunito 900 34 with 0.12em tracking. */
@Composable
fun CodeField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val shape = RoundedCornerShape(AulaShapes.CodeInput)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        interactionSource = source,
        textStyle = AulaTheme.type.code.copy(textAlign = TextAlign.Center),
        cursorBrush = SolidColor(AulaColors.Primary),
        modifier = modifier.fillMaxWidth().focusRing(focused, AulaShapes.CodeInput)
            .semantics { contentDescription = label },
        decorationBox = { inner ->
            Box(
                Modifier.fillMaxWidth().height(AulaSizes.CodeFieldHeight).clip(shape).background(AulaColors.Surface)
                    .border(AulaSizes.CodeFieldBorder, AulaColors.Primary, shape).padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) { inner() }
        },
    )
}

/**
 * Multi-line field inside a card (Escribir duda): eyebrow [label], Inter 500 20/1.4 text filling the card.
 * [footer] is an optional counter ("120 / 280"); it is not on the board (addition).
 */
@Composable
fun AulaMultilineField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    footer: String? = null,
) {
    AulaCard(modifier, padding = AulaSpacing.CardPaddingCompact, spacing = 10.dp) {
        AulaText(label, AulaTheme.type.eyebrow)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = AulaTheme.type.textarea,
            cursorBrush = SolidColor(AulaColors.Primary),
            modifier = Modifier.fillMaxWidth().weight(1f).heightIn(min = 0.dp),
            decorationBox = { inner ->
                Box(Modifier.fillMaxWidth()) {
                    if (value.isEmpty()) AulaText(placeholder, AulaTheme.type.textarea, color = AulaColors.InkSoft)
                    inner()
                }
            },
        )
        if (footer != null) AulaText(footer, AulaTheme.type.small, Modifier.align(Alignment.End))
    }
}
