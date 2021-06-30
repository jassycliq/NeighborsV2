package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.api.AcuityApiService
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.utils.ACUITY_API_URL
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val acuityModule = module {

    single(named("ACUITY_API_URL")) { ACUITY_API_URL }

    single(named("AcuityAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("ACUITY_API_URL")))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get<OkHttpClient>(named("AuthClient")))
            .build()
    }

    single { get<Retrofit>(named("AcuityAPI")).create(AcuityApiService::class.java) }

    single { AcuityRepository(get<AcuityApiService>()) }
}
