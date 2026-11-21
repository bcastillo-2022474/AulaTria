package org.aula.app.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** A 390x844 phone viewport, the size of the boards. */
@Composable
private fun PhoneFrame(content: @Composable () -> Unit) {
    Box(Modifier.size(AulaSizes.PhoneWidth, AulaSizes.PhoneHeight).clipToBounds().border(1.dp, AulaColors.LineStrong)) { content() }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(Modifier.width(AulaSizes.PhoneWidth - AulaSpacing.ScreenSide * 2), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        AulaText(title, AulaTheme.type.heading24)
        content()
    }
}

@Composable
private fun Label(text: String) = AulaText(text, AulaTheme.type.small)

/** Living spec of the student design system. Add a component here FIRST, then use it in screens. */
@Composable
fun DesignSystemGallery() {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("BD2-4K9P") }
    var doubt by remember { mutableStateOf("") }
    var pulse by remember { mutableStateOf<PulseTone?>(PulseTone.Ok) }
    Box(Modifier.fillMaxWidth().background(AulaColors.Paper)) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(vertical = 24.dp).align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            AulaText("AulaTria · sistema de diseño (estudiante)", AulaTheme.type.title30, Modifier.padding(horizontal = AulaSpacing.ScreenSide))
            BrandMarkSlot()

            Section("Botones") {
                ButtonKind.entries.forEach { k -> AulaButton(k.name, {}, kind = k) }
                AulaButton("Primary deshabilitado", {}, enabled = false)
                AulaButton("Secondary deshabilitado", {}, kind = ButtonKind.Secondary, enabled = false)
                AulaTextLink("Ya tengo cuenta", {})
                Label("Mantén pulsado un botón: el borde inferior se colapsa.")
            }
            Section("Banner de acción") {
                ActionBanner("ABIERTA AHORA", "Ronda de dudas · 30 s", {})
                ActionBanner("EN VIVO AHORA", "Bases de Datos 2", {}, tall = true, trailing = { PillLabel("Entrar") })
            }
            Section("Pulso (seleccionado / sin elegir)") {
                Column(Modifier.height(380.dp).padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(AulaSpacing.TileGap)) {
                    PulseTile(PulseTone.Ok, "Voy bien", "Todo claro", pulse == PulseTone.Ok, { pulse = PulseTone.Ok }, Modifier.weight(1f))
                    PulseTile(PulseTone.Lost, "Me perdí", "Necesito que repitas", pulse == PulseTone.Lost, { pulse = PulseTone.Lost }, Modifier.weight(1f))
                }
            }
            Section("Tarjeta y filas") {
                AulaCard { AulaText("Tarjeta blanca, radio 24, sombra suave", AulaTheme.type.bodyCard) }
                AulaListCard {
                    ListRow("Bases de Datos 2", subtitle = "CC3057", onClick = {}, leading = { CourseChip("B", AulaColors.Info) }, trailing = { AulaBadge("En vivo") })
                    ListDivider(AulaSpacing.DividerInset)
                    ListRow("Sistemas Operativos", subtitle = "CC3064", leading = { CourseChip("S", AulaColors.OkDeep) }, trailing = null)
                }
                AulaListCard {
                    ListRow("Escanear asistencia", onClick = {}, minHeight = AulaSizes.RowHeight, titleStyle = AulaTheme.type.rowTitle, leading = { ActionChip(AulaIcons.Qr) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    CourseChip("B", AulaColors.Info, size = CourseChipSize.Small)
                    CourseChip("B", AulaColors.Info, size = CourseChipSize.Medium)
                    CourseChip("B", AulaColors.Info, size = CourseChipSize.Large)
                }
            }
            Section("Insignias") { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { BadgeTone.entries.forEach { AulaBadge(it.name, tone = it) } } }
            Section("Campos") {
                AulaTextField(name, { name = it }, "Nombre completo", placeholder = "Tu nombre")
                AulaTextField("", {}, "Correo", placeholder = "tu@correo.com", error = "Falta la @")
                AulaTextField("", {}, "Contraseña", placeholder = "Mínimo 8 caracteres", password = true)
                CodeField(code, { code = it }, "Código del curso")
                AulaMultilineField(doubt, { doubt = it }, "Tu duda", Modifier.height(160.dp), placeholder = "Escribe aquí", footer = "${doubt.length} / 280")
            }
            Section("Cuenta atrás") {
                CountdownRow("0:27", 0.6f)
                CountdownRow("0:22", 0.73f, size = ReadoutSize.Chip)
                CountdownReadout("0:15", size = ReadoutSize.Hero)
                CountdownBar(0.5f, color = AulaColors.Ink)
            }
            Section("Opciones de quiz") {
                val texts = listOf("Idle", "Selected", "Correct", "Wrong", "Dimmed", "Disabled")
                QuizOptionState.entries.forEachIndexed { i, s -> QuizOptionRow(QuizSlot.entries[i % 4], texts[i], {}, state = s, compact = true) }
            }
            Section("Fila de turno") {
                QueueList(listOf(QueueEntry("Marta R.", "respondiendo"), QueueEntry("Tú", "sigues si falla", self = true), QueueEntry("Diego A.")))
            }
            Section("Estado y pasos") {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { StatusDisc(StatusKind.Success); StatusDisc(StatusKind.Warning) }
                AulaCard(padding = 20.dp) {
                    StepRow("Código válido", StepState.Done)
                    StepRow("Confirmando que estás en el aula", StepState.Active)
                    StepRow("Registrando tu asistencia", StepState.Pending)
                }
            }
            Section("Escáner y mano") {
                ScanFrame("Apunta a la pantalla del aula", Modifier.fillMaxWidth().height(260.dp))
                RaiseHandButton({})
            }
            Section("Vacío y aviso") {
                EmptyState("Aún no tienes cursos", "Únete con el código de tu catedrático.")
                AulaToast("Duda enviada")
            }

            AulaText("Pantallas completas (comparar con docs/design)", AulaTheme.type.title30, Modifier.padding(horizontal = AulaSpacing.ScreenSide))
            Label("Estudiante-C-Home"); PhoneFrame { HomeScreenSample() }
            Label("Estudiante-C-Quiz"); PhoneFrame { QuizScreenSample() }
            Label("Estudiante-C-Turno"); PhoneFrame { TurnoScreenSample() }
            Label("Estudiante-C-Dudas"); PhoneFrame { DudasScreenSample() }
        }
    }
}
