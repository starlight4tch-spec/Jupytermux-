plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.jupytermux"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jupytermux"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.0")

    // Material Design
    implementation("com.google.android.material:material:1.10.0")

    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    // JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")

    // Terminal emulator
    implementation("com.termux:termux-app:0.118.0")
    implementation("com.termux:termux-shared:0.118.0")

    // WebView for Jupyter rendering
    implementation("androidx.webkit:webkit:1.6.1")

    // File handling
    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Syntax Highlighting
    implementation("com.android.code:syntax-highlight:1.0.0")
    
    // Code Editor
    implementation("io.github.rosemoe.sora-editor:editor:0.23.4")

    // Markdown
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:html:4.6.2")
    implementation("io.noties.markwon:image:4.6.2")

    // SSH/SFTP
    // implementation("com.jcraft:jsch:0.1.55") // Add if using SSH

    // GIT
    // implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r") // Add for Git support

    // QR Code
    implementation("com.google.zxing:core:3.5.2")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")

    // PDF Export
    implementation("com.itextpdf:itextg:5.5.10")
    implementation("com.itextpdf:barcodes:5.5.10")

    // Charts/Plots
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
