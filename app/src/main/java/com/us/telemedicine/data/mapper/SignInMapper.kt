package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.response.SignInResponse
import com.us.telemedicine.domain.entity.Role
import com.us.telemedicine.domain.entity.UserEntity
import com.us.telemedicine.domain.entity.UserFullInfo

class SignInMapper {
    companion object {
        fun toUseFullMapper(response: SignInResponse?) =
            if (null == response?.responseData) null
            else
                UserFullInfo(
                    accessToken = response.responseData.accessToken?.token,
                    refreshToken = response.responseData.refreshToken?.token,
                    accessTokenExpiresAt = response.responseData.accessToken?.expiresAt,
                    user = UserEntity(
                        firstName = response.responseData.userInfo?.firstName ?: "",
                        lastName = response.responseData.userInfo?.lastName ?: "",
                        email = response.responseData.userInfo?.email,
                        phone = response.responseData.userInfo?.phone,
                        role = Role.valueOf(response.responseData.userInfo?.role ?: "Unknown"),
                        password = null
                    )
                )
    }
}
