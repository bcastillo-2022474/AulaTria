# Mobile (student) design system

Source of truth: `docs/design/Estudiante-C-*.dc.html` (20 boards, 390x844; inline `style=` attributes). Code:
`apps/app/shared/src/commonMain/kotlin/org/aula/app/designsystem/`. Foundation-only (BasicText, BasicTextField,
clickable, Canvas): no Material and no extra dependency were added, because every control on the boards is a custom
"chunky" shape that Material would only have to be stripped of. 1 CSS px = 1 dp/sp.

## Rules for adding a component
1. Add it to `DesignSystemGallery.kt` first, in every state, then use it in screens.
2. Tokens only: no colour/radius/size literal outside `AulaTokens.kt` (type sizes live in `AulaTheme.kt`). Need a new value? Add a named token and list it here.
3. Stateless: state is hoisted; previews are gallery sections.
4. Anything tappable: `Role`, >= 56dp (links on the boards are 44dp, kept), icon-only things get a `contentDescription`.
5. Anything not on a board is marked "derived" in KDoc and in the list below.

## Tokens (AulaColors)
| Token | Value | Used for |
|---|---|---|
| Primary / PrimaryEdge / PrimaryTint | #5B2EE0 / #3F1DB0 / #ECE6FF | main buttons, banner, focus ring, bars / their bottom edge / icon chips, own queue row |
| PrimaryKicker | #EDE7FF | banner kicker text |
| Accent (InkDeep on it) | #FFC83D / #2A1260 | arrow disc, "Entrar", timer chip, "Me perdí", quiz C |
| Ok / OkDeep | #3DDC84 / #17753D | Ok unused on student boards; OkDeep = "Voy bien", status check, "En vivo", quiz D |
| Danger, Info | #D02F38, #1F6FE5 | quiz A, B (Info also course chip) |
| Alert | #FF9F0A | "Falta confirmar ubicación" disc |
| Warn | #FF7A59 | web parity only (teacher), unused here |
| Ink / InkSoft / Paper / Surface | #1C1C1E / #6C6C70 / #F2F2F7 / #FFFFFF | text / secondary text / page / cards |
| Line / LineStrong | #E5E5EA / #C7C7CC | tracks, dividers, secondary button / resting field border |
| Stage / StageInk, AccentEdge | #3A1B96 / #D9CEF7, #D9A700 | web parity, unused on student boards |
| Edge | rgba(0,0,0,.22) | pressable edge on every non-primary fill (painted over the fill) |
| OnFill, OnFillDisc, OnFillChip | #FFF, rgba(255,255,255,.30), .28 | content on fills; pulse glyph disc; quiz letter disc |
| PulseHaloOuter/Inner, ShadowSoft | rgba(91,46,224,.08/.14), rgba(0,0,0,.06) | raise-hand halos, card shadow `0 1px 2px` |
| Camera, CameraInk, CameraFrame | #0F0F10, #F2F1EC, #FFF | scan frame |
| QuizA..D | Danger, Info, Accent, OkDeep | answer slots |

Shapes (AulaShapes, dp): Banner 26, Card 24, CardLarge 26, CardCompact 20, Button 18, Option 16, Input 16, CodeInput 20,
Chip 12/14/16 (40/44/52 chips), Camera 24, CameraCorner 14, Bar 6, Pill 999.
Spacing: ScreenTop 56, ScreenSide 20, ScreenBottom 28, ScreenGap 22, TextInset 6, TileGap 16, OptionGap 10, RowPadding 18.
Sizes: button 60 (secondary 56), link 44, banner 76/88, row 64/68, option 76/64, queue row 60, field 56, code field 76, bar 12,
status disc 96, raise button 200 (halo 310). Edges: button 5, banner/pulse 6, option 5, raise 8; pressed edge 1.
Type (AulaType, Nunito = display, Inter = body): display110/100, hero48/44, timer40, title36/34/32/30, heading26/24 (all Nunito 900,
-0.01em, line-height 1.1 except 110 = .95 and 100 = .9); bannerTitle 21/22, pulseTitle 30, raiseLabel 24, code 34 (+0.12em),
button 17 (800), option 17/1.2, badge 12/13, eyebrow (Inter 13, +0.04em), kicker (Inter 700 13, +0.06em), paragraph 16/1.5,
note 15, caption 14, small 13, rowTitle 17/600, rowTitleStrong 17/700, queueName 17/500|800, fieldLabel 14/700, fieldText 17/600, textarea 20/500/1.4.

