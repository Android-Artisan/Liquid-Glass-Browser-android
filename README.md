# Artisan Browser

A full-featured Android web browser built entirely with Jetpack Compose, powered by **GeckoView** (Firefox's rendering engine).

## Features

- **Tabbed browsing** with live preview snapshots, private tabs, and a liquid-animated bottom tab bar
- **Liquid Glass UI** — real-time GPU-accelerated blur, vibrancy, and tinting via the Backdrop library (first Android browser to feature Liquid Glass)
- **Bookmarks & history** persisted in SharedPreferences
- **Download manager** with live progress tracking
- **Developer tools** — JavaScript console and "Edit Page" (makes page content editable)
- **Settings** with adjustable blur/tint intensity and a live chrome preview
- **Dynamic color** theming (Material You, Android 12+)

## Tech Stack

| Layer | Library |
|---|---|
| Engine | [GeckoView 150](https://maven.mozilla.org/maven2/org/mozilla/geckoview/) |
| UI | Jetpack Compose + Material 3 |
| Liquid Glass | [Backdrop](https://github.com/kyant0/backdrop) (vibrancy, blur, lens) |
| Shapes | [Kyant Shapes](https://github.com/kyant0/shapes) |
| Architecture | Single-activity, Compose-first |

## Build

### Commands

```bash
# Install on device
./gradlew installArm64Debug          # modern phones (arm64-v8a)
./gradlew installArm7Debug            # older phones (armeabi-v7a)
./gradlew installX86Debug             # emulators / Chromebooks (x86_64)
./gradlew installUniversalDebug       # all architectures

# Build all APKs
./gradlew assembleDebug
```

### APK Sizes (compressed)

| Variant | Size |
|---|---|
| `arm64` | 152 MB |
| `arm7` | 148 MB |
| `x86` | 159 MB |
| `universal` | 305 MB |

## CI

On every push to `main`, GitHub Actions builds all 4 variants and uploads them as artifacts.

## License

GPLv3
