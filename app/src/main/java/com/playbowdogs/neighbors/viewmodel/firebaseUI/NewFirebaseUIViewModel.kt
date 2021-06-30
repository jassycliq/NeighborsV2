package com.playbowdogs.neighbors.viewmodel.firebaseUI

import android.content.SharedPreferences
import com.playbowdogs.neighbors.data.model.FirestoreUser
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.intent.FirebaseUIState
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.USER_TYPE_PREF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class NewFirebaseUIViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val firebaseAuthRepo: FirebaseAuthRepository,
    private val sharedPrefEditor: SharedPreferences.Editor,
    scope: CoroutineScope,
) : BaseViewModel(scope), ContainerHost<FirebaseUIState, Nothing> {

    override val container = container<FirebaseUIState, Nothing>(FirebaseUIState()) {
        scope.launch {
            getAuthUser()
        }
    }

    private suspend fun getAuthUser() = intent {
        firebaseAuthRepo.getCurrentUser().collect {
            when (it?.currentUser) {
                null -> {
                    Timber.e("Null user")
                    reduce { state.copy(goTo = "FirebaseUI") }
                }
                else -> {
                    Timber.e("going to usertype\n$it")
                    getFirestoreUserType()
                }
            }
        }
    }

    private suspend fun getFirestoreUserType() = intent {
        val userType = firestoreRepo.getUserType()
        Timber.e("Getting user Type\n${userType.toString()}")
        when (userType) {
            null -> {
                reduce { state.copy(goTo = "OnBoarding") }
            }
            else -> {
                sharedPrefEditor.putString(USER_TYPE_PREF, userType.user_type)
                    .apply()
                getFirestoreUser()
            }
        }
    }

    private suspend fun getFirestoreUser() = intent {
        val user: FirestoreUser = firestoreRepo.userDocument()?.get()?.await()?.toObject(FirestoreUser::class.java) ?: FirestoreUser()
        when (user.completed_onboarding) {
//            null -> reduce { state.copy(goTo = "OnBoarding") }
            false -> reduce { state.copy(goTo = "OnBoarding") }
            else -> reduce { state.copy(goTo = "BottomNav", firestoreUser = user) }
        }
    }

    fun navigateTo(location: String) = intent {
        reduce { state.copy(goTo = location) }
    }

    fun updateFCMToken(token: String?) = scope.launch {
        firestoreRepo.updateUserToken(token)
    }

    fun getAuthUIInstance() = firebaseAuthRepo.getAuthUIInstance()
}
