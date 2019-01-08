import androidx.annotation.CheckResult
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Result
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@CheckResult
fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> {
    return compose {
        // item:Flowable<T>
        item ->
        // it:T
        // map:Flowable<T>をResult<T>に変換
        item.map { Result.success(it) }
                // エラー通知
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                // 結果はUIスレッドに通知
                .observeOn(schedulerProvider.ui())
                // inProgressの結果を通知した後に、Flowable<Result<T>>を通知する
                .startWith(Result.inProgress())
    }
}

@CheckResult
fun <T> Observable<T>.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    return compose {
        // item:Observable
        item ->
        // it:T
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

@CheckResult
fun <T> Single<T>.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    // Single<T>をObservable<T>に変換後、Observable<Result<T>>に変換
    return toObservable().toResult(schedulerProvider)
}

@CheckResult
fun <T> Completable.toResult(schedulerProvider: SchedulerProvider):
        Observable<Result<T>> {
    // Completable<T>をObservable<T>に変換後、Observable<Result<T>>に変換
    return toObservable<T>().toResult(schedulerProvider)
}