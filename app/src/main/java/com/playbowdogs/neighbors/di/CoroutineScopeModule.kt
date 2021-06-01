package com.playbowdogs.neighbors.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.module.Module
import org.koin.dsl.module

val coroutineScopeModule: Module = module {
    factory { CoroutineScope(Job() + Dispatchers.Main) }
}
