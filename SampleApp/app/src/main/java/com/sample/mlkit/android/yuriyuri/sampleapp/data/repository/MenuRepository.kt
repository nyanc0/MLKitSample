package com.sample.mlkit.android.yuriyuri.sampleapp.data.repository

import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class MenuRepository @Inject constructor(
        private val appSchedulerProvider: SchedulerProvider
) {
    /**
     * メニューのリストを返す.
     */
    fun fetchMenu(): Single<List<ContentSet>> {
        return Single.create {
            val list: MutableList<ContentSet> = mutableListOf()
            for (contentSet in ContentSet.values()) {
                list.add(contentSet)
            }
            if (list.isEmpty()) {
                it.onError(NullPointerException("ContentSetList is null!!"))
            } else {
                it.onSuccess(list)
            }
        }
    }
}