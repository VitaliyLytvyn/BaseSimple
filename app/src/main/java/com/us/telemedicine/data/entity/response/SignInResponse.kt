package com.us.telemedicine.data.entity.response

import com.google.gson.annotations.SerializedName
import com.us.telemedicine.data.entity.BaseResponse

class SignInResponse(
    @SerializedName("data")
    val responseData: SignInResponseData?,
    statusCode: Int?,
    code: Int?,
    message: String?
) : BaseResponse(statusCode, code, message)

class SignInResponseData(
    val userInfo: UserInfo?,
    val accessToken: Token?,
    val refreshToken: Token?
)

data class UserInfo(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phone: String?,
    val role: String?
)

data class Token(
    val token: String?,
    val expiresAt: String?
)