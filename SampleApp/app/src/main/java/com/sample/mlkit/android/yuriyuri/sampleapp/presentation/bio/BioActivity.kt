package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio

import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

class BioActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//                .setTitle("title")
//                .setSubtitle("sub title")
//                .setDescription("description")
//                .setNegativeButtonText("cancel")
//                .build()
//
//        BiometricPrompt(this, mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
//            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                // 認証成功時のハンドル
//            }
//
//            override fun onAuthenticationFailed() {
//                // 認証失敗時のハンドル
//            }
//
//            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                // 認証エラー時のハンドル
//            }
//        }).authenticate(promptInfo)
    }
}