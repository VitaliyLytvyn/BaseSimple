package com.us.telemedicine.data

import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.data.mapper.DoctorsMapper
import com.us.telemedicine.data.mapper.PendingCallMapper
import com.us.telemedicine.data.mapper.CallResponseMapper
import com.us.telemedicine.domain.Repository
import com.us.telemedicine.domain.entity.DoctorEntity
import com.us.telemedicine.domain.entity.PendingCallEntity
import com.us.telemedicine.domain.entity.CallEntity
import com.us.telemedicine.global.Either
import com.us.telemedicine.global.extention.Failure
import com.us.telemedicine.domain.platform.NetworkHandler
import com.us.telemedicine.domain.platform.PreferenceHelper
import okhttp3.ResponseBody
import retrofit2.Converter
import javax.inject.Inject

class RepositoryImp
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: GitHubService,
    private val preferenceHelper: PreferenceHelper,
    private val errorConverter: Converter<ResponseBody, BaseResponse>
) : Repository {

    override suspend fun getDoctors(queryParamsMap: Map<String, String>): Either<Failure, List<DoctorEntity>> {
        return request(
            networkHandler,
            errorConverter,
            {
                val response = service.getDoctors(queryParamsMap)
                response.responseData
            },
            {
                DoctorsMapper.toDoctorEntityList(it)
            },
            emptyList()
        )
    }

    override suspend fun getPendingCalls(): Either<Failure, List<PendingCallEntity>> {
        return request(
            networkHandler,
            errorConverter,
            {
                val response = service.getPendingCalls()
                response.responseData
            },
            {
                PendingCallMapper.toPendingCallEntityList(it)
            },
            emptyList()
        )
    }

    override suspend fun testCall(): Either<Failure, CallEntity?> {
        return request(
            networkHandler,
            errorConverter,
            {
                val response = service.testCall()
                response.responseData
            },
            {
                CallResponseMapper.toCallEntity(it)
            },
            null //Either.Left(Failure.OtherError()) //CallEntity(null, null,null,null,null)
        )
    }

    override suspend fun createRegularCall(patientId: String): Either<Failure, CallEntity?> {
        return request(
            networkHandler,
            errorConverter,
            {
                val response = service.createRegularCall(patientId)
                response.responseData
            },
            {
                CallResponseMapper.toCallEntity(it)
            },
            null
        )
    }

    override suspend fun joinCall(sessionId: String): Either<Failure, CallEntity?> {
        return request(
            networkHandler,
            errorConverter,
            {
                val response = service.joinCall(sessionId)
                response.responseData
            },
            {
                CallResponseMapper.toCallEntity(it)
            },
            null
        )
    }

    override suspend fun leaveCall(sessionId: String): Either<Failure, Boolean> {
        return request(
            networkHandler,
            errorConverter,
            {
                service.leaveCall(sessionId)
            },
            {
                true
            },
            false
        )
    }
}