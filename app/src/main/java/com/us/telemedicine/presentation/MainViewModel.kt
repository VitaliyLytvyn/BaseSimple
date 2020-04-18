package com.us.telemedicine.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.domain.interactor.SignOutUserUC
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.SingleLiveEvent
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val authenticator: Authenticator,
    private val signOutUserUC: SignOutUserUC
) : BaseViewModel() {

    private val _signOutResult = SingleLiveEvent<Boolean>()
    val signOutResult: LiveData<Boolean> = _signOutResult

    fun isLoggedIn() = authenticator.isLoggedIn()

    fun signOut() {
        signOutUserUC(viewModelScope, true) {
            it.fold(
                ::handleFailure,
                ::handleSignOutResult
            )
        }
    }

    private fun handleSignOutResult(result: Boolean) {
        _signOutResult.value = result
    }

    fun isSignInRequired() = authenticator.isSignInRequired()

    val authenticationState = authenticator.observeLoggedInStatus().map { status ->
        if (status == true) {
            Authenticator.AuthenticationState.AUTHENTICATED
        } else {
            Authenticator.AuthenticationState.UNAUTHENTICATED
        }
    }

}