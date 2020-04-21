package com.us.telemedicine.domain.entity

data class DoctorEntity(
    val id: String,
    val firstName: String,
    val lastName: String
) {
    val fullName
        get() = "$firstName $lastName".trim()
}