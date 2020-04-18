package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Authenticator
import javax.inject.Inject

class SignInUserEmailPasswordUC
@Inject constructor(private val authenticator: Authenticator) :
    BaseUseCase<Boolean, Pair<String, String>>() {

    override suspend fun run(params: Pair<String, String>) =
        authenticator.signInEmailOrPhonePassword(params.first, params.second)
}