package com.us.telemedicine.di

import com.us.telemedicine.BuildConfig
import com.us.telemedicine.data.AuthenticatorImpl
import com.us.telemedicine.data.RepositoryImp
import com.us.telemedicine.data.entity.BaseResponse
import com.us.telemedicine.domain.Authenticator
import com.us.telemedicine.domain.Repository
import com.us.telemedicine.data.PreferenceHelper

import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideResponseBodyConverter(retrofit: Retrofit): Converter<ResponseBody, BaseResponse> {
        return retrofit.responseBodyConverter(BaseResponse::class.java, emptyArray())
    }

    @Provides
    @Singleton
    fun createClient(
        @Named("auth") authInterceptor: Interceptor,
        @Named("logger") loggerInterceptor: Interceptor
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)

            addInterceptor(loggerInterceptor)
            addInterceptor(authInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRepoesRepository(dataSource: RepositoryImp): Repository = dataSource

    @Provides
    @Singleton
    fun provideAuthenticator(authenticator: AuthenticatorImpl): Authenticator =
        authenticator

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthorisationInterceptor(preferenceHelper: PreferenceHelper) = object : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()
            val accessToken = preferenceHelper.accessToken

            if (accessToken != null) {
                builder.addHeader("Authorization", accessToken)
            }
            return chain.proceed(builder.build())
        }
    }

    @Provides
    @Singleton
    @Named("logger")
    fun provideLoggerInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }
}
