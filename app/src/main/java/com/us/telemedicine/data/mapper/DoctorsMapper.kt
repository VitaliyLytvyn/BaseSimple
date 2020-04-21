package com.us.telemedicine.data.mapper

import com.us.telemedicine.data.entity.response.DoctorData
import com.us.telemedicine.domain.entity.DoctorEntity

class DoctorsMapper {
    companion object {
        fun toDoctorEntity(input: DoctorData?): DoctorEntity? {
            input?.id ?: return null

            return DoctorEntity(
                id = input.id,
                firstName = input.firstName ?: "",
                lastName = input.lastName ?: ""
            )
        }

        fun toDoctorEntityList(input: List<DoctorData>?): List<DoctorEntity> {
            input ?: return emptyList()
            return input.mapNotNull { doctor -> toDoctorEntity(doctor) }
        }
    }
}
