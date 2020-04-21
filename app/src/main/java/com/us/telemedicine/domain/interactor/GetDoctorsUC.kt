package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.DoctorEntity
import javax.inject.Inject

class GetDoctorsUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<List<DoctorEntity>, Map<String, String>>() {

    override suspend fun run(params: Map<String, String>) =
        repository.getDoctors(params)
}