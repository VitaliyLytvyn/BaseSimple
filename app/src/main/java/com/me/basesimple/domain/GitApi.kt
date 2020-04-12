package com.me.basesimple.domain

import com.me.basesimple.data.entity.GitRepo
import retrofit2.http.*

internal interface GitApi {

//    @GET("login")
//    suspend fun logInUser(@Query("url") url: String = ""): UserEntity

    @FormUrlEncoded
    @POST("login")
    suspend fun logInUser(@FieldMap fields: Map<String, String>): String

    @GET("repos")
    suspend fun repoes(): List<GitRepo>

}
