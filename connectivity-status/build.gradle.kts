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
        summary =
            "KMP library to monitor the connectivity status of the device. Supports Android and iOS."
        homepage = "https://javokhirsavriev.github.io/"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../app-ios/Podfile")

        framework {
            baseName = "connectivity-status"
        }

        // https://github.com/tonymillion/Reachability
        pod("Reachability") {
            version = "3.2"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android {
    namespace = "com.kmp.libraries.connectivity"
    compileSdk = 34

    defaultConfig {
        minSdk = 25
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}