package com.us.telemedicine.data

import androidx.lifecycle.LiveData
import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.data.entity.request.RecoverPasswordRequest
import com.us.telemedicine.data.entity.request.SignInRequest
import com.us.telemedicine.data.mapper.SignInMapper
import com.us.telemedicine.data.mapper.SignUpMapper
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.global.Either
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.global.extention.Failure
import com.us.telemedicine.global.extention.asDate
import okhttp3.ResponseBody
import retrofit2.Converter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticatorImpl
@Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val service: GitHubService,
    private val networkHandler: NetworkHandler,
    private val errorConverter: Converter<ResponseBody, BaseResponse>
) : Authenticator {

    override fun isLoggedIn() = preferenceHelper.isLoggedIn

    override fun isSignInRequired(): Boolean {
        // No token -> need sign in
        preferenceHelper.accessToken ?: return true

        // No token expire date -> need sign in
        val tokenExpiresAt = preferenceHelper.tokenExpiresAt ?: return true

        // Token expire date before Now -> need sign in, else -> No need(return false)
        return tokenExpiresAt.asDate()?.before(Date()) ?: true
    }

    override suspend fun signOutUser(): Either<Failure, Boolean> {
        return request(
            networkHandler,
            errorConverter,
            {
                service.signOut()
                preferenceHelper.accessToken = null
                preferenceHelper.clearAll()
                preferenceHelper.isLoggedIn
            },
            {
                it
            },
            false
        )
    }

    override fun observeUser(): LiveData<UserEntity?> {
        return preferenceHelper.profileAsLiveData
    }

    override fun observeLoggedInStatus(): LiveData<Boolean?> {
        return preferenceHelper.loginStatusAsLiveData
    }

    override suspend fun signInEmailOrPhonePassword(
        emailOrPhone: String,
        password: String
    ): Either<Failure, Boolean> {
        return request(
            networkHandler,
            errorConverter,
            {
                val request = SignInRequest(username = emailOrPhone, password = password)
                val response = service.signInUser(request)
                val signInResult = SignInMapper.toUseFullMapper(response)
                signInResult?.let {
                    preferenceHelper.currentProfile = it.user
                    preferenceHelper.accessToken = it.accessToken
                    preferenceHelper.refreshToken = it.refreshToken
                    preferenceHelper.tokenExpiresAt = it.accessTokenExpiresAt
                }
                preferenceHelper.isLoggedIn
            },
            {
                it
            },
            false
        )
    }

    override suspend fun recoverPassword(email: String): Either<Failure, Boolean> {
        return request(
            networkHandler,
            errorConverter,
            {
                val recoverRequest = RecoverPasswordRequest(email = email)
                service.passwordRecovery(recoverRequest)
            },
            {
                true
            },
            false
        )
    }

    override suspend fun createUserEmailPassword(user: UserEntity): Either<Failure, Boolean> {
        return request(
            networkHandler,
            errorConverter,
            {
                val requestUser = SignUpMapper.toSignUpRequest(user)
                service.signUpUser(requestUser)
            },
            {
                true
            },
            false
        )
    }

    // Alternative - check token with Auth0 JWT library
//    val token =
//        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE1ODcwNjkwOTYsImV4cCI6MTYxODYwNTA5NiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IlZpdGFsIiwiU3VybmFtZSI6Ikx5dCIsIkVtYWlsIjoibWVza3lAZG90LmNvbSIsIlJvbGUiOiJEb2N0b3IifQ.Wr68S7mMhQi6MfvVHKwChd5K9vWULUI9w5Hx15HEIwg"
//    override fun isSignInRequired(): Boolean {
//        val token = preferenceHelper.accessToken
//        token ?: return true
//
//        val jwt = try {
//            JWT(token)
//        } catch (ex: Exception) {
//            Timber.e(ex)
//            null
//        }
//        jwt ?: return true
//
//        val isExpired = jwt.isExpired(0)
//
//        val expiresAt = jwt.expiresAt
//        val name = jwt.getClaim("GivenName")
//        val surname = jwt.getClaim("Surname")
//        val email = jwt.getClaim("Email")
//        val role = jwt.getClaim("Role")
//
//        return isExpired
//    }

}
