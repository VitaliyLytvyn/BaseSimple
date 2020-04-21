package com.us.telemedicine.data.entity.response

import com.google.gson.annotations.SerializedName
import com.us.telemedicine.data.entity.BaseResponse

class CallResponse (
    @SerializedName("data")
    val responseData: CallData?,
    statusCode: Int?,
    code: Int?,
    message: String?
) : BaseResponse(statusCode, code, message)

data class CallData(
    val sessionId: String?,
    val sessionToken: String?,
    val userToken: String?,
    val caller: Caller?,
    val callee: Caller?
)

data class Caller(
    val id: String?,
    val fullName: String?
)