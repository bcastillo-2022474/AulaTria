package org.aula.app.feature.live.floor

import com.arkivanov.decompose.ComponentContext
import org.aula.app.AppContainer

/** PLACEHOLDER. TODO(contributor): Estudiante-C-{Levantar,Turno,TuTurno,ResultadoTurno}. Skill: new-mobile-screen. */
class FloorComponent(
    componentContext: ComponentContext,
    val container: AppContainer,
    val lessonId: String,
    val roundId: String,
    val onBack: () -> Unit,
) : ComponentContext by componentContext
