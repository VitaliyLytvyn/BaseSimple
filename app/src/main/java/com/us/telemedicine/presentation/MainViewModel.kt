package com.us.telemedicine.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.domain.interactor.BaseUseCase
import com.us.telemedicine.domain.interactor.GetPatientDoctorsUC
import com.us.telemedicine.domain.interactor.SignOutUserUC
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.SingleLiveEvent
import timber.log.Timber.d
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val authenticator: Authenticator,
    private val signOutUserUC: SignOutUserUC,
    private val getPatientDoctorsUC: GetPatientDoctorsUC
) : BaseViewModel() {

    private val _signOutResult = SingleLiveEvent<Boolean>()
    val signOutResult: LiveData<Boolean> = _signOutResult

    private val _patientDoctorsResult = MutableLiveData<List<DoctorEntity>>()
    val patientDoctorsResult: LiveData<List<DoctorEntity>> = _patientDoctorsResult

    fun isLoggedIn() = authenticator.isLoggedIn()

    fun getUserType() = authenticator.getUserType()

    fun signOut() {
        signOutUserUC(viewModelScope, BaseUseCase.None()) {
            it.fold(
                ::handleFailure,
                ::handleSignOutResult
            )
        }
    }

    fun getPatientDoctors() {
        authenticator.getUserId()?.let { id ->
            getPatientDoctorsUC(viewModelScope, id) {
                it.fold(
                    ::handleFailure,
                    ::handleGetPatientDoctorsResult
                )
            }
        }
    }

    private fun handleGetPatientDoctorsResult(result: List<DoctorEntity>) {
        _patientDoctorsResult.value = result
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