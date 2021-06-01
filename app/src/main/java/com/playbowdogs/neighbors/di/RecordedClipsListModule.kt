package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.viewmodel.recordedClipsList.RecordedClipsListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val recordedClipsListModule: Module = module {
    viewModel {
        RecordedClipsListViewModel(
            get<AngelCamRepository>(),
            get<FirestoreRepository>(),
            get<FirebaseFunctionsRepository>(),
            get<CoroutineScope>(),
        )
    }
}
