package com.playbowdogs.neighbors.firebase.firestore

import androidx.lifecycle.liveData
import com.playbowdogs.neighbors.data.model.FirestoreMessage
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class FirestoreViewModel(
    scope: CoroutineScope,
    private val repo: FirestoreRepository,
    private val auth: FirebaseAuthRepository,
) : BaseViewModel(scope) {

    fun updateFCMToken(token: String?) = scope.launch {
        repo.updateUserToken(token)
    }

    val authInstance = auth.getAuthUIInstance()

    // save notifications to firebase
    fun saveNotificationToFirestore(firestoreMessageItem: FirestoreMessage) = scope.launch {
        repo.saveNotification(firestoreMessageItem)
    }

    val userType = liveData {
        try {
            emit(repo.getUserType())
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    // get realtime updates from firebase regarding saved notifications
    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    fun subscribeNotifications() = liveData {
        emit(Resource.loading(data = null))
        try {
            repo.getMessageListItemsFlow()?.collect {
                emit(Resource.success(data = it))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Unknown error"))
        }
    }
}
