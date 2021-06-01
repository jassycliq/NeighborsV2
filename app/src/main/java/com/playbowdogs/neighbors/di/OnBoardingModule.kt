package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.viewmodel.onboard.OnBoardingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val onBoardingModule = module {
    viewModel { OnBoardingViewModel(get(), get(), get(), get()) }
}
