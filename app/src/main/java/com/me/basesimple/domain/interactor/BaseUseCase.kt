package com.me.basesimple.domain.interactor


import com.me.basesimple.global.extention.Failure
import kotlinx.coroutines.*
import timber.log.Timber.e

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [BaseUseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class BaseUseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(
        modelCoroutineScope: CoroutineScope,
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {

//        val job = modelCoroutineScope.async(Dispatchers.Default) { run(params) }
//        modelCoroutineScope.launch(Dispatchers.Main) {
//
//            //delay(4000)//todo for test purposes
//
//            try {
//                onResult(job.await())
//            } catch (e: Throwable) {
//                onResult(Either.Left(Failure.OtherError()))
//                    .also { e.printStackTrace() }///todo need to be logged
//            }
//        }


//        modelCoroutineScope.launch(Dispatchers.Default) {
//            val res: Either<Failure, Type>
//            res = try {
//                run(params)
//            } catch (t: Throwable) {
//                e(t)
//                Either.Left(Failure.OtherError())
//            }
//
//            modelCoroutineScope.launch(Dispatchers.Main) { onResult(res) }
//        }

//        modelCoroutineScope.launch(Dispatchers.Default) {
//            val res: Either<Failure, Type>
//            res = try {
//                run(params)
//            } catch (t: Throwable) {
//                e(t)
//                Either.Left(Failure.OtherError())
//            }
//
//            withContext(Dispatchers.Main) { onResult(res) }
//        }

//        modelCoroutineScope.launch {
//            var res: Either<Failure, Type>? = null
//            withContext(Dispatchers.Default) {
//                res = try {
//                    run(params)
//                } catch (t: Throwable) {
//                    e(t)//log error
//                    Either.Left(Failure.OtherError())//here is gonna be the most unusual error caught - no need show to user
//                }
//            }
//
//            //this run on Main(UI) thread
//            onResult(res!!)
//        }

        modelCoroutineScope.launch {
            val res: Either<Failure, Type>?
                res = try {
                    run(params)
                } catch (t: Throwable) {
                    e(t)//log error
                    Either.Left(Failure.OtherError(t.message))//here is gonna be the most unusual error caught - no need show to user
                }

            //this run on Main(UI) thread
            onResult(res)
        }

    }

    class None
}
