package com.me.basesimple.di

import com.me.basesimple.BuildConfig
import com.me.basesimple.data.AuthenticatorLocalDB
import com.me.basesimple.data.ReposRepositoryImp
import com.me.basesimple.data.entity.ErrorResponse
import com.me.basesimple.domain.Authenticator
import com.me.basesimple.domain.ReposRepository

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideResponseBodyConverter(retrofit: Retrofit): Converter<ResponseBody, ErrorResponse> {
        return retrofit.responseBodyConverter(ErrorResponse::class.java, emptyArray())
    }

    @Provides
    @Singleton
    fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS);
            connectTimeout(60, TimeUnit.SECONDS)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRepoesRepository(dataSource: ReposRepositoryImp): ReposRepository = dataSource

    @Provides
    @Singleton
    fun provideAuthenticator(authenticator: AuthenticatorLocalDB): Authenticator =
        authenticator
}
