package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.binding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
            .load(imageUrl)
            .into(view)
}

@BindingAdapter("vectorRes")
fun loadDrawableRes(view: ImageView, resId: Int) {
    view.setImageResource(resId)
}