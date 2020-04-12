package com.me.basesimple.domain

import androidx.lifecycle.LiveData
import com.me.basesimple.domain.interactor.Either
import com.me.basesimple.presentation.entity.UserEntity
import com.me.basesimple.global.extention.Failure

interface Authenticator {

    fun isLogedIn(): Boolean

    fun logOutUser(): Either<Failure, Boolean>

    fun observeUser(): LiveData<UserEntity?>

    suspend fun signInEmailPassword(email: String, password: String): Either<Failure, Boolean>

    suspend fun createUserEmailPassword(
        email: String,
        password: String
    ): Either<Failure, UserEntity>

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }
}