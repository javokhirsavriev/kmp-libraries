package com.kmp.libraries.connectivity

import kotlinx.coroutines.flow.MutableStateFlow

expect class ConnectivityStatus {
    val networkConnected: MutableStateFlow<Boolean>

    fun start()
    fun stop()
}