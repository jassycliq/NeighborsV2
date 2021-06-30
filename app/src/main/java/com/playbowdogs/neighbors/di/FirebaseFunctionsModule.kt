package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val firebaseFunctionsModule: Module = module { factory { FirebaseFunctionsRepository() } }
