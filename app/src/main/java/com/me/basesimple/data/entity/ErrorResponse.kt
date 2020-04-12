package com.me.basesimple.data.entity

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    val status: Int?,
    val type: String?,
    val title: String?
)