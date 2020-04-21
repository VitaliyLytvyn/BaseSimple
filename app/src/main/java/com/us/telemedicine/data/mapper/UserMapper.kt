package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.response.SignInResponse


class UserMapper {

    companion object {
        fun toUserAuth(u: SignInResponse?) =
            if (null == u) u
            else { 1
//                UserAuthent(
//                    uid = "1" //u.uid,
//                    email = u.email,
//                    name = u.name,
//                    phoneNumber = u.phoneNumber,
//                    photoUrl = u.photoUrl.toString()
//                )
            }
    }
}