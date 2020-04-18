package com.us.telemedicine.domain.interactor


import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure
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
