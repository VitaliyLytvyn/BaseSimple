package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.PendingCallEntity
import javax.inject.Inject

class GetPendingCalls
@Inject constructor(private val repository: Repository) :
    BaseUseCase<List<PendingCallEntity>, BaseUseCase.None>() {

    override suspend fun run(params: None) =
        repository.getPendingCalls()
}