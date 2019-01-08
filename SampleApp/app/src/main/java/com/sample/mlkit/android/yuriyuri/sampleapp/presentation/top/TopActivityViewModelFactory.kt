package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.MenuRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.util.AppSchedulerProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider

@Suppress("UNCHECKED_CAST")
class TopActivityViewModelFactory private constructor(private val schedulerProvider: SchedulerProvider, private val repository: MenuRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TopActivityViewModel::class.java)) {
            TopActivityViewModelFactory(AppSchedulerProvider(), MenuRepository()) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TopActivityViewModelFactory? = null

        fun getInstance(schedulerProvider: SchedulerProvider, repository: MenuRepository): TopActivityViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: TopActivityViewModelFactory(schedulerProvider, repository).also {
                        INSTANCE = it
                    }
                }
    }
}