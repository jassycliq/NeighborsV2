package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.BuildConfig
import com.playbowdogs.neighbors.data.api.AcuityApiService
import com.playbowdogs.neighbors.data.api.AngelCamApiService
import com.playbowdogs.neighbors.data.api.BasicAuthInterceptor
import com.playbowdogs.neighbors.data.api.PBApiService
import com.playbowdogs.neighbors.utils.*
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single(named("BASE_ANGEL_CAM_URL")) { ANGELCAM_API_URL }

    single(named("PLAYBOW_API_URL")) { PLAYBOW_API_URL }

    single(named("ACUITY_API_URL")) { ACUITY_API_URL }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }

    single(named("NoAuthClient")) {
        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(get<HttpLoggingInterceptor>())
        }
        okHttpClient.build()
    }

    single(named("AuthClient")) {
        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(get<HttpLoggingInterceptor>())
        }
        okHttpClient.addInterceptor(BasicAuthInterceptor(ACUITY_API_USERNAME, ACUITY_API_KEY))
        okHttpClient.build()
    }

    single {
        Moshi.Builder()
                .build()
    }

    single(named("AngelCamAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("BASE_ANGEL_CAM_URL")))
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .client(get<OkHttpClient>(named("NoAuthClient")))
                .build()
    }

    single(named("PlayBowAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("PLAYBOW_API_URL")))
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .client(get<OkHttpClient>(named("NoAuthClient")))
                .build()
    }

    single(named("AcuityAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("ACUITY_API_URL")))
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .client(get<OkHttpClient>(named("AuthClient")))
                .build()
    }

    single {
        get<Retrofit>(named("AngelCamAPI")).create(AngelCamApiService::class.java)
    }

    single {
        get<Retrofit>(named("PlayBowAPI")).create(PBApiService::class.java)
    }

    single {
        get<Retrofit>(named("AcuityAPI")).create(AcuityApiService::class.java)
    }
}
