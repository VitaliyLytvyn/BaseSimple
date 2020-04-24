package com.us.telemedicine.domain

import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.domain.entity.PendingCallEntity
import com.us.telemedicine.domain.entity.CallEntity
import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure

interface Repository {

    suspend fun getDoctors(queryParamsMap: Map<String, String>): Either<Failure, List<DoctorEntity>>

    suspend fun getPendingCalls(): Either<Failure, List<PendingCallEntity>>

    suspend fun testCall(): Either<Failure, CallEntity?>

    suspend fun createRegularCall(patientId: String): Either<Failure, CallEntity?>

    suspend fun joinCall(sessionId: String): Either<Failure, CallEntity?>

    suspend fun leaveCall(sessionId: String): Either<Failure, Boolean>

    suspend fun getPatientDoctors(patientId: String): Either<Failure, List<DoctorEntity>>

}
