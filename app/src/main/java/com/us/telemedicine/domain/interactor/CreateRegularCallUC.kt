package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.CallEntity
import javax.inject.Inject

class CreateRegularCallUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<CallEntity?, String>() {

    override suspend fun run(params: String) =
        repository.createRegularCall(params)
}