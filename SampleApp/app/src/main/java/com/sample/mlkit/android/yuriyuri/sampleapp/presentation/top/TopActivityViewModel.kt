package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Result
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.MenuRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import toResult

class TopActivityViewModel constructor(private val schedulerProvider: SchedulerProvider, private val repository: MenuRepository)
    : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mutableRefreshState: MutableLiveData<Result<List<ContentSet>>> = MutableLiveData()
    val refreshResult: LiveData<Result<List<ContentSet>>> = mutableRefreshState

    fun loadContentSet() {
        repository.fetchMenu()
                .subscribeOn(schedulerProvider.io())
                .toResult(schedulerProvider)
                .subscribeBy(
                        onNext = {
                            mutableRefreshState.value = it
                        },
                        onError = {
                            // do something
                        }
                ).addTo(compositeDisposable)
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }
}