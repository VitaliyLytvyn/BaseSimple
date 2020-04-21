package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import javax.inject.Inject

class LeaveCallUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<Boolean, String>() {

    override suspend fun run(params:  String) =
        repository.leaveCall(params)
}