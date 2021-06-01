package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.viewmodel.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val notificationModule: Module = module {
    viewModel { NotificationsViewModel() }
}
