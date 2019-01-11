package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("stringResId")
fun setTextResId(view: TextView, resId: Int) {
    view.setText(resId)
}