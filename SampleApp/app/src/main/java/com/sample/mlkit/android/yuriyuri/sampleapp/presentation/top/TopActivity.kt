package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Result
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityTopBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.NavigationController
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.ViewModelFactory
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common.view.SpaceItemDecoration

class TopActivity : AppCompatActivity() {

    private lateinit var adapter: ContentAdapter
    private val navigationController = NavigationController()

    private val binding: ActivityTopBinding by lazy {
        DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
    }

    private val viewModel: TopActivityViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(TopActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.addItemDecoration(SpaceItemDecoration(8))
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@TopActivity, 2)

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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun renderView(list: List<ContentSet>) {
        if (binding.recyclerView.adapter == null) {
            adapter = ContentAdapter(ArrayList(list), object : ContentAdapter.ContentItemClickListener {
                override fun itemClicked(contentSet: ContentSet) {
                    when (contentSet) {
                        ContentSet.BiometricPromptSet -> {
                            navigationController.navigateToBioActivity(this@TopActivity)
                        }
                    }
                }
            })
            binding.recyclerView.adapter = adapter
        } else {
            adapter.reset(list)
        }
    }
}