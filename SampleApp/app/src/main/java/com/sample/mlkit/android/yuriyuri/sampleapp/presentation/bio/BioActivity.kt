package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class BioActivity : AppCompatActivity() {

    private val KEY = "test"
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BioActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isTargetSDK()) {

            val biometricPrompt = BiometricPrompt.Builder(this)
                    .setTitle("Title")
                    .setSubtitle("Subtitle")
                    .setDescription("")
                    .setNegativeButton("Cancel", mainExecutor, DialogInterface.OnClickListener { dialog, which ->

                    })
                    .build()

            val cancellationSignal = CancellationSignal()
            cancellationSignal.setOnCancelListener {

            }

            val keyPair = viewModel.generateKeyPair(KEY, true)
            val signature = viewModel.initSignature(KEY)

            biometricPrompt.authenticate(BiometricPrompt.CryptoObject(signature), cancellationSignal
                    , mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("BIOMETRIC", "onError")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("BIOMETRIC", "onSuccess")
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpCode, helpString)
                    Log.d("BIOMETRIC", "onHelp")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("BIOMETRIC", "onFailed")
                }
            })
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BioActivity::class.java)
            context.startActivity(intent)
        }
    }
}