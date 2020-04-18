package com.us.telemedicine.presentation.onboard

/**
 * Data validation state of the recover form.
 */
data class RecoverFormState(
    val emailError: Int? = null,
    val isDataValid: Boolean = false
)