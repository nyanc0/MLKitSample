package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class SpaceItemDecoration(private val space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView,
                                state: androidx.recyclerview.widget.RecyclerView.State) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space
            outRect.right = space
            outRect.left = space
        } else {
            outRect.top = space
            outRect.bottom = space
            outRect.right = space
            outRect.left = space
        }
    }
}