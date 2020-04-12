package com.me.basesimple.data

import androidx.lifecycle.LiveData
import com.me.basesimple.data.entity.ErrorResponse
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.domain.interactor.Either
import com.me.basesimple.presentation.entity.UserEntity
import com.me.basesimple.global.extention.Failure
import com.me.basesimple.domain.platform.NetworkHandler
import com.me.basesimple.domain.platform.PreferenceHelper
import okhttp3.ResponseBody
import retrofit2.Converter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticatorLocalDB
@Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val service: GitHubService,
    private val networkHandler: NetworkHandler,
    private val errorConverter: Converter<ResponseBody, ErrorResponse>
) : Authenticator {

    override fun isLogedIn() = preferenceHelper.isLoggedIn

    override fun logOutUser(): Either<Failure, Boolean> {
        preferenceHelper.currentProfile = null
        return Either.Right(preferenceHelper.currentProfile == null)
    }

    override fun observeUser(): LiveData<UserEntity?> {
        return preferenceHelper.profileAsLiveData
    }

    override suspend fun signInEmailPassword(
        email: String,
        password: String
    ): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {
            true -> {
                Either.Right(true)
            }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override suspend fun createUserEmailPassword(
        email: String,
        password: String
    ): Either<Failure, UserEntity> {

        return request(
            networkHandler,
            errorConverter,
            {
                val valuesMapped =
                    mutableMapOf("email" to "me@me.net", "password" to "123456q")
                service.logInUser(valuesMapped)
            },
            {
                UserEntity("myId", "MyEmail", "MyName", "", "")
            },
            UserEntity("defaultId", "", "", "", "")
        )
    }
}
