# Brand

Source files (SVG, from the design team): `logo-icon.svg` (square mark "AT" with the four dots, full-bleed purple) and `logo-full.svg` (mark + "AulaTria" + tagline, on white).
PNG exports at 1024 px are for places that cannot use SVG (Android/iOS icons, Compose resources). Regenerate with ImageMagick:
`magick -background none -density 192 logo-icon.svg -resize 1024x1024 logo-icon-1024.png`.

Where the logo is used: web favicon and header (`apps/web/public/logo-icon.svg`, `LogoSlot`), student app logo (`apps/app/shared/src/commonMain/composeResources/drawable/logo_icon.png`), browser build favicon, Android launcher icons (`apps/app/androidApp/src/main/res/mipmap-*`), iOS AppIcon (`apps/app/iosApp/iosApp/Assets.xcassets/AppIcon.appiconset`). Brand color of the icon background: `#3A1D95`.
