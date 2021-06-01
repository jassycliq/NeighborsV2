package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.utils.ActivityRetriever
import com.playbowdogs.neighbors.utils.DefaultCurrentActivityListener
import org.koin.dsl.module

val appModule = module {
    single { DefaultCurrentActivityListener() }
    single { ActivityRetriever(get()) }
}
