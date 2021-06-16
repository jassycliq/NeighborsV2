package com.playbowdogs.neighbors.di

import android.content.SharedPreferences
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.firebase.firestore.FirestoreViewModel
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val firestoreModule = module {
    factory { FirestoreRepository() }
    viewModel {
        NewFirebaseUIViewModel(get<FirestoreRepository>(), get<FirebaseAuthRepository>(), get<SharedPreferences.Editor>(), get<CoroutineScope>(),)
    }
}
