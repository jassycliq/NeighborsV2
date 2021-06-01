package com.playbowdogs.neighbors.viewmodel.firebaseUI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseUser
import com.playbowdogs.neighbors.data.model.FirestoreUser
import com.playbowdogs.neighbors.data.model.FirestoreUserType
import com.playbowdogs.neighbors.data.model.UserType
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.intent.FirebaseUIState
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class FirebaseUIViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val firebaseAuthRepo: FirebaseAuthRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope), ContainerHost<FirebaseUIState, Nothing> {

    val userType by lazy { MutableLiveData<UserType?>(null) }

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

    fun getFirebaseUser() = liveData(Dispatchers.IO) {
        try {
            firebaseAuthRepo.getCurrentUser().collect { firebaseAuth ->
                firebaseAuth?.let {
                    emit(Resource.success(data = firebaseAuth.currentUser))
                    Timber.e("Firebase user: ${firebaseAuth.currentUser}")
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message.orEmpty()))
        }
    }

    fun getFirebaseCurrentUser(): FirebaseUser? {
        return firebaseAuthRepo.getUserAfterSignIn()
    }

//    suspend fun getFirestoreUser(): FirestoreUser? {
//        return firestoreRepo.getUserModel()
//    }

    val userFromDB = MutableLiveData<FirestoreUser?>()

    suspend fun getUserFromDB() {
        Timber.e("getUserFromDB called!")
        try {
            firestoreRepo.getUserFlow()?.collect {
                userFromDB.value = it
                Timber.e(it.toString())
            }
        } catch (exception: Exception) {
            userFromDB.value = null
            Timber.e(exception)
        }
    }

    fun getToken() = liveData(Dispatchers.IO) {
        try {
            emit(Resource.success(data = firebaseAuthRepo.tokenAwait()?.token))
        } catch (exception: KotlinNullPointerException) {
            emit(Resource.error(data = null, message = exception.message ?: "Unknown error"))
        }
    }

    fun updateFCMToken(token: String?) = scope.launch {
        firestoreRepo.updateUserToken(token)
    }

    fun getAuthUIInstance() = firebaseAuthRepo.getAuthUIInstance()

    override val container = container<FirebaseUIState, Nothing>(FirebaseUIState())

}