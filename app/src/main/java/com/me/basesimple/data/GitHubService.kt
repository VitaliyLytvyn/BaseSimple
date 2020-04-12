package com.me.basesimple.data

import com.me.basesimple.domain.GitApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubService
@Inject constructor(retrofit: Retrofit) : GitApi {

    private val gitApi by lazy { retrofit.create(GitApi::class.java) }

    //override  suspend fun logInUser(url: String): UserEntity {
    override suspend fun logInUser(fields: Map<String, String>): String {
        return gitApi.logInUser(fields) //todo
    }

    override suspend fun repoes() = gitApi.repoes()
}
