package com.sample.mlkit.android.yuriyuri.sampleapp.model

import com.sample.mlkit.android.yuriyuri.sampleapp.R

enum class BioModel(val messageResId: Int, val imageResId: Int) {
    INCOMPATIBLE(R.string.message_incompatible, R.drawable.ic_incompatible__36dp),
    FAILED(R.string.message_failed, R.drawable.ic_failed_36dp),
    SUCCESS(R.string.message_success, R.drawable.ic_success_36dp),
    UNAUTHENTICATED(R.string.message_unauthenticated, R.drawable.ic_unauthenticated_24dp)
}