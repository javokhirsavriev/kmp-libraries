enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-libraries"
include(":app-android")
include(":shared")
include(":base64")
include(":uuid")
include(":connectivity-status")
include(":device-info")
include(":biometry")
