package com.me.basesimple.presentation.onboard

import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.domain.interactor.CreateUserEmailPasswordUC
import com.me.basesimple.domain.interactor.SaveUserUC
import com.me.basesimple.presentation.entity.UserEntity
import com.me.basesimple.global.BaseViewModel
import com.me.basesimple.domain.platform.PreferenceHelper
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val createUserEmailPasswordUC: CreateUserEmailPasswordUC,
    private val authenticator: Authenticator,
    private val saveUserUC: SaveUserUC,
    private val preferenceHelper: PreferenceHelper

) : BaseViewModel() {

     val authenticationState = authenticator.observeUser().map { user->
         if (user != null) {
             //handleFailure(Failure.ServerError("my ser"))
             Authenticator.AuthenticationState.AUTHENTICATED
         } else {
             Authenticator.AuthenticationState.UNAUTHENTICATED
         }
     }

    fun logIn() {
        preferenceHelper.currentProfile = UserAuthent(
            uid = "11"
        )
    }

    fun saveUser(user: UserEntity) =
        saveUserUC(viewModelScope, user) { it.fold(::handleFailure, ::handleSave) }

    private fun handleSave(b: Boolean) {
        //this._saveResult.value = b
    }
}