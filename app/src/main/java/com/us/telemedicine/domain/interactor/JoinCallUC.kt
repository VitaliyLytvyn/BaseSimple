package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.CallEntity
import javax.inject.Inject

class JoinCallUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<CallEntity?, String>() {

    override suspend fun run(params:  String) =
        repository.joinCall(params)
}