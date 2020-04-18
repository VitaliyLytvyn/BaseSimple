package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.domain.entity.UserEntity
import javax.inject.Inject

class CreateUserUC
@Inject constructor(private val authenticator: Authenticator) :
    BaseUseCase<Boolean, UserEntity>() {

    override suspend fun run(params: UserEntity) =
        authenticator.createUserEmailPassword(params)
}