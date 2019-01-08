package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.ViewModel
import java.security.*
import java.security.spec.ECGenParameterSpec


class BioActivityViewModel : ViewModel() {


    fun isTargetSDK(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    private fun isSupportBiometricPrompt(context: Context): Boolean {
        val packageManager = context.packageManager
        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    @Throws(Exception::class)
    @TargetApi(Build.VERSION_CODES.P)
    fun generateKeyPair(keyName: String, invalidatedByBiometricEnrollment: Boolean): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(keyName,
                KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                // Require the user to authenticate with a biometric to authorize every use of the key
                .setUserAuthenticationRequired(true)
                // Generated keys will be invalidated if the biometric templates are added more to user device
                .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)

        keyPairGenerator.initialize(builder.build())
        return keyPairGenerator.generateKeyPair()
    }

    @Throws(Exception::class)
    private fun getKeyPair(keyName: String): KeyPair? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (keyStore.containsAlias(keyName)) {
            // Get public key
            val publicKey = keyStore.getCertificate(keyName).publicKey
            // Get private key
            val privateKey = keyStore.getKey(keyName, null) as PrivateKey
            // Return a key pair
            return KeyPair(publicKey, privateKey)
        }
        return null
    }

    @Throws(Exception::class)
    fun initSignature(keyName: String): Signature? {
        val keyPair = getKeyPair(keyName)

        if (keyPair != null) {
            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign(keyPair.private)
            return signature
        }
        return null
    }
}