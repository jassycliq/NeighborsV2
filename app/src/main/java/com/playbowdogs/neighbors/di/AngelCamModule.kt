package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.api.AngelCamApiService
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.utils.ANGELCAM_API_URL
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val angelCamModule: Module = module {

    single(named("BASE_ANGEL_CAM_URL")) { ANGELCAM_API_URL }

    single(named("AngelCamAPI")) {
        Retrofit.Builder().baseUrl(get<String>(named("BASE_ANGEL_CAM_URL")))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get<OkHttpClient>(named("NoAuthClient")))
            .build()
    }

    single { get<Retrofit>(named("AngelCamAPI")).create(AngelCamApiService::class.java) }

    single { AngelCamRepository(get<AngelCamApiService>()) }
}
