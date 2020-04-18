package com.us.telemedicine.presentation.onboard

/**
 * Data validation state of the sign up form.
 */
data class SignUpFormState(
    val emailError: Int? = null,
    val phoneError: Int? = null,
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)