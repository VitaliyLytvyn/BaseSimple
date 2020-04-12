package com.me.basesimple.domain.entity

import com.me.basesimple.presentation.entity.UserEntity

data class UserAuthent(
    val uid: String ,
    val email: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val photoUrl: String? = null
){

    fun toUserEntity() = UserEntity(uid, email, name, phoneNumber, photoUrl)

    constructor(ent: UserEntity):this( ent.uid, ent.email, ent.name, ent.phoneNumber, ent.photoUrl)
}
