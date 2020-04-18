package com.us.telemedicine.presentation.onboard

/**
 * Data validation state of the signin form.
 */
data class SignInFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
