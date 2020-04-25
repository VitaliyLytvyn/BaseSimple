package com.us.telemedicine.domain

import androidx.lifecycle.LiveData
import com.us.telemedicine.domain.entity.Role
import com.us.telemedicine.global.Either
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.global.extention.Failure

interface Authenticator {

    // Regular response
    fun isLoggedIn(): Boolean
    fun isSignInRequired(): Boolean
    fun getUserType(): Role?
    fun getUserId(): String?

    // LiveData response
    fun observeUser(): LiveData<UserEntity?>
    fun observeLoggedInStatus(): LiveData<Boolean?>

    // Suspend response
    suspend fun signOutUser(): Either<Failure, Boolean>
    suspend fun signInEmailOrPhonePassword(
        emailOrPhone: String,
        password: String
    ): Either<Failure, UserEntity?>

    suspend fun recoverPassword(email: String): Either<Failure, Boolean>
    suspend fun createUserEmailPassword(user: UserEntity): Either<Failure, Boolean>

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }
}