# iosApp

Thin Xcode shell around the shared Compose code (`../shared`, framework `Shared`). Adapted from the Chassis project.

- Open `iosApp.xcodeproj` on a Mac with Xcode. The first build runs Gradle (`:shared:embedAndSignAppleFrameworkForXcode`) from a build phase.
- `Configuration/Config.xcconfig` holds the bundle id, the display name and `API_BASE_URL`. For a real phone create `Configuration/Local.xcconfig` (gitignored) with `API_BASE_URL=http:/$()/<your-mac-lan-ip>:3000` and `TEAM_ID`.
- **App icon: missing.** Put the 1024x1024 icon (docs/brand/logo-icon.png) into `iosApp/Assets.xcassets/AppIcon.appiconset/` and add its filename to `Contents.json`.
- **Never compiled on this repo's Linux dev host** (Kotlin/Native iOS targets need macOS): CI job `app-ios` is the first real build. Treat the first Mac run as a spike (streaming over the Darwin engine is unverified: see `docs/MVP.md`).
