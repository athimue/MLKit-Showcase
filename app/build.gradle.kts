plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.mlkit_showcase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mlkit_showcase"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Text recognition
    implementation("com.google.mlkit:text-recognition:16.0.0")
    // Object detection
    implementation("com.google.mlkit:object-detection:17.0.0")
    // Image labeling
    implementation("com.google.mlkit:image-labeling:17.0.7")
    // Barcode scanning
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    // Face detection
    implementation("com.google.mlkit:face-detection:16.1.5")
    // Face mesh detection
    implementation("com.google.mlkit:face-mesh-detection:16.0.0-beta1")
    // Digital Ink Recognition
    implementation("com.google.mlkit:digital-ink-recognition:18.1.0")
    // Pose detection
    implementation("com.google.mlkit:pose-detection:18.0.0-beta3")
    // Selfie segmentation
    implementation("com.google.mlkit:segmentation-selfie:16.0.0-beta4")
    // Subject segmentation
    implementation("com.google.android.gms:play-services-mlkit-subject-segmentation:16.0.0-beta1")
    // Language identification
    implementation("com.google.mlkit:language-id:17.0.4")

    // CameraX core library using the camera2 implementation
    val camerax_version = "1.4.0-alpha02"
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")

    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // For tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // For debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}