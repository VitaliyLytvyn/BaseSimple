package com.us.telemedicine.presentation

/**
 * Data validation state of the recover form.
 */
data class ChooseDoctorFormState(
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)