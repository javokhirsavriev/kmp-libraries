package com.kmp.libraries.biometry

expect class BiometryAuthenticator {

    /**
     * Performs user authentication using biometrics-fingerprint/face scan-returns the result of the scan
     *
     * @param title - Text for title of request dialog
     * @param reason - Text describing the reason for confirmation via biometrics
     * @param failureButtonText - Text of the button to go to the backup verification method in
     * case of unsuccessful biometrics recognition
     * if biometric permission is not granted user can authorise by device passcode
     *
     * @throws Exception if authentication failed
     *
     * @return true for successful confirmation of biometrics, false for unsuccessful confirmation
     */

    suspend fun checkBiometryAuthentication(
        title: String,
        reason: String,
        failureButtonText: String,
    ): Boolean

    /**
     * Performs a biometric scan availability check
     *
     * @return true if it is possible to use a biometry, false - if it is not available
     */
    fun isBiometricAvailable(): Boolean
}