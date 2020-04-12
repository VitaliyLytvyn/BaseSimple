package com.me.basesimple.presentation

import androidx.lifecycle.map
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.global.BaseViewModel
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val authenticator: Authenticator
) : BaseViewModel() {

    init {
//        getProfileLiveDataUC(viewModelScope, null) {
//            it.fold(
//                ::handleFailure,
//                ::handleGetProfileLiveDta
//            )
//        }
    }

    fun isLoggedIn() = authenticator.isLogedIn()

    val authenticationState = authenticator.observeUser().map { user->
        if (user != null) {
            Authenticator.AuthenticationState.AUTHENTICATED
        } else {
            Authenticator.AuthenticationState.UNAUTHENTICATED
        }
    }

}