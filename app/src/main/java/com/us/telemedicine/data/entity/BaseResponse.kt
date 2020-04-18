package com.us.telemedicine.data.entity

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("status")
    val statusCode: Int?,
    val code: Int?,
    val message: String?
)