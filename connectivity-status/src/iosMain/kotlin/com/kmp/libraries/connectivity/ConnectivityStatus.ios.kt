package com.kmp.libraries.connectivity

import cocoapods.Reachability.Reachability
import kotlinx.coroutines.flow.MutableStateFlow
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class ConnectivityStatus {

    actual val networkConnected = MutableStateFlow(false)

    private var reachability: Reachability? = null

    actual fun start() {
        dispatch_async(dispatch_get_main_queue()) {
            reachability = Reachability.reachabilityForInternetConnection()

            val reachableCallback = { _: Reachability? ->
                dispatch_async(dispatch_get_main_queue()) {
                    networkConnected.value = true
                }
            }
            reachability?.reachableBlock = reachableCallback

            val unreachableCallback = { _: Reachability? ->
                dispatch_async(dispatch_get_main_queue()) {
                    networkConnected.value = false
                }
            }
            reachability?.unreachableBlock = unreachableCallback

            reachability?.startNotifier()

            dispatch_async(dispatch_get_main_queue()) {
                networkConnected.value = reachability?.isReachable() ?: false
            }
        }
    }

    actual fun stop() {
        reachability?.stopNotifier()
    }
}