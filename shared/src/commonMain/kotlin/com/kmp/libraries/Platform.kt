package com.kmp.libraries

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform