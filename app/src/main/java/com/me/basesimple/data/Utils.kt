package com.me.basesimple.data

import com.google.android.gms.tasks.Task
import com.me.basesimple.domain.interactor.Either
import com.me.basesimple.global.extention.Failure
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import timber.log.Timber.e


//todo this main - UNCOMMENT????
//suspend fun <T, R> request(call: suspend () -> T, transform: (T) -> R, default: T): Either<Failure, R> {
//    return try {
//        Either.Right(transform((call() ?: default)))
//    } catch (exception: Throwable) {
//        e(exception)
//
//
//        Either.Left(Failure.ServerError())
//
//    }
//}

//fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
//    return try {
//        val response = call.execute()
//        when (response.isSuccessful) {
//            true -> {
//                Either.Right(transform((response.body() ?: default)))
//            }
//            false -> Either.Left(
//                Failure.ServerError(
//                    response.message()
//                )
//            )
//        }
//    } catch (exception: Throwable) {
//        e(exception)
//        Either.Left(Failure.ServerError())
//    }
//}

suspend fun <T, R> taskToFirebaseSet(task: Task<T>, transform: (T) -> R, default: R): Either<Failure, R> {
    return try {
        val result = task.asDeferred().await()

        if (result != null) Either.Right(transform(result))//todo check for refactor
        else Either.Right(default)
    } catch (exception: Exception) {
        e(exception)//log exception
        Either.Left(Failure.ServerError(exception.message))
    }
}

fun <T> Task<T>.asDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            // optional, handle coroutine cancellation however you'd like here
        }
    }

    this.addOnSuccessListener { result -> deferred.complete(result) }
    this.addOnFailureListener { exception -> deferred.completeExceptionally(exception) }

    return deferred
}