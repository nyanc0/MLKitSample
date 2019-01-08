package com.sample.mlkit.android.yuriyuri.sampleapp.data.repository

import com.sample.mlkit.android.yuriyuri.sampleapp.model.ContentSet
import io.reactivex.Single

class MenuRepository {
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