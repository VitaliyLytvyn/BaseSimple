package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Authenticator
import javax.inject.Inject

class SignOutUserUC
@Inject  constructor(private val authenticator: Authenticator) :
    BaseUseCase<Boolean, Any>() {

    override suspend fun run(params: Any) =
        authenticator.signOutUser()
}