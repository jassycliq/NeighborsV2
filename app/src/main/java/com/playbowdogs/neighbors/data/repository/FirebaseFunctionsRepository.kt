package com.playbowdogs.neighbors.data.repository

import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

@ExperimentalCoroutinesApi
class FirebaseFunctionsRepository {
    private val functions = Firebase.functions

    suspend fun testNotification(
        fcmToken: String?,
        androidChannel: String? = null,
    ) = withContext(Dispatchers.IO) {
        val imageUrlString =
            "https://imgproxy.geocaching.com/29f2d5cd7e35051b4bc048191eb78dffebe04bf0?url=" +
                    "http%3A%2F%2Fwww.bassethoundsrunning.com%2Fwp-content%2Fuploads%2F2013%2F02%2Fbasset_hound_running_0084thumb.jpg"
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "fcmToken" to fcmToken,
            "androidChannel" to androidChannel,
            "imageUrlString" to imageUrlString,
        )

        functions
            .getHttpsCallable("sendNotification")
            .call(data)
    }

    suspend fun createThumbnail(
        downloadUrl: String?,
        clipUID: String?,
        userType: String?,
        city: String?,
    ): HttpsCallableResult? = withContext(Dispatchers.IO) {

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "downloadUrl" to downloadUrl,
            "clipUID" to clipUID,
            "userType" to userType,
            "city" to city,
        )

        if (data.containsValue(null)) {
            return@withContext null
        }

        val response = functions
            .getHttpsCallable("createThumbnail")
            .call(data)
            .await()
        Timber.e(response.data.toString())
        response
    }

    suspend fun getCalendar(): HttpsCallableResult? = withContext(Dispatchers.IO) {
        functions
            .getHttpsCallable("acuityCalendarUI")
            .call()
            .await()
            .also {
                Timber.e(it.data.toString())
            }
    }
}
