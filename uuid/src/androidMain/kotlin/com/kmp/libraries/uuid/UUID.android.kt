package com.kmp.libraries.uuid

import java.util.UUID

actual fun randomUUID() = UUID.randomUUID().toString()