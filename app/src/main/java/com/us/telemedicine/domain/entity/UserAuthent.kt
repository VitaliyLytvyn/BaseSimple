package com.us.telemedicine.domain.entity

data class UserAuthent(
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val password: String,
    val role: String
) {

    fun toUserEntity() = UserEntity(
        firstName,
        lastName,
        email,
        phone,
        password,
        Role.valueOf(role)
    )
}
