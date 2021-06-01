package com.playbowdogs.neighbors.viewmodel.firebaseUI

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.playbowdogs.neighbors.data.model.FirestoreUser
import com.playbowdogs.neighbors.data.model.FirestoreUserType
import com.playbowdogs.neighbors.data.model.UserType
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.intent.FirebaseUIState
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.USER_TYPE_PREF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@ExperimentalCoroutinesApi
class NewFirebaseUIViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val firebaseAuthRepo: FirebaseAuthRepository,
    private val sharedPrefEditor: SharedPreferences.Editor,
    scope: CoroutineScope,
) : BaseViewModel(scope), ContainerHost<FirebaseUIState, Nothing> {

    val nextButton by lazy { MutableLiveData<Boolean?>(false) }
    val submitButton by lazy { MutableLiveData<Boolean?>(false) }

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

    fun setSubmitButton(isEnabled: Boolean?) {
        submitButton.value = isEnabled
    }

    fun setNextButton(isEnabled: Boolean?) {
        nextButton.value = isEnabled
    }

    fun setButtons() {

    }

    fun signOut(context: Context?) = context?.let { AuthUI.getInstance().signOut(it) }

    fun saveUserType(
        userType: UserType,
    ) = scope.launch {

        val uid = firebaseAuthRepo.getUserAfterSignIn()?.uid
        val type = FirestoreUserType(uid, userType.type, "San Francisco")

        try {
            firestoreRepo.saveUserType(uid, type)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    fun updateFCMToken(token: String?) = scope.launch {
        firestoreRepo.updateUserToken(token)
    }

    fun getAuthUIInstance() = firebaseAuthRepo.getAuthUIInstance()
}