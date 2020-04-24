package com.us.telemedicine.domain

import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.data.entity.request.ChangePasswordRequest
import com.us.telemedicine.data.entity.request.RecoverPasswordRequest
import com.us.telemedicine.data.entity.request.SignInRequest
import com.us.telemedicine.data.entity.response.SignInResponse
import com.us.telemedicine.data.entity.request.SignUpRequestEntity
import com.us.telemedicine.data.entity.response.DoctorsResponse
import com.us.telemedicine.data.entity.response.PendingCallsResponse
import com.us.telemedicine.data.entity.response.CallResponse
import retrofit2.http.*

internal interface GitApi {

    //-----/identity
    @POST("${URL_PREFIX}/identity/signin")
    suspend fun signInUser(@Body signInRequest: SignInRequest): SignInResponse

    @POST("${URL_PREFIX}/identity/password/recovery")
    suspend fun passwordRecovery(@Body recoverRequest: RecoverPasswordRequest): BaseResponse

    @POST("${URL_PREFIX}/identity/signup")
    suspend fun signUpUser(@Body user: SignUpRequestEntity): BaseResponse

    @POST("${URL_PREFIX}/identity/signout")
    suspend fun signOut(): BaseResponse

    @PUT("${URL_PREFIX}/identity/password")
    suspend fun changePassword(@Body changePassword: ChangePasswordRequest): BaseResponse


    //-----/doctors
    @GET("${URL_PREFIX}/doctors")
    suspend fun getDoctors(@QueryMap queryParamsMap: Map<String, String>): DoctorsResponse

    //-----/conversations
    @GET("${URL_PREFIX}/conversations/pending")
    suspend fun getPendingCalls(): PendingCallsResponse

    @POST("${URL_PREFIX}/conversations/test")
    suspend fun testCall(): CallResponse

    @POST("${URL_PREFIX}/conversations/{patientId}")
    suspend fun createRegularCall(@Path("patientId") patientId: String): CallResponse

    @POST("${URL_PREFIX}/conversations/{sessionId}/join")
    suspend fun joinCall(@Path("sessionId") sessionId: String): CallResponse

    @POST("${URL_PREFIX}/conversations/{sessionId}/leave")
    suspend fun leaveCall(@Path("sessionId") sessionId: String): BaseResponse

    //-----/patients
    @GET("${URL_PREFIX}/patients/{patientId}/doctors")
    suspend fun getPatientDoctors(@Path("patientId") patientId: String): DoctorsResponse

    companion object {
        const val URL_PREFIX = "api/v1"
    }
}