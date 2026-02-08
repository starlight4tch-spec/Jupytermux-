# Jupytermux - Development Guide

## Quick Start

### Prerequisites

- Android Studio 4.2 or higher
- JDK 11 or higher
- Android SDK 34
- Gradle 8.x

### Setup

1. **Clone and Open**
   ```bash
   git clone https://github.com/starlight4tch-spec/Jupytermux-.git
   cd Jupytermux-
   ```

2. **Open in Android Studio**
   - File → Open → Select project folder
   - Wait for Gradle sync to complete

3. **Build and Run**
   - Connect device via USB or use emulator
   - Click Run or press `Shift + F10`

## Key Components

### Activities

- **MainActivity**: Launch pad with button shortcuts
- **NotebookViewerActivity**: Displays Jupyter notebooks using WebView
- **TerminalActivity**: Terminal interface for shell commands

### Code Modules

- **ui/**: User interface components and activities
- **data/**: Data models, repositories, and database access
- **utils/**: Helper functions (NotebookParser, etc)

## Architecture Decisions

### Notebook Rendering
Uses WebView to render parsed JSON as HTML for compatibility with existing Jupyter formats.

### Terminal Access
Integrates with Termux or local shell for command execution. Can be extended to support remote connections.

### Data Storage
Uses Android's file system directly. Can be upgraded to Room database for better management.

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Build Variants

### Debug (Default)
```bash
./gradlew assembleDebug
```

### Release
```bash
./gradlew assembleRelease
```

## Dependencies Update

Check for updates:
```bash
./gradlew dependencyUpdates
```

## Troubleshooting

### Build Issues
- Clean: `./gradlew clean`
- Sync: `./gradlew --refresh-dependencies`
- Reset: Delete `.gradle` and `build` folders

### Runtime Issues
- Check logcat: `adb logcat | grep jupytermux`
- Verify permissions in AndroidManifest.xml
- Check storage access in app settings

## Code Style

- Follow [Google's Kotlin Style Guide](https://android.github.io/kotlin-guides/style-guide.html)
- Use ktlint: `./gradlew lint`
- Format code: `Ctrl+Alt+L` in Android Studio

## Next Steps

1. Implement notebook file picker
2. Add terminal command history
3. Support for remote Jupyter servers
4. Advanced notebook editing features
