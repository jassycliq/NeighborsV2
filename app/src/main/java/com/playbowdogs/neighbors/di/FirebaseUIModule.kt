package com.playbowdogs.neighbors.di

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.viewmodel.firebaseUI.FirebaseUIViewModel
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val firebaseUIModule: Module = module {
    viewModel {
        FirebaseUIViewModel(
            get<FirestoreRepository>(),
            get<FirebaseAuthRepository>(),
            get<CoroutineScope>(),
        )
    }

    viewModel {
        NewFirebaseUIViewModel(
            get<FirestoreRepository>(),
            get<FirebaseAuthRepository>(),
            get<SharedPreferences.Editor>(),
            get<CoroutineScope>(),
        )
    }
}
