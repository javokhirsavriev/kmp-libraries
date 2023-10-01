package com.kmp.libraries.deviceinfo

import android.os.Build

actual val platform = Platform.Android

actual fun deviceInfo(): String {
    return "${platform.name}, ${deviceManufacturer()} ${deviceModel()}, ${deviceVersion()}"
}

actual fun deviceModel(): String = Build.MODEL ?: "Unknown"

actual fun deviceManufacturer(): String = Build.MANUFACTURER ?: "Unknown"

actual fun deviceVersion(): String = Build.VERSION.RELEASE ?: "Unknown"