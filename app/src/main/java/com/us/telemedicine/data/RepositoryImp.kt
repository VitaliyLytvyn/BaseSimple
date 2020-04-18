package com.us.telemedicine.data

import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.Repo
import com.us.telemedicine.domain.entity.UserAuthent
import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure
import com.us.telemedicine.domain.platform.NetworkHandler
import com.us.telemedicine.domain.platform.PreferenceHelper
import okhttp3.ResponseBody
import retrofit2.Converter
import javax.inject.Inject

class RepositoryImp
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: GitHubService,
    private val preferenceHelper: PreferenceHelper,
    private val errorConverter: Converter<ResponseBody, BaseResponse>
) : Repository {

    override suspend fun repoes(): Either<Failure, List<Repo>> {
        return request(
            networkHandler,
            errorConverter,
            { service.repoes() },
            { it.map { it1 -> it1.toRepo() } },
            emptyList()
        )
    }

    override suspend fun saveNewUser(userAuthent: UserAuthent): Either<Failure, Boolean> {
        //preferenceHelper.currentProfile = userAuthent
        return Either.Right(preferenceHelper.isLoggedIn)
    }
}