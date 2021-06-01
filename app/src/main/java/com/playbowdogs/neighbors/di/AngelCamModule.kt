package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.api.AngelCamApiService
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val angelCamModule: Module = module {
    factory { AngelCamRepository(get<AngelCamApiService>()) }
}
