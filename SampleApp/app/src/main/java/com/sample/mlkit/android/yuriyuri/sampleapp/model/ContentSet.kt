package com.sample.mlkit.android.yuriyuri.sampleapp.model

import com.sample.mlkit.android.yuriyuri.sampleapp.R

enum class ContentSet(val title: String, val resId: Int) {
    BiometricPromptSet("生体認証", R.drawable.ic_mood_white_24dp),
    MLLandMarkSet("ランドマーク認証", R.drawable.ic_landmark_white_24dp)
}