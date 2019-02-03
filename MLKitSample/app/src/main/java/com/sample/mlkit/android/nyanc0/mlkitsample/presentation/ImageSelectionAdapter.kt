package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.view.ViewGroup
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ItemImageSelectionBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.ArrayRecyclerAdapter
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.BindingHolder

class ImageSelectionAdapter(list: ArrayList<ImageSelection>, private val listener: OnItemClickListener) :
    ArrayRecyclerAdapter<ImageSelection, BindingHolder<ItemImageSelectionBinding>>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemImageSelectionBinding> {
        return BindingHolder(parent.context, parent, R.layout.item_image_selection)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemImageSelectionBinding>, position: Int) {
        val data = list[position]
        holder.binding!!.selecion = data
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(list[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: ImageSelection)
    }
}