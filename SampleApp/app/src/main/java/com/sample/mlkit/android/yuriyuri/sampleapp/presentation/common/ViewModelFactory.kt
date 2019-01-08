package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.common

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.MenuRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top.TopActivityViewModel
import com.sample.mlkit.android.yuriyuri.sampleapp.util.AppSchedulerProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider


@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val schedulerProvider: SchedulerProvider, private val repository: MenuRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopActivityViewModel::class.java)) {
            return TopActivityViewModel(schedulerProvider, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(AppSchedulerProvider(), MenuRepository())
                    }
                }
            }
            return INSTANCE
        }
    }
}