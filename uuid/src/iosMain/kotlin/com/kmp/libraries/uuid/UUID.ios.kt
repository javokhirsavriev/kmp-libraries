package com.kmp.libraries.uuid

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFUUIDCreate
import platform.CoreFoundation.CFUUIDCreateString
import platform.Foundation.CFBridgingRelease

@OptIn(ExperimentalForeignApi::class)
actual fun randomUUID(): String =
    CFBridgingRelease(CFUUIDCreateString(null, CFUUIDCreate(null))) as String