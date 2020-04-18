package com.us.telemedicine.domain

import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.data.entity.GitRepo
import com.us.telemedicine.data.entity.SignInResponse
import com.us.telemedicine.domain.entity.UserEntity
import retrofit2.http.*

internal interface GitApi {

    //@GET("user")
    //suspend fun getUser(@Query("id") id: String = ""): SignInResponse

    @FormUrlEncoded
    @POST("${URL_PREFIX}identity/signin")
    suspend fun signInUser(@FieldMap fields: Map<String, String>): SignInResponse

    @FormUrlEncoded
    @POST("${URL_PREFIX}identity/password/recovery")
    suspend fun passwordRecovery(@FieldMap fields: Map<String, String>): BaseResponse

    @GET("repos")
    suspend fun repoes(): List<GitRepo>

    @POST("${URL_PREFIX}identity/signup")
    suspend fun signUpUser(@Body user: UserEntity): BaseResponse

    @POST("${URL_PREFIX}identity/signout")
    suspend fun signOut (): BaseResponse


    companion object{
        const val URL_PREFIX = "api/v1/"
    }
}