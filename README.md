# Jupytermux - Android Jupyter Notebook & Termux Terminal

A free Android application that combines Jupyter Notebook viewing/editing with integrated terminal access via Termux. Run Python code, manage Jupyter notebooks, and access system commands all in one app.

## Features

✨ **Core Features:**
- 📓 View and edit Jupyter notebooks (.ipynb files)
- 💻 Integrated Termux terminal access
- 🐍 Execute Python code directly
- 📁 File browser and notebook management
- 🚀 Fast, lightweight, and free

## Requirements

- Android 6.0 (API 24) or higher
- 50MB free storage
- Internet connection (for remote resources)

## Installation

### From Source

1. Clone the repository:
```bash
git clone https://github.com/starlight4tch-spec/Jupytermux-.git
cd Jupytermux-
```

2. Open in Android Studio:
   - File → Open → Select the project folder
   - Wait for Gradle to sync

3. Build and Run:
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

### Build APK

```bash
./gradlew build
```

APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

## Project Structure

```
Jupytermux/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/jupytermux/
│   │   │   │   ├── ui/              # Activities and UI components
│   │   │   │   ├── data/            # Data models and repository
│   │   │   │   └── utils/           # Utilities (notebook parser, etc)
│   │   │   ├── res/
│   │   │   │   ├── layout/          # XML layout files
│   │   │   │   ├── values/          # Colors, strings, themes
│   │   │   │   └── menu/            # Menu definitions
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Unit tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Usage

### Viewing Notebooks

1. Open the app and tap "Notebook Viewer"
2. Browse your device storage for `.ipynb` files
3. Select a notebook to view
4. Scroll through cells and outputs

### Using Terminal

1. Tap "Terminal" on the main screen
2. Execute shell commands and Python code
3. Switch between terminal and notebooks seamlessly

## Development

### Dependencies

- **AndroidX**: Core Android framework components
- **Material Design**: Modern UI components
- **Retrofit 2**: HTTP client for remote notebooks
- **Gson**: JSON serialization
- **Termux**: Terminal emulation

### Building

```bash
# Install dependencies
./gradlew build

# Run tests
./gradlew test

# Create signed APK
./gradlew bundleRelease
```

## Architecture

The app follows the MVVM (Model-View-ViewModel) pattern:

- **UI Layer**: Activities and Fragments
- **Data Layer**: Repositories and local storage
- **Utility Layer**: Notebook parsing and terminal management

## Permissions

The app requires the following permissions:

```xml
- android:permission.INTERNET
- android:permission.READ_EXTERNAL_STORAGE
- android:permission.WRITE_EXTERNAL_STORAGE
- android:permission.ACCESS_FINE_LOCATION
```

## Roadmap

- [ ] Cloud sync (Google Drive, Dropbox)
- [ ] Advanced notebook editing with markdown preview
- [ ] Python package management  
- [ ] Theme customization
- [ ] Dark mode enhancement
- [ ] Split-screen notebook + terminal view
- [ ] Code syntax highlighting
- [ ] Jupyter kernel integration
- [ ] Export notebooks to PDF/HTML

## Known Issues

- Terminal output rendering in complex scenarios
- Large notebook files may load slowly
- Some advanced Jupyter features not yet supported

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or suggestions:
- Create an issue on GitHub
- Check existing issues for solutions

## Acknowledgments

- Jupyter Project for the notebook format
- Termux community for terminal tools
- Material Design team for UI/UX guidelines

## Disclaimer

This is an unofficial Jupyter client. Jupyter is maintained by Project Jupyter. Termux is developed by the Termux community.

---

**Made with ❤️ by the Jupytermux Team**
