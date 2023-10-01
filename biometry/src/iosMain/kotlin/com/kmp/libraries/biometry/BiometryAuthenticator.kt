package com.kmp.libraries.biometry

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

actual class BiometryAuthenticator {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual suspend fun checkBiometryAuthentication(
        title: String,
        reason: String,
        failureButtonText: String,
    ): Boolean {
        val laContext = LAContext()
        laContext.setLocalizedFallbackTitle(failureButtonText)

        val (canEvaluate: Boolean?, error: NSError?) = memScoped {
            val p = alloc<ObjCObjectVar<NSError?>>()
            val canEvaluate: Boolean? = runCatching {
                laContext.canEvaluatePolicy(
                    policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    error = p.ptr
                )
            }.getOrNull()
            canEvaluate to p.value
        }

        if (error != null) throw error.toException()
        if (canEvaluate == null) return false

        return callbackToCoroutine { callback ->
            laContext.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = reason,
                reply = mainContinuation { result: Boolean, error: NSError? ->
                    callback(result, error)
                }
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun isBiometricAvailable(): Boolean {
        val laContext = LAContext()
        return laContext.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = null
        )
    }
}