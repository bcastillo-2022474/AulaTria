---
name: change-design-system
description: Add or change a design-system component or token in AulaTria (web React/Tailwind and mobile Compose), keeping both faithful to docs/design and in sync. Use before building a screen that needs a visual pattern that does not exist yet.
---

# Change the design system

Two implementations, one table of tokens. Docs: `docs/design-system/web.md`, `docs/design-system/mobile.md` (read the rules and the known deviations there first). Source of truth for looks: `docs/design/*.dc.html` (inline styles are the truth; compare rendered pages).

| | Web (teacher/admin) | Mobile (student) |
|---|---|---|
| tokens | `apps/web/src/design-system/tokens.css` (`@theme`) | `apps/app/shared/.../designsystem/AulaTokens.kt` (+ `AulaType` in `AulaTheme.kt`) |
| components | one file each in `apps/web/src/design-system/`, exported from `index.ts` | `apps/app/shared/.../designsystem/` |
| gallery | `/#/design-system` (`apps/web/src/design-system/Gallery.tsx`) | `DesignSystemGallery.kt` (`?gallery` in the browser build) |

## Rules
1. **Gallery first.** A component exists when it is in the gallery in all its states. Screens consume components; they never style.
2. **Tokens only.** No raw hex/rgba/px colours or font sizes outside the token files. A new value that repeats in the design becomes a token with a **semantic name shared by both platforms** (`primary`, `primary-edge`, `stage`, `ink-soft`...); add it to BOTH token files and to both docs' token tables, or say in the doc why it is platform-specific.
3. **Fidelity is the bar.** Derive values from the originals (sizes, radii, the 5–8px "pressable edge", paddings). Where the design is inconsistent pick the dominant value and write the deviation in the doc. States the design does not show (pressed, disabled, error) are derived from the nearest pattern and **listed as guesses** in the doc.
4. **Stateless components** (state hoisted), accessible (web: real `<button>`, labels, `aria-*` on meters/timers; mobile: `Role.Button`, content descriptions on icon-only things), student touch targets ≥ 56dp.
5. **One concept, one component.** Before adding, search both folders; extend with a parameter if it is the same pattern.
6. English identifiers; Spanish only in gallery sample text.
7. No new dependency for a component unless it clearly pays (list it with the reason in the doc).

## Verify
Web: `mage web:check` + screenshot the gallery against the original at 1280x800. Mobile: `mage app:check` + `mage app:web?gallery`. Compare real renders, not code. Say what you could not compare. Update the doc's inventory and deviations in the same change.
