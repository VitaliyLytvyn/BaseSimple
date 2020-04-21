package com.us.telemedicine.data.entity.response

import com.google.gson.annotations.SerializedName
import com.us.telemedicine.data.entity.BaseResponse

class PendingCallsResponse(
    @SerializedName("data")
    val responseData: List<PendingCall>?,
    statusCode: Int?,
    code: Int?,
    message: String?
) : BaseResponse(statusCode, code, message)

data class PendingCall(
    val sessionId: String?,
    val callerName: String?,
    val pendingSince: String?,
    val isAutoAnswerEnabled: Boolean?,
    val isMeeting: Boolean?
)