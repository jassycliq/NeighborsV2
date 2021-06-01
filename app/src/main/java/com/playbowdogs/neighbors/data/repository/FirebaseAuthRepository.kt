package com.playbowdogs.neighbors.data.repository

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class FirebaseAuthRepository {
    fun getAuthUIInstance() = AuthUI.getInstance()
    fun getUser() = Firebase.auth.currentUser

    fun getFirebase(): Firebase {
        return Firebase
    }

    fun getCurrentUser(): Flow<FirebaseAuth?> = callbackFlow {
        val firebaseAuthCallback = FirebaseAuth.AuthStateListener { firebaseAuth ->
            try {
                sendBlocking(firebaseAuth)
            } catch (e: Exception) {

            }
        }
        Firebase.auth.addAuthStateListener(firebaseAuthCallback)

        awaitClose { Firebase.auth.removeAuthStateListener(firebaseAuthCallback) }
    }

    fun getUserAfterSignIn(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    suspend fun tokenAwait(): InstanceIdResult? {
        FirebaseMessaging.getInstance().token
        return FirebaseInstanceId.getInstance().instanceId.await()
    }
}
