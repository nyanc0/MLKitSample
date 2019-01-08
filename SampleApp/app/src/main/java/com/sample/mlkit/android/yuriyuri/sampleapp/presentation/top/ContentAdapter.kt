package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top

import android.view.ViewGroup
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ItemTopBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.ArrayRecyclerAdapter
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.BindingHolder

class ContentAdapter(list: ArrayList<ContentSet>, listener: ContentItemClickListener)
    : ArrayRecyclerAdapter<ContentSet, BindingHolder<ItemTopBinding>>(list) {

    val itemListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemTopBinding> {
        return BindingHolder(parent.context, parent, R.layout.item_top)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemTopBinding>, position: Int) {
        val data = list[position]
        holder.binding!!.content = data
        holder.binding.card.setOnClickListener {
            itemListener.itemClicked(list[position])
        }
    }

    interface ContentItemClickListener {
        fun itemClicked(contentSet: ContentSet)
    }
}