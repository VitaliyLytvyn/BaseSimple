package com.us.telemedicine.domain

import com.us.telemedicine.domain.entity.Repo
import com.us.telemedicine.domain.entity.UserAuthent
import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure

interface Repository {
    suspend fun repoes(): Either<Failure, List<Repo>>

    suspend fun saveNewUser(userAuthent: UserAuthent): Either<Failure, Boolean>
}
