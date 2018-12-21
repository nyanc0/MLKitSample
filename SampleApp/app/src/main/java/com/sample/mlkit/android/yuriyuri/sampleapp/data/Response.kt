package com.sample.mlkit.android.yuriyuri.sampleapp.data

sealed class Response<R>(val inProgress: Boolean) {

    class InProgress<T> : Response<T>(true) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int = javaClass.hashCode()
    }

    /**
     * データ取得成功
     */
    data class Success<T>(var data: T) : Response<T>(false)

    /**
     * データ取得失敗
     */
    data class Failure<T>(val errorMessage: String?, val e: Throwable) : Response<T>(false)

    companion object {
        fun <T> inProgress(): Response<T> = InProgress()

        fun <T> success(data: T): Response<T> = Success(data)

        fun <T> failure(errorMessage: String, e: Throwable): Response<T> = Failure(errorMessage, e)
    }

}