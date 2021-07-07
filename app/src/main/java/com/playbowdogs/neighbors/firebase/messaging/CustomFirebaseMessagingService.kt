package com.playbowdogs.neighbors.firebase.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import timber.log.Timber

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class CustomFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationHelper = FirebaseNotificationHelper(this)
    private val firestoreRepository: FirestoreRepository by inject()
    private val firebaseAuthRepo: FirebaseAuthRepository by inject()
    private val scope: CoroutineScope by inject()

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.e("Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //
        // Also make sure that user exist before saving token
        if (firebaseAuthRepo.getUserAfterSignIn() != null) {
            scope.launch {
                firestoreRepository.updateUserToken(token)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.e("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.e("Message data payload: ${remoteMessage.data}")

            // Use [FirebasePayload] class to handle payload logic
            // TODO: Implement payload logic
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.e("Message Notification Body: ${it.body}")
            Timber.e("Message Channel: ${it.channelId}")

            // Use [FirebaseNotificationHelper] class to handle notification logic
            notificationHelper.onNotificationReceived(it)
        }
    }
}
