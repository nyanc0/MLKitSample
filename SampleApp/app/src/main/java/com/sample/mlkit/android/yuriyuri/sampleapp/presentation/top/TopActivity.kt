package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.ViewGroup
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Result
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityTopBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ItemTopBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.ArrayRecyclerAdapter
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.BindingHolder
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.SpaceItemDecoration
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class TopActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: ContentAdapter

    private val binding: ActivityTopBinding by lazy {
        DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
    }

    private val viewModel: TopActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TopActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.addItemDecoration(SpaceItemDecoration(8))
        binding.recyclerView.layoutManager = GridLayoutManager(this@TopActivity, 2)

        viewModel.refreshResult.observe(
                this, Observer { it ->
            when (it) {
                is Result.Success -> {
                    renderView(it.data)
                }
                is Result.Failure -> {
                    Log.d("TEST", "TopActivity is called!!")
                }
            }
        }
        )

        viewModel.loadContentSet()
    }

    private fun renderView(list: List<ContentSet>) {
        if (binding.recyclerView.adapter == null) {
            adapter = ContentAdapter(ArrayList(list))
            binding.recyclerView.adapter = adapter
        } else {
            adapter.reset(list)
        }
    }

    inner class ContentAdapter(list: ArrayList<ContentSet>)
        : ArrayRecyclerAdapter<ContentSet, BindingHolder<ItemTopBinding>>(list) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemTopBinding> {
            return BindingHolder(parent.context, parent, R.layout.item_top)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemTopBinding>, position: Int) {
            val data = list[position]
            holder.binding!!.content = data
            holder.binding.card.setOnClickListener {

            }
        }
    }
}