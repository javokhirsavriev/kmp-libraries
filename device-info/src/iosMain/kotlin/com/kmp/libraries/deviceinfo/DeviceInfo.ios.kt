package com.kmp.libraries.deviceinfo

actual val platform = Platform.IOS

actual fun deviceInfo(): String {
    return "${platform.name}, ${deviceManufacturer()} ${deviceModel()}, ${deviceVersion()}"
}

actual fun deviceModel(): String {
    return ""
}

actual fun deviceManufacturer(): String {
    return ""
}

actual fun deviceVersion(): String {
    return ""
}