# Design system web (docente y admin)

Fuente: `docs/design/Docente-C-*.dc.html` y `Admin-C-*.dc.html` (22 archivos, 1280x800). Código: `apps/web/src/design-system/`. Galería viva: `/#/design-system`.

## Hallazgos de la auditoría

- **Las 22 pantallas docente/admin usan la superficie "stage" (#3A1B96).** No existe ninguna pantalla "paper" en web; "paper" solo la usan las pantallas de estudiante. Los tokens `paper`, `primary*` y `Card tone="paper"` se conservan por paridad con móvil, pero ningún componente web los necesita.
- En web el botón principal es **accent (amarillo)**, no `primary` (violeta).
- Todas las pantallas llevan "CC3057 · CLASE 12" a la izquierda del AppBar, incluso Admin.
- Solo hay dos SVG en el diseño: play y QR. Chevron `›` y check `✓` son glifos de texto.
- Focus: `outline: 3px solid #FFD23F; outline-offset: 3px`. No hay hover ni pressed en el diseño (es estático).

## Tokens (`tokens.css`)

| Token | Valor | Uso |
|---|---|---|
| stage | #3A1B96 | fondo de todas las pantallas web |
| stage-ink | #D9CEF7 | texto secundario sobre stage (133 usos), AppBar |
| on-stage | #FFFFFF | texto principal sobre stage y tiles |
| ink-deep | #2A1260 | texto sobre amarillo/blanco, módulos del QR |
| surface | #FFFFFF | inputs, QR, caja de código |
| accent / accent-edge | #FFD23F / #D9A700 | botón principal, progreso, primero de la cola, play |
| ok | #3DDC84 | medidor "voy bien", badge Activo, banner de éxito |
| ok-ink | #0B3B1E | texto sobre ok |
| ok-deep | #17753D | tile verde, opción D |
| warn | #FF7A59 | medidor "me perdí", punto Grabando, barras de pulso bajas |
| danger | #D02F38 | tile rojo, opción A |
| info | #1F6FE5 | tile azul, opción B |
| sun | #FFC83D | tile amarillo, opción C (distinto de accent) |
| stage-card | rgba(255,255,255,.12) | cards, grupos de lista, cola, textarea |
| stage-ghost | rgba(255,255,255,.16) | botón ghost |
| stage-track | rgba(255,255,255,.18) | pista de medidor/progreso |
| stage-divider | rgba(255,255,255,.20) | separadores de lista |
| stage-chip | rgba(255,255,255,.28) | letra en tiles, relleno de votos |
| stage-readonly | rgba(255,255,255,.70) | input solo lectura |
| stage-well | rgba(0,0,0,.28) | panel de grabación |
| edge-shade | rgba(0,0,0,.22) | borde inferior "pressable" de tiles, opciones, QR |
| lift | rgba(20,0,70,.25) | color de `shadow-lift` (0 6px 0) |
| primary, primary-edge, primary-tint, ink, ink-soft, paper, line | #5B2EE0 #3F1DB0 #ECE6FF #1C1C1E #6C6C70 #F2F2F7 #E5E5EA | solo estudiante/móvil; sin uso en web |

Radios: card 26, control 18, row 16, stat 22, textarea 20, qr-lg 32, qr 28, letter 10, meter 9, progress 6, bar 4, column 8, pill 999. Sombras: `lift`, `soft`.
Tipografía: Nunito (900 titulares, 800 botones/badges) e Inter. Titulares `text-title-{200..20}` + utilidad `type-title` (900, -.01em, 1.1); cuerpo `text-body-{12..26}`. Título de página dominante: 48 (listas), 44 (live, formularios).
Bordes inferiores: botón 5px, tile de acción 8, opción de quiz 5, stat 6, QR/caja de código 8. Alturas de botón 48/52/54/56/60/64/68 con fuente 16/16/16/17/17/19/20.

## Componentes

`Screen{tone,frame}`, `Content`, `AppBar{left,right,brand}`, `StatusDot`, `LogoSlot` (placeholder; reemplazar con docs/brand/logo-*.png), `Heading{size}`, `Eyebrow`, `Text{size}`, `Button{variant: accent|ghost|primary|ok|danger|link, size, block, loading, href}` (renderiza `<a>` con href), `Card{tone,padding}`, `ActionTile`, `CourseTile`, `StatTile`, `LetterBadge`, `Field{label,hint,error,size}`, `TextArea`, `Badge{ok|accent|chart}`, `CountPill`, `Meter` (role=meter), `Progress` (role=progressbar), `Stat`, `Countdown` (role=timer), `QrFrame{cells,large}`, `CodeBox`, `OptionRow{letter,color,text,correct,count,share,dimmed,height}`, `QueueRow`, `ListGroup`/`ListRow{density 64|76,chevron,trailing,href}`, `Banner{ok|warn|danger}`, `EmptyState{card|page}`, `PulseChart`, `ColumnChart`, `VideoPanel`, `SettingRow`, `PlayIcon`, `ChevronGlyph`, `CheckGlyph`.
Sin dependencias nuevas (todo propio).

## Reglas para añadir un componente

1. Primero en la galería (`Gallery.tsx`), con todos sus estados.
2. Solo tokens: nada de hex/rgba en componentes; si falta un valor, se añade a `tokens.css` con nombre semántico y a esta tabla (avisar al equipo móvil).
3. Un archivo por componente, exportado desde `index.ts`; `<button>`/`<a>` reales y roles ARIA en medidores y temporizadores.

## Verificación

Galería vs originales en Chromium headless 1280x800 (ImageMagick compare, fuzz 6%): Cursos 0 px de diferencia, Palabra 0, Admin-Lista 0.07%, QuizVivo 0.15%, Live 0.2% (1px de desplazamiento vertical en algunos textos). No se comparó el resto de pantallas ni QR/charts contra el original.

## Desviaciones e incertidumbres

- Derivados (no están en el diseño): Button `primary/ok/danger`, `Banner warn/danger`, error de Field, estado pressed (baja 3px, borde 2px), disabled (opacidad .5).
- `Button size="lg"` es alias heredado de 60 porque `features/auth/LoginPage.tsx` lo usa y no se puede tocar.
- No hay avatar ni stepper/segmented en el diseño docente/admin (tiempo por pregunta es solo lectura): no se inventaron. `LetterBadge` es lo más cercano.
- Escala de PulseChart (1.2px por %, umbral 55%) y ColumnChart (11px por interacción): inferidos de las alturas del HTML.
- Dos amarillos: sun #FFC83D (tile/opción C) y accent #FFD23F (botón); ambos del diseño.
- Inter 700 no está cargado (400/500/600), igual que el original.
- `html,body{line-height:normal}`: el diseño no lo define; el preflight de Tailwind lo forzaba a 1.5.
- "CC3057 · CLASE 12" en pantallas Admin: copiado tal cual, probablemente debería ser contextual.
- Falta que `apps/web/src/rpc/hooks.ts` compile siempre: un error TS ajeno apareció una vez durante el trabajo y luego desapareció; el build final pasa.
