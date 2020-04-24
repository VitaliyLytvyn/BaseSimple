package com.us.telemedicine.domain.interactor

import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.DoctorEntity
import javax.inject.Inject

class GetPatientDoctorsUC
@Inject constructor(private val repository: Repository) :
    BaseUseCase<List<DoctorEntity>,  String>() {

    override suspend fun run(params:  String) =
        repository.getPatientDoctors(params)
}