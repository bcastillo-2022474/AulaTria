# Decisiones y preguntas abiertas

> Respuestas a las preguntas del equipo. **[DECIDIDO]** ya está en el código o los docs. **[PROPUESTA]** es una recomendación que confirma el dueño del producto. **[ABIERTO]** necesita decisión de una persona. Este documento puede ir directo a la rama principal (`master`), sin PR.

## 1) ¿WebSocket o WatchLesson es el transporte en vivo definitivo?
  RECOMENDACION: WatchLesson es el transporte para el demo. WebSocket queda como plan B, NO se construye ahora.
  - WatchLesson (stream del servidor, ConnectRPC) YA FUNCIONA y esta probado: el smoke test (mage api:smoke) recibe en vivo "sala abierta -> clase en curso -> alumno se une -> pulso 'me perdi'". Sin WebSocket no hay nada que mantener: un solo servidor, un solo hub (apps/api/src/modules/lessons/hub.ts).
  - Si el stream falla en una plataforma, la app NO se rompe: GetLessonSnapshot se consulta cada 3 s (refetch_seconds) y el snapshot trae un contador "revision" para refrescar rondas al instante. Es decir, el peor caso es un retraso de ~3 s, no una pantalla muerta.
  - WebSocket hoy es solo un stub (apps/api/src/realtime/websocket.ts) + el mensaje RealtimeHello en el proto. Hacerlo = servidor (ws) + cliente web + cliente Kotlin (plugin WebSockets de Ktor) = una segunda via a mantener.
  - LO UNICO SIN VERIFICAR: que el stream llegue incremental en iOS (motor Darwin de Ktor) y en el build de navegador de Compose (wasm). En Android (OkHttp) y en la web React (fetch streaming) deberia funcionar, pero tampoco lo probamos en un telefono real todavia.
  - REGLA DE DECISION (1 tarde): probar WatchLesson en un iPhone real y en Safari. Si llega en vivo -> se BORRA el stub de WebSocket y se cierra el tema. Si NO llega -> se implementa WebSocket (el contrato ya esta reservado; es un cambio aditivo, no rompe nada).
  - Por que no WebSocket "por defecto": Ktor documenta WebSocket en iOS, pero eso no prueba que sea mejor aqui; añade codigo y no hay evidencia de que lo necesitemos.

## 2) ¿Convencion de Git y de revision de PR? ¿master o ramas?
  [PROPUESTA] Ramas cortas desde la rama principal + PR + 1 revision. OJO: la rama principal en este repo se llama "master" (origin/master), no "main".
  - Ramas: feat/<tema>, fix/<tema>, docs/<tema>. Commits estilo convencional (feat:, fix:, docs:, chore:), como ya hace el repo.
  - Un PR NO se mergea si falla CI: "mage fast" (contrato sin deriva + typecheck API + build web) y "mage proto:breaking" (contrato congelado).
  - Cambios al contrato (.proto): solo ADITIVOS (campo nuevo, RPC nuevo, valor de enum nuevo, codigo de error nuevo). Renombrar/renumerar/quitar/cambiar tipo = rompe, requiere OK explicito del dueño.
  - Cambios solo de documentacion (docs/*.md): pueden ir directo a la rama principal, sin PR.
  - Todo el trabajo de floor/quiz esta en la rama feat/floor-quiz-backend (sin commitear aun).

## 3) ¿Fecha o alcance del demo que fije el corte?
  [ABIERTO] No hay fecha en el repo. Alcance propuesto: cortar despues de P4 (flujo en vivo, repaso, dudas+IA, quiz). Backend ya hecho: pulso, sala/clase, floor (flujo 3), quiz (flujo 4). Falta backend de: dudas, repaso (review), asistencia, followup, invitaciones/admin, materiales. Falta TODO el frontend de los flujos (solo hay login + cursos como referencia).
  Necesitamos que el dueño diga la fecha para recortar con criterio.

## 4) ¿Que proveedor de LLM, con que clave y presupuesto?
  [ABIERTO] clave y presupuesto: no definidos. [DECIDIDO en codigo] el adaptador (apps/api/src/ai/quiz.ts) llama a la API de Anthropic con ANTHROPIC_API_KEY (modelo por defecto claude-haiku-5-5, configurable con ANTHROPIC_MODEL). SIN clave o ante cualquier fallo usa un generador determinista sin red, asi el demo nunca depende de internet.
  - Uso previsto: 1 llamada por borrador/regeneracion de quiz (y 1 por ronda de dudas cuando se implemente el agrupado). Volumen muy bajo, pero hace falta que el dueño ponga clave y tope de gasto.
  - La ruta con IA real NO se ha probado (no hay clave en este entorno); el fallback es funcional pero flojo (regenerar repite la misma pregunta).

## 5) ¿Como y donde se suben los materiales, y en que formatos?
  [ABIERTO / parcialmente definido] Contrato: RPC UploadMaterial (el PDF viaja en el mensaje, max 15 MB) y ListMaterials. NO esta implementado (stub): ni la subida ni la extraccion de texto del PDF. Formato objetivo: PDF.
  - Riesgo conocido: un mensaje unario de 15 MB puede chocar con el limite de cuerpo del servidor; probablemente haya que cambiar a subida directa a almacenamiento (URL prefirmada) o por partes. Eso SI cambiaria el contrato (aditivo: RPCs nuevos).
  - Para el demo no hace falta: el material de la clase 11 viene sembrado (seed) con el texto de las diapositivas 18, 22 y 27.

## 6) ¿Politica de datos de menores?
  [ABIERTO] No hay politica. El dueño indico que por ahora no es prioridad. Hecho actual: se guarda la ubicacion de cada escaneo de asistencia y los nombres de alumnos; no hay retencion, borrado, ni consentimiento. Hay que decidirla ANTES de usar con alumnos reales (retencion de ubicacion, quien ve nombres, borrado a peticion). Para el demo se usan datos sembrados.

## 7) ¿El email es unico globalmente?
  [DECIDIDO en codigo, a confirmar] Si: columna unica en toda la base y el login no pregunta colegio. Consecuencia: una misma persona no puede tener el mismo email en dos colegios. Cambiarlo despues rompe el login (habria que pedir el colegio): decidirlo ahora.

## 8) ¿"Pruebas: ninguna" tambien despues del demo?
  [DECIDIDO para el demo] sin tests unitarios; la red de seguridad es: contrato verificado en CI, typecheck, y scripts "smoke" de extremo a extremo (mage api:smoke / smoke:floor / smoke:quiz). [ABIERTO] despues del demo: propongo convertir esos smoke en la base de una fase de tests (CI con Postgres) antes de añadir features nuevas. Lo decide el dueño.

## 9) ¿Alcance real de la "administracion de instituciones"?
  [DECIDIDO en codigo] Solo existe un rol admin que invita docentes (ListTeachers, InviteTeacher, RegenerateTeacherInvite). NO hay alta de colegios: el colegio se crea por seed/script. No hay pantallas de admin construidas todavia. Alta de colegios / superadmin queda FUERA del MVP; si se necesita, es trabajo nuevo.

CUENTAS DE DEMO (todas con clave demo1234): ana.morales@sanmarcos.edu.gt (docente), marta.ramirez@sanmarcos.edu.gt (alumna), admin@sanmarcos.edu.gt. Codigo de curso: BD2-4K9P.
