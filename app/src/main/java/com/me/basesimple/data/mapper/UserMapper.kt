package com.me.basesimple.data.mapper


class UserMapper {

    companion object {
        fun toUserAuth(u: String?) =
            if (null == u) u
            else {
//                UserAuthent(
//                    uid = u.uid,
//                    email = u.email,
//                    name = u.displayName,
//                    phoneNumber = u.phoneNumber,
//                    photoUrl = u.photoUrl.toString()
//                )
            }
    }
}