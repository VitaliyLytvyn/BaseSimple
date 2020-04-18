package com.us.telemedicine.data

import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.data.entity.SignInResponse
import com.us.telemedicine.domain.GitApi
import com.us.telemedicine.domain.platform.NetworkHandler
import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure
import com.us.telemedicine.domain.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import timber.log.Timber.e
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubService
@Inject constructor(retrofit: Retrofit) : GitApi {

    private val gitApi by lazy { retrofit.create(GitApi::class.java) }

    override suspend fun signInUser(fields: Map<String, String>): SignInResponse {
        return gitApi.signInUser(fields)
    }

    override suspend fun passwordRecovery(fields: Map<String, String>): BaseResponse {
        return gitApi.passwordRecovery(fields)
    }

    override suspend fun repoes() = gitApi.repoes()

    override suspend fun signUpUser(user: UserEntity): BaseResponse {
        return gitApi.signUpUser(user)
    }

    override suspend fun signOut(): BaseResponse {
        return gitApi.signOut()
    }
}

suspend fun <T, R> request(
    networkHandler: NetworkHandler,
    errorConverter: Converter<ResponseBody, BaseResponse>,
    call: suspend () -> T,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    if (networkHandler.isConnected != true) return Either.Left(Failure.NetworkConnection())
    return try {
        Either.Right(
            //Dispatchers.Default - needed only for transform part, Retrofit(Room) suspend calls are main-safe
            withContext(Dispatchers.Default) {
                transform((call() ?: default))
            }
        )
    } catch (exception: Throwable) {
        e(exception)

        if (exception is HttpException) {
            val error = convertError(errorConverter, exception)
            Either.Left(Failure.ServerError(error.message))
        } else
            Either.Left(Failure.ServerError())
    }
}

fun convertError(
    errorConverter: Converter<ResponseBody, BaseResponse>,
    t: HttpException
): BaseResponse {
    var errorResponse: BaseResponse? = null
    val errBody = t.response()?.errorBody()
    if (errBody != null) {
        try {
            errorResponse = errorConverter.convert(errBody)
        } catch (ex: Exception) {
            when (ex) {
                is com.google.gson.JsonSyntaxException, is com.google.gson.stream.MalformedJsonException -> {
                    e(ex)
                }
                else -> throw ex
            }
        }
    }

    return errorResponse ?: BaseResponse(t.code(), null, t.message())
}