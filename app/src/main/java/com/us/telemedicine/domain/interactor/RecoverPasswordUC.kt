package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Authenticator
import javax.inject.Inject

class RecoverPasswordUC
@Inject constructor(private val authenticator: Authenticator) :
    BaseUseCase<Boolean, String>() {

    override suspend fun run(params: String) =
        authenticator.recoverPassword(params)
}