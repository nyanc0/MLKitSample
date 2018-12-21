
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Response
import kotlinx.coroutines.Deferred


typealias DeferredResponse<R> = Deferred<Response<R>>

// https://qiita.com/naito-y/items/c3d286585a7c9fb72372
// https://qiita.com/k-kagurazaka@github/items/702c92bc3381af36db12
// https://speakerdeck.com/sys1yagi/kotlin-korutinwo-li-jie-siyou?slide=177

//internal inline fun <R> generateResponse(vararg expected: KClass<out Throwable> = arrayOf(Throwable::class),
//                                         block: () -> R)
//        : Response<R> = try {
//
//}