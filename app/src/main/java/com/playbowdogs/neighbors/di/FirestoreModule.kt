package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val firestoreModule = module { factory { FirestoreRepository() } }
