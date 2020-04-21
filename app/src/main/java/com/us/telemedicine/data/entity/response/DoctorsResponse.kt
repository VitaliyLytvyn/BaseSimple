package com.us.telemedicine.data.entity.response

import com.google.gson.annotations.SerializedName
import com.us.telemedicine.data.entity.BaseResponse

class DoctorsResponse (
    @SerializedName("data")
    val responseData: List<DoctorData>?,
    statusCode: Int?,
    code: Int?,
    message: String?
) : BaseResponse(statusCode, code, message)

data class DoctorData(
    val id: String?,
    val firstName: String?,
    val lastName: String?
)