## Components
| File | API |
|---|---|
| Pressable.kt | `PressableFace` (internal): edge + press collapse + selected ring + focus ring |
| AulaButton.kt | `AulaButton(text, onClick, modifier, kind: ButtonKind{Primary,Secondary,Accent*,Ok*,Danger*}, enabled, style)`, `AulaTextLink(text, onClick)` |
| ActionBanner.kt | `ActionBanner(kicker, title, onClick, modifier, tall, enabled, trailing)` (`enabled = false`: dimmed, no tap), `ArrowDisc()`, `PillLabel(text)` |
| PulseTile.kt | `PulseTile(tone: PulseTone{Ok,Lost}, title, subtitle, selected, onClick, modifier, selectedLabel)`; give it `weight(1f)` |
| AulaCard.kt | `AulaCard(modifier, padding, radius, spacing, content)` |
| ListRow.kt | `AulaListCard`, `ListRow(title, modifier, subtitle, onClick, leading, trailing, minHeight, titleStyle)`, `ListDivider(inset)`, `ChevronEnd`, `IconChip`, `ActionChip(icon)`, `ActionChipText(glyph)`, `CourseChip(letter, color, modifier, size)` |
| AulaBadge.kt | `AulaBadge(text, modifier, tone: BadgeTone{Live,Neutral*,Primary*,Warn*,Danger*})` |
| AulaFields.kt | `AulaTextField(value, onChange, label, modifier, placeholder, password, keyboardOptions, error*)`, `CodeField(value, onChange, label)`, `AulaMultilineField(value, onChange, label, modifier, placeholder, footer*)` |
| Countdown.kt | `formatClock(seconds)`, `CountdownBar(progress, modifier, color)`, `CountdownReadout(text, modifier, size: ReadoutSize{Inline,Chip,Hero}, spoken)`, `CountdownRow(...)` |
| StudentScreen.kt | `StudentScreen(courseLabel, stateLabel, modifier, content)`, `ScreenHeader`, `ScreenTitle`, `ScreenParagraph`, `HelperText` |
| QuizOption.kt | `QuizSlot{A..D}`, `QuizOptionState{Idle,Selected*,Correct,Wrong*,Dimmed,Disabled}`, `QuizOptionRow(slot, text, onClick, modifier, state, compact)` |
| QueueRow.kt | `QueueRow(position, name, status, modifier, self)`, `QueueEntry`, `QueueList(entries)` |
| StatusParts.kt | `StatusKind{Success,Warning}`, `StatusDisc`, `StatusHero(kind, title, modifier, titleStyle, content)`, `HeroParagraph`, `StepRow(text, StepState{Done,Active,Pending})`, `Spinner` |
| ScanAndRaise.kt | `ScanFrame(hint, modifier, onClick, preview)`, `RaiseHandButton(onClick, modifier, label, description)` |
| Extras.kt | `EmptyState*`, `AulaToast*`, `BrandMarkSlot` (marked slot for the logo in docs/brand/) |
| AulaIcons.kt | `AulaIcons.{Check,CheckBold,Cross*,Alert,Qr,Hand,Arrow,Chevron}`, `AulaIcon(icon, tint, modifier, size, contentDescription)` |

`*` = derived or an addition, not on any student board.

## Inconsistencies in the boards and the value picked
- Primary button height 60 vs secondary 56 vs "Ver caso sin ubicación" 52 (font 16): kept 60 / 56; the 52 board is normalised to 56 (>= touch floor).
- Banner 76dp/21sp (Home) vs 88dp/22sp (Mis cursos): both kept, `tall` flag.
- List rows 64 with 17/600 titles (Home) vs 68 with 17/700 + subtitle (Cursos): both kept as parameters.
- Card radius 24 almost everywhere, 26 on "¿Es este tu curso?", 20 on the Registro course card: three named shapes.
- Accent is #FFC83D on student boards, #FFD23F in the web tokens (teacher boards use #FFD23F once): mobile uses #FFC83D; this needs a decision by whoever owns the web tokens.
- Status amber #FF9F0A (student) vs Warn #FF7A59 (teacher): both tokens (Alert, Warn).
- Verificando card has the typo `padding:8px 20pxpx`; the browser drops it. Used 20dp horizontal / 8dp vertical.
- Home draws "Elegido" ring on the tile colour; ring = 3dp Paper gap + 3dp colour, the list column has 6dp vertical padding for it.

## Deviations
- Glyphs "✓ → › ✕" in the boards are text; here they are drawn icons (font fallback differs per platform). The board's check tile glyph is therefore the stroke check, not Nunito's.
- CSS `border-bottom` on a rounded box draws a crescent; the edge here is a darker rounded rectangle under the face. Visually equal on 5-8dp edges, not pixel-identical.
- Soft shadow is a 1dp offset 6% black rounded rect, not a blurred shadow.
- 56dp top inset is the board's mock status bar; on devices the safe-area inset should replace `AulaSpacing.ScreenTop` (not done yet, no inset plumbing).
- Links are 44dp as on the boards (below the 56dp floor).
- Focus ring (3dp primary, 3dp offset, from the boards' CSS) is drawn on pressables and fields.

## Uncertainties (not visible in the HTML, decided here)
- Pressed state: the boards have none. Face drops (edge - 1dp), edge collapses; Secondary fades to 75%.
- Disabled: 50% opacity, no press. Not on a board.
- Quiz option Selected (ring) and Wrong (50% + cross) are guesses; Correct (full opacity + check, others 50%) is on the board.
- "Elegido" pill ink for "Me perdí": InkDeep (board only shows the green tile).
- Counter footer of the multi-line field, EmptyState, AulaToast: not on student boards.
- Nunito/Inter are loaded as variable fonts with weight variation; if a platform ignores variation settings the bold cuts will look regular.
