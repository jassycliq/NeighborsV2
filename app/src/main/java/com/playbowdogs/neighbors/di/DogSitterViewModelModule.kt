package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.viewmodel.DogSitterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val dogSitterViewModelModule: Module = module {
    viewModel {
        DogSitterViewModel(
            get<AngelCamRepository>(),
            get<AcuityRepository>(),
            get<FirestoreRepository>(),
            get<CoroutineScope>(),
        )
    }

}
