package com.playbowdogs.neighbors.di

import android.app.Application
import android.content.SharedPreferences
import com.playbowdogs.neighbors.utils.SHARED_PREFERENCES
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedPrefModule: Module = module {

    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences(SHARED_PREFERENCES, android.content.Context.MODE_PRIVATE)
}
