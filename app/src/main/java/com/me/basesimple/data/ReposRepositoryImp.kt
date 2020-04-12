package com.me.basesimple.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.me.basesimple.domain.FileDownLoader.Companion.FILE_PATH
import com.me.basesimple.data.entity.ErrorResponse
import com.me.basesimple.domain.ReposRepository
import com.me.basesimple.domain.entity.Repo
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.domain.interactor.Either
import com.me.basesimple.global.extention.Failure
import com.me.basesimple.domain.platform.NetworkHandler
import com.me.basesimple.domain.platform.PreferenceHelper
import com.me.basesimple.domain.room.RepoDao
import com.me.basesimple.domain.room.entity.RepoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import timber.log.Timber.e
import javax.inject.Inject

class ReposRepositoryImp
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: GitHubService,
    private val fileLoader: FileDownLoaderImpl,
    private val preferenceHelper: PreferenceHelper,
    private val errorConverter: Converter<ResponseBody, ErrorResponse>,
    private val repoDao: RepoDao
) : ReposRepository {


    override suspend fun getOneUserFromDB(key: String): Either<Failure, UserAuthent?> {

        return Either.Left(Failure.NetworkConnection())
    }

    override suspend fun getUsersFromDB(): Either<Failure, List<UserAuthent>> {

        return Either.Left(Failure.NetworkConnection())
    }

    private suspend fun <T, R> getFromDbGeneric(
        task: Task<T>,
        transform: (T) -> R
    ): Either<Failure, R> {
        return when (networkHandler.isConnected) {
            true -> Either.Left(Failure.NetworkConnection())

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override suspend fun repoes(): Either<Failure, List<Repo>> {
        return request(
            networkHandler,
            errorConverter,
            { service.repoes() },
            { it.map { it1 -> it1.toRepo() } },
            emptyList()
        )
    }

    override suspend fun saveNewRepo(repo: RepoEntity): Either<Failure, Boolean> {
        repoDao.insertRepo(repo)
        return Either.Right(true)
    }


    override suspend fun downloadFile(pathFrom: String): Either<Failure, LiveData<String>> {
        return when (networkHandler.isConnected) {
            true -> {

                //fileLoader.downloadFrom(pathFrom, FILE_PATH)
                //Either.Right(fileLoader).also{e("NetworkRepo downloadFile ${it}")}
                Either.Right(fileLoader.downloadFrom(pathFrom, FILE_PATH))
            }

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override suspend fun saveNewUser(userAuthent: UserAuthent): Either<Failure, Boolean> {
        preferenceHelper.currentProfile = userAuthent
        return Either.Right(preferenceHelper.isLoggedIn)
    }
}

suspend fun <T, R> request(
    networkHandler: NetworkHandler,
    errorConverter: Converter<ResponseBody, ErrorResponse>,
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
            Either.Left(Failure.ServerError(error.title))
        } else
            Either.Left(Failure.ServerError())
    }
}

fun convertError(
    errorConverter: Converter<ResponseBody, ErrorResponse>,
    t: HttpException
): ErrorResponse {
    var errorResponse: ErrorResponse? = null
    val errBody = t.response()?.errorBody()
    if (errBody != null) {
        try {
            errorResponse = errorConverter.convert(errBody)
        } catch (e: com.google.gson.JsonSyntaxException) {

        }
    }
    return errorResponse ?: ErrorResponse(t.code(), null, t.message())
}