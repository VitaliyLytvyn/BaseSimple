package com.me.basesimple.domain.interactor

import com.me.basesimple.domain.Authenticator
import com.me.basesimple.presentation.entity.UserEntity
import javax.inject.Inject

class CreateUserEmailPasswordUC
@Inject constructor(private val authenticator: Authenticator) :
    BaseUseCase<UserEntity, Pair<String, String>>() {

    override suspend fun run(params: Pair<String, String>) =
        authenticator.createUserEmailPassword(params.first, params.second)
}