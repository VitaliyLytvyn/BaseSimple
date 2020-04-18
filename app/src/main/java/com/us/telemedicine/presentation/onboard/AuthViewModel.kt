package com.us.telemedicine.presentation.onboard

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.us.telemedicine.R
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.domain.interactor.CreateUserUC
import com.us.telemedicine.domain.interactor.RecoverPasswordUC
import com.us.telemedicine.domain.interactor.SignInUserEmailPasswordUC
import com.us.telemedicine.global.BaseViewModel
import com.us.telemedicine.global.SingleLiveEvent
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.domain.interactor.SignOutUserUC
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val createUserUC: CreateUserUC,
    private val signInUserEmailPasswordUC: SignInUserEmailPasswordUC,
    private val recoverPasswordUC: RecoverPasswordUC,
    private val authenticator: Authenticator

) : BaseViewModel() {

    private val _signInResult = SingleLiveEvent<Boolean>()
    val signInResult: LiveData<Boolean> = _signInResult

    private val _recoveryResult = SingleLiveEvent<Boolean>()
    val recoveryResult: LiveData<Boolean> = _recoveryResult

    private val _signUpResult = SingleLiveEvent<Boolean>()
    val signUpResult: LiveData<Boolean> = _signUpResult

    private val _signInForm = MutableLiveData<SignInFormState>()
    val signInFormState: LiveData<SignInFormState> = _signInForm

    private val _resetPasswordForm = MutableLiveData<RecoverFormState>()
    val resetPasswordFormState: LiveData<RecoverFormState> = _resetPasswordForm

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    val authenticationState = authenticator.observeLoggedInStatus().map { status ->
        if (status == true) {
            Authenticator.AuthenticationState.AUTHENTICATED
        } else {
            Authenticator.AuthenticationState.UNAUTHENTICATED
        }
    }

    fun logIn(emailOrPhone: String, password: String) {
        signInUserEmailPasswordUC(viewModelScope, emailOrPhone to password) {
            it.fold(
                ::handleFailure,
                ::handleSignInResult
            )
        }
    }

    fun recoverPassword(email: String) {
        recoverPasswordUC(viewModelScope, email) {
            it.fold(
                ::handleFailure,
                ::handleRecoveryResult
            )
        }
    }

    fun signUp(user: UserEntity) {
        createUserUC(viewModelScope, user) {
            it.fold(
                ::handleFailure,
                ::handleSignUpResult
            )
        }
    }

    private fun handleRecoveryResult(result: Boolean) {
        _recoveryResult.value = result
    }

    private fun handleSignInResult(result: Boolean) {
        _signInResult.value = result
    }

    private fun handleSignUpResult(result: Boolean) {
        _signUpResult.value = result
    }

    // ------------------------Validating forms --------------------------

    // Recovery
    fun resetDataChanged(email: String) {
        if (!isEmailValid(email)) {
            _resetPasswordForm.value =
                RecoverFormState(emailError = R.string.invalid_email)
        } else {
            _resetPasswordForm.value = RecoverFormState(isDataValid = true)
        }
    }

    // Sign in
    fun signInDataChanged(emailOrPhone: String, password: String) {
        if (!isEmailOrPhoneValid(emailOrPhone)) {
            _signInForm.value = SignInFormState(usernameError = R.string.invalid_email_or_phone)
        } else if (!isPasswordValid(password)) {
            _signInForm.value = SignInFormState(passwordError = R.string.invalid_password)
        } else {
            _signInForm.value = SignInFormState(isDataValid = true)
        }
    }

    // Sign up
    fun signUpDataChanged(
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        password: String,
        confirmPassword: String
    ) {
        if (phone.isBlank() and email.isBlank()) {
            _signUpForm.value = SignUpFormState(emailError = R.string.email_or_phone_needed)
        } else if (email.isNotBlank() && !isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (phone.isNotBlank() && !isPhoneValid(phone)) {
            _signUpForm.value = SignUpFormState(phoneError = R.string.invalid_phone)
        } else if (!isNameValid(firstName)) {
            _signUpForm.value = SignUpFormState(firstNameError = R.string.invalid_first_name)
        } else if (!isNameValid(lastName)) {
            _signUpForm.value = SignUpFormState(lastNameError = R.string.invalid_last_name)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _signUpForm.value = SignUpFormState(confirmPasswordError = R.string.password_not_match)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    // A placeholder emailOrPhone validation check
    private fun isEmailOrPhoneValid(emailOrPhone: String): Boolean {
        return when {
            emailOrPhone.matches("[0-9]+".toRegex()) -> {
                emailOrPhone.length in 8..10
            }
            emailOrPhone.contains('@') -> {
                Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches()
            }
            else -> {
                false
            }
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return when {
            email.contains('@') -> {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
            else -> {
                false
            }
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    // A placeholder password validation check
    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPhoneValid(phone: String): Boolean {
        return phone.matches("[0-9]+".toRegex()) && phone.length in 8..10
    }

}