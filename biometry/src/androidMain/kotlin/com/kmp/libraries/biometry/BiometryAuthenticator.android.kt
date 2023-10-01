package com.kmp.libraries.biometry

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.suspendCoroutine

actual class BiometryAuthenticator(
    private val context: Context,
) {

    actual suspend fun biometryAuthentication(
        title: String,
        subtitle: String,
        failureButtonText: String,
    ): Boolean {
        return suspendCoroutine { continuation ->
            var resumed = false

            showBiometricPrompt(
                title = title,
                subtitle = subtitle,
                failureButtonText = failureButtonText,
            ) {
                if (!resumed) {
                    continuation.resumeWith(it)
                    resumed = true
                }
            }
        }
    }

    private fun showBiometricPrompt(
        title: String,
        subtitle: String,
        failureButtonText: String,
        callback: (Result<Boolean>) -> Unit,
    ) {
        val fragmentActivity = context as FragmentActivity
        val executor = ContextCompat.getMainExecutor(fragmentActivity)

        val biometricPrompt = BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED
                    ) {
                        callback.invoke(Result.success(false))
                    } else {
                        callback.invoke(Result.failure(Exception(errString.toString())))
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    callback.invoke(Result.success(true))
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(failureButtonText)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    actual fun hasBiometricCapabilities(): Boolean {
        val manager: BiometricManager = BiometricManager.from(context)
        return manager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }
}