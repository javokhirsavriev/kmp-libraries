package com.kmp.libraries.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

actual class ConnectivityStatus(context: Context) {

    actual val networkConnected = MutableStateFlow(false)

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkConnected.value = true
        }

        override fun onLost(network: Network) {
            networkConnected.value = false
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            val connected =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            networkConnected.value = connected
        }
    }

    actual fun start() {
        try {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
            val currentNetwork = connectivityManager.activeNetwork

            if (currentNetwork == null) {
                networkConnected.value = false
            }

        } catch (e: Exception) {
            networkConnected.value = false
        }
    }

    actual fun stop() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}