package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.firebase.firestore.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val settingsModule = module {
    viewModel {
        SettingsViewModel(
            get<CoroutineScope>(),
            get<FirestoreRepository>(),
            get<FirebaseAuthRepository>(),
        )
    }
}
