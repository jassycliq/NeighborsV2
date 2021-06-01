package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.api.AcuityApiService
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import org.koin.dsl.module

val acuityModule = module {
    factory { AcuityRepository(get<AcuityApiService>()) }
}
