package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.request.SignUpRequestEntity
import com.us.telemedicine.domain.entity.UserEntity

class SignUpMapper {
    companion object {
        fun toSignUpRequest(input: UserEntity) =
            SignUpRequestEntity(
                firstName = input.firstName,
                lastName = input.lastName,
                email = input.email,
                phone = input.phone,
                role = input.role.name,
                password = input.password!!
            )
    }
}