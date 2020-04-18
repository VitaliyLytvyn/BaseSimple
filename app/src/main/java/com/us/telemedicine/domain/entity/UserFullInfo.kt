package com.us.telemedicine.domain.entity

class UserFullInfo(
    val user: UserEntity,
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresAt: String?
)