package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.api.PBApiService
import com.playbowdogs.neighbors.utils.PLAYBOW_API_URL
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val playbowModule = module {

    single(named("PLAYBOW_API_URL")) { PLAYBOW_API_URL }

    single(named("PlayBowAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("PLAYBOW_API_URL")))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get<OkHttpClient>(named("NoAuthClient")))
            .build()
    }

    single { get<Retrofit>(named("PlayBowAPI")).create(PBApiService::class.java) }
}