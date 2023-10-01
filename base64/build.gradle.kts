plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Base64 encoder/decoder for Kotlin/Multiplatform. Supports Android and iOS."
        homepage = "https://javokhirsavriev.github.io/"
        version = "1.0"
        ios.deploymentTarget = "14.1"

        framework {
            baseName = "base64"
        }
    }
}

android {
    namespace = "com.kmp.libraries.base64"
    compileSdk = 34

    defaultConfig {
        minSdk = 25
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}