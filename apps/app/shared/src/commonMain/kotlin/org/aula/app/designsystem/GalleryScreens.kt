package org.aula.app.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Four full student screens composed only from design-system components, with placeholder data.
// Compare each against docs/design/Estudiante-C-<Name>.dc.html.

/** Estudiante-C-Home */
@Composable
fun HomeScreenSample() {
    var choice by remember { mutableStateOf(PulseTone.Ok) }
    StudentScreen("CC3057 · CLASE 12", "En clase") {
        ActionBanner("ABIERTA AHORA", "Ronda de dudas · 30 s", onClick = {})
        ScreenTitle("¿Cómo vas?", style = AulaTheme.type.title34)
        Column(Modifier.weight(1f).padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(AulaSpacing.TileGap)) {
            PulseTile(PulseTone.Ok, "Voy bien", "Todo claro", choice == PulseTone.Ok, { choice = PulseTone.Ok }, Modifier.weight(1f))
            PulseTile(PulseTone.Lost, "Me perdí", "Necesito que repitas", choice == PulseTone.Lost, { choice = PulseTone.Lost }, Modifier.weight(1f))
        }
        HelperText("Tu catedrático ve el total de la clase.")
        AulaListCard {
            ListRow(
                "Escanear asistencia", onClick = {}, minHeight = AulaSizes.RowHeight, titleStyle = AulaTheme.type.rowTitle,
                leading = { ActionChip(AulaIcons.Qr) },
            )
            ListDivider()
            ListRow(
                "Dejar una duda en la diapositiva", onClick = {}, minHeight = AulaSizes.RowHeight, titleStyle = AulaTheme.type.rowTitle,
                leading = { ActionChipText("?") },
            )
        }
    }
}

/** Estudiante-C-Quiz */
@Composable
fun QuizScreenSample() {
    var picked by remember { mutableStateOf<QuizSlot?>(null) }
    val options = listOf(
        QuizSlot.A to "Disminuye: hay más rutas de acceso",
        QuizSlot.B to "Se mantiene igual",
        QuizSlot.C to "Aumenta: cada índice también se actualiza",
        QuizSlot.D to "Solo aumenta con índices hash",
    )
    StudentScreen("CC3057 · CLASE 12", "Quiz") {
        CountdownRow("0:27", 0.6f, spoken = "27 segundos")
        AulaText("¿Qué ocurre con el costo de un INSERT cuando una tabla tiene muchos índices?", AulaTheme.type.heading26)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(AulaSpacing.OptionGap)) {
            options.forEach { (slot, text) ->
                QuizOptionRow(slot, text, onClick = { picked = slot }, state = if (picked == slot) QuizOptionState.Selected else QuizOptionState.Idle)
            }
        }
    }
}

/** Estudiante-C-Turno */
@Composable
fun TurnoScreenSample() {
    StudentScreen("CC3057 · CLASE 12", "Toma de la palabra") {
        AulaCard(padding = 22.dp, spacing = 10.dp) {
            AulaText("PREGUNTA DEL CATEDRÁTICO", AulaTheme.type.eyebrow)
            AulaText("¿Qué información devuelve EXPLAIN ANALYZE que EXPLAIN solo no da?", AulaTheme.type.heading24)
        }
        Row(Modifier.padding(horizontal = AulaSpacing.TextInset, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            AulaText("2.º", AulaTheme.type.display100, Modifier.alignByBaseline())
            AulaText("en la fila.\nResponde ahora Marta.", AulaTheme.type.paragraph.copy(lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified), Modifier.alignByBaseline())
        }
        QueueList(listOf(QueueEntry("Marta R.", "respondiendo"), QueueEntry("Tú", "sigues si falla", self = true), QueueEntry("Diego A.")))
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(AulaSpacing.ButtonStack)) {
            AulaTextLink("Ya es tu turno →", onClick = {})
            AulaButton("Salir de la fila", onClick = {}, kind = ButtonKind.Secondary)
        }
    }
}

/** Estudiante-C-Dudas */
@Composable
fun DudasScreenSample() {
    var text by remember { mutableStateOf("no entiendo cuándo Postgres decide no usar el índice y hace Seq Scan") }
    StudentScreen("CC3057 · CLASE 12", "Ronda de dudas") {
        CountdownRow("0:22", 0.73f, size = ReadoutSize.Chip, modifier = Modifier.padding(horizontal = AulaSpacing.TextInset), spoken = "22 segundos")
        ScreenTitle("¿Qué te quedó sin entender?", style = AulaTheme.type.title32)
        AulaMultilineField(text, { text = it }, "Tu duda", Modifier.weight(1f))
        HelperText("Se agrupa con las de tus compañeros para el catedrático.")
        AulaButton("Enviar duda", onClick = {})
    }
}
