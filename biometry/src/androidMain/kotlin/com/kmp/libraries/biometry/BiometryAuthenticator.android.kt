package com.kmp.libraries.biometry

import android.annotation.SuppressLint
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.Executor
import kotlin.coroutines.suspendCoroutine

actual class BiometryAuthenticator(
    private val applicationContext: Context,
) {
    private var fragmentManager: FragmentManager? = null

    fun bind(
        lifecycle: Lifecycle,
        fragmentManager: FragmentManager,
    ) {
        this.fragmentManager = fragmentManager

        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed(source: LifecycleOwner) {
                this@BiometryAuthenticator.fragmentManager = null
                source.lifecycle.removeObserver(this)
            }
        }
        lifecycle.addObserver(observer)
    }

    actual suspend fun checkBiometryAuthentication(
        title: String,
        reason: String,
        failureButtonText: String,
    ): Boolean {
        val resolverFragment: ResolverFragment = getResolverFragment()

        return suspendCoroutine { continuation ->
            var resumed = false

            resolverFragment.showBiometricPrompt(
                title = title,
                reason = reason,
                failureButtonText = failureButtonText
            ) {
                if (!resumed) {
                    continuation.resumeWith(it)
                    resumed = true
                }
            }
        }
    }

    actual fun isBiometricAvailable(): Boolean {
        val manager: BiometricManager = BiometricManager.from(applicationContext)
        return manager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun getResolverFragment(): ResolverFragment {
        val fragmentManager: FragmentManager = fragmentManager
            ?: error("can't check biometry without active window")

        val currentFragment: Fragment? = fragmentManager
            .findFragmentByTag(BIOMETRY_RESOLVER_FRAGMENT_TAG)

        return if (currentFragment != null) {
            currentFragment as ResolverFragment
        } else {
            ResolverFragment().apply {
                fragmentManager
                    .beginTransaction()
                    .add(this, BIOMETRY_RESOLVER_FRAGMENT_TAG)
                    .commitNow()
            }
        }
    }

    class ResolverFragment : Fragment() {
        private lateinit var executor: Executor
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: BiometricPrompt.PromptInfo

        init {
            retainInstance = true
        }

        /**
         * Prepare and show BiometricPrompt system dialog
         *
         * @param title biometric prompt title
         * @param reason biometric prompt reason
         * @param failureButtonText
         */
        fun showBiometricPrompt(
            title: String,
            reason: String,
            failureButtonText: String,
            callback: (Result<Boolean>) -> Unit,
        ) {
            val context = requireContext()

            executor = ContextCompat.getMainExecutor(context)

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    @SuppressLint("RestrictedApi")
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

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(reason)
                .setNegativeButtonText(failureButtonText)
                .setConfirmationRequired(false)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    companion object {
        private const val BIOMETRY_RESOLVER_FRAGMENT_TAG = "BiometryControllerResolver"
    }
}