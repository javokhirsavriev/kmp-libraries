package com.kmp.libraries.deviceinfo

expect val platform: Platform

expect fun deviceInfo(): String

expect fun deviceModel(): String

expect fun deviceManufacturer(): String

expect fun deviceVersion(): String