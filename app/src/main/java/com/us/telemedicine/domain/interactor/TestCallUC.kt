package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.CallEntity
import javax.inject.Inject

class TestCallUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<CallEntity?, BaseUseCase.None>() {

    override suspend fun run(params: None) =
        repository.testCall()
}