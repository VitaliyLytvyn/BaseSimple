package com.us.telemedicine.domain.entity

class CallEntity (
    val sessionId: String?,
    val sessionToken: String?,
    val userToken: String?,
    val caller: CallerEntity?,
    val callee: CallerEntity?
)

data class CallerEntity(
    val id: String?,
    val fullName: String?
)