package com.us.telemedicine.data.entity.request

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)