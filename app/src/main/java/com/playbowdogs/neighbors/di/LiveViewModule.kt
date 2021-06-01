package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewVideoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val liveViewModule: Module = module {
    viewModel {
        LiveViewVideoViewModel(
            get<FirebaseFunctionsRepository>(),
            get<CoroutineScope>(),
        )
    }
}
