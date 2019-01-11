package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityBioBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.model.BioModel

class BioActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityBioBinding>(this, R.layout.activity_bio)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BioActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = BioModel.UNAUTHENTICATED
        binding.container.setOnClickListener {
            startBiometric()
        }

        startBiometric()
        binding.container.setOnClickListener {
            startBiometric()
        }
    }

    /**
     * 生体認証開始
     */
    private fun startBiometric() {
        if (viewModel.isTargetSDK() && viewModel.isSupportBiometricPrompt(this)) {
            val biometricPrompt = BiometricPrompt.Builder(this)
                    .setTitle("Title")
                    .setSubtitle("Subtitle")
                    .setDescription("")
                    .setNegativeButton("Cancel", mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                        showError("Cancel is selected")
                    })
                    .build()

            val cancellationSignal = CancellationSignal()
            cancellationSignal.setOnCancelListener {
                showError("Cancel is selected")
            }

            viewModel.generateKeyPair(KEY_NAME, true)
            val signature = viewModel.initSignature(KEY_NAME)

            biometricPrompt.authenticate(BiometricPrompt.CryptoObject(signature), cancellationSignal
                    , mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    showError(errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    showSuccess("")
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpCode, helpString)
                    Log.d("BIOMETRIC", "onHelp")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showError("onAuthenticationFailed has occurred")
                }
            })
        } else {
            showIncompatible("")
        }
    }

    /**
     * 認証利用不可表示
     */
    private fun showIncompatible(message: CharSequence?) {
        binding.model = BioModel.INCOMPATIBLE
        binding.message.text = message
    }

    /**
     * 認証エラー
     */
    private fun showError(message: CharSequence?) {
        binding.model = BioModel.FAILED
        binding.message.text = message
    }

    /**
     * 認証成功
     */
    private fun showSuccess(message: CharSequence?) {
        binding.model = BioModel.SUCCESS
        binding.message.text = message
    }

    companion object {

        private const val KEY_NAME = "test"

        fun start(context: Context) {
            val intent = Intent(context, BioActivity::class.java)
            context.startActivity(intent)
        }
    }
}