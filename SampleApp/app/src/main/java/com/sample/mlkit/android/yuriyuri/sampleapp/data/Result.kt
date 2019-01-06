package com.sample.mlkit.android.yuriyuri.sampleapp.data

sealed class Result<R>(val inProgress: Boolean) {

    class InProgress<T> : Result<T>(true) {
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
    data class Success<T>(var data: T) : Result<T>(false)

    /**
     * データ取得失敗
     */
    data class Failure<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)

    companion object {
        fun <T> inProgress(): Result<T> = InProgress()

        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(errorMessage: String, e: Throwable): Result<T> = Failure(errorMessage, e)
    }

}