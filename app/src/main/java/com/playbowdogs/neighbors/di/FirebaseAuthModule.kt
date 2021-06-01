package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val firebaseAuthModule: Module = module {
    single { FirebaseAuthRepository() }
}
