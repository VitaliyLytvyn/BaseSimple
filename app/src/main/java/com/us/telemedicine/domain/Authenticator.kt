package com.us.telemedicine.domain

import androidx.lifecycle.LiveData
import com.us.telemedicine.global.Either
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.global.extention.Failure

interface Authenticator {

    fun isLoggedIn(): Boolean

    fun isSignInRequired(): Boolean

    suspend fun signOutUser(): Either<Failure, Boolean>

    fun observeUser(): LiveData<UserEntity?>

    fun observeLoggedInStatus(): LiveData<Boolean?>

    suspend fun signInEmailOrPhonePassword(
        emailOrPhone: String,
        password: String
    ): Either<Failure, Boolean>

    suspend fun recoverPassword(email: String): Either<Failure, Boolean>

    suspend fun createUserEmailPassword(user: UserEntity): Either<Failure, Boolean>

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }
}