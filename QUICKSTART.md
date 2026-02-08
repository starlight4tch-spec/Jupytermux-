# Jupytermux - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites
- Android Studio installed
- Android SDK 34 (configured in Android Studio)
- JDK 11 or higher
- A device/emulator running Android 6.0+

### Step 1: Clone & Open
```bash
git clone https://github.com/starlight4tch-spec/Jupytermux-.git
cd Jupytermux-
```

Then open in Android Studio:
- **File** → **Open** → Select `Jupytermux-` folder

### Step 2: Sync & Build
Android Studio will automatically:
1. Download Gradle wrapper
2. Resolve dependencies
3. Build the project

If you get errors, run:
```bash
./gradlew clean build
```

### Step 3: Run the App
1. Connect an Android device via USB **OR** start an Android emulator
2. Click the green **Run** button (▶️) in toolbar OR press `Shift + F10`
3. Select your device/emulator
4. App will install and launch

## 📱 App Features

### Navigation
- **Main Screen**: Landing page with two buttons
- **Notebook Viewer**: Browse and display Jupyter notebooks
- **Terminal**: Execute shell commands (planned)

### Current Capabilities
✅ View `.ipynb` files  
✅ Parse notebook cells  
✅ Display markdown and code cells  
✅ Basic HTML rendering

⏳ To Implement
📝 Notebook editing  
🔧 Python execution via Termux  
💾 Auto-save functionality  
☁️ Cloud sync

## 🔧 Development Tips

### Modify UI
- Edit layouts: `app/src/main/res/layout/*.xml`
- Modify strings: `app/src/main/res/values/strings.xml`
- Update colors: `app/src/main/res/values/colors.xml`

### Add Features
1. Create new Activity in `app/src/main/java/com/jupytermux/ui/`
2. Add layout XML in `res/layout/`
3. Register in `AndroidManifest.xml`
4. Update `MainActivity.kt` to navigate to it

### Debug
- Open Logcat: **View** → **Tool Windows** → **Logcat**
- Filter by package: Type `jupytermux` in search box
- Connect physical device: Enable USB Debugging in settings

## 📦 Build APK

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
Located at: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for distribution)
```bash
./gradlew assembleRelease
```

## 🧪 Test Your Changes

After modifying code:
1. Click **Run** button or press `Shift + F10`
2. App will rebuild and reinstall automatically
3. Check **Logcat** for errors

## 📚 Project Structure

```
app/src/main/
├── java/com/jupytermux/
│   ├── ui/             ← Activities (screens)
│   ├── data/           ← Models & repositories
│   └── utils/          ← Helper functions
├── res/
│   ├── layout/         ← XML UI layouts
│   ├── values/         ← Colors, strings, themes
│   └── menu/           ← Menu definitions
└── AndroidManifest.xml ← App configuration
```

## 🐛 Common Issues

### Gradle sync fails
```bash
./gradlew clean build --refresh-dependencies
```

### App crashes on launch
- Check Logcat for error messages
- Ensure all permissions are in `AndroidManifest.xml`
- Verify SDK level compatibility

### Emulator slow
- Increase RAM allocation in AVD settings
- Use hardware acceleration if supported

## 📖 Learn More

- [Android Docs](https://developer.android.com/docs)
- [Kotlin Guide](https://kotlinlang.org/docs/home.html)
- [Jupyter Format](https://nbformat.readthedocs.io/)
- [Material Design](https://material.io/design)

## 🎯 Next Steps

1. **Understand the code**: Read through `MainActivity.kt`
2. **Add a feature**: Try adding a "Settings" screen
3. **Explore APIs**: Check out `NotebookParser.kt`
4. **Contribute**: Make a PR with improvements!

---

**Happy coding! 🎉**
