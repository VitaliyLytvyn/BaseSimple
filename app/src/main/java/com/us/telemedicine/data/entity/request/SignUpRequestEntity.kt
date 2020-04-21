package com.us.telemedicine.data.entity.request

data class SignUpRequestEntity(
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val password: String,
    val role: String
)