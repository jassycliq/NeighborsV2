package com.playbowdogs.neighbors.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.playbowdogs.neighbors.data.model.*
import com.playbowdogs.neighbors.utils.getDataFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@ExperimentalCoroutinesApi
class FirestoreRepository : KoinComponent {
    private val firebaseAuth: FirebaseAuthRepository by inject()

    private val firestoreDB = FirebaseFirestore.getInstance()

    private val user = firebaseAuth.getUserAfterSignIn()

    private fun getUserTypeDocument(): Task<DocumentSnapshot>? {
        return firebaseAuth.getUserAfterSignIn()?.let {
            firestoreDB.collection("UserTypes")
                .document(it.uid)
                .get()
        }
    }

    suspend fun saveUserType(
        uid: String?,
        userType: FirestoreUserType,
    ) = withContext(Dispatchers.IO) {
        uid?.let {
            firestoreDB
                .collection("UserTypes")
                .document(uid)
                .set(userType)
        }
    }

    suspend fun getUserType(): FirestoreUserType? = withContext(Dispatchers.IO) {
        firebaseAuth.getUserAfterSignIn()?.let {
            firestoreDB
                .collection("UserTypes")
                .document(it.uid)
                .get()
                .await()
                .toObject(FirestoreUserType::class.java)
        }
    }

    suspend fun userDocument(): DocumentReference? = withContext(Dispatchers.IO) {
        val userTypeJob =
            async { getUserTypeDocument()?.await()?.toObject(FirestoreUserType::class.java) }
        val userType = userTypeJob.await() ?: FirestoreUserType("", "", "")

        when {
            firebaseAuth.getUserAfterSignIn() != null -> {
                firestoreDB.collection("Users")
                    .document(userType.user_type)
                    .collection(userType.city)
                    .document(firebaseAuth.getUserAfterSignIn()!!.uid)
            }
            else -> null
        }
    }

    private fun getDogSitterDocument(uid: String): DocumentReference {
        return firestoreDB.collection("Users")
            .document("Dog Sitter")
            .collection("San Francisco")
            .document(uid)
    }

    private fun getCustomerDocument(uid: String): DocumentReference {
        return firestoreDB.collection("Users")
            .document("Customer")
            .collection("San Francisco")
            .document(uid)
    }

    private fun angelCamDocument(user_id: String): DocumentReference? {
        return when {
            firebaseAuth.getUserAfterSignIn() != null -> {
                firestoreDB.collection("AngelCamUsers")
                    .document(user_id)
            }
            else -> null
        }
    }

    private fun acuityDocument(userEmail: String): DocumentReference? {
        return when {
            firebaseAuth.getUserAfterSignIn() != null -> {
                firestoreDB.collection("AcuityUsers")
                    .document(userEmail)
            }
            else -> null
        }
    }

    suspend fun saveAcuityUser(
        userEmail: String?,
    ) = withContext(Dispatchers.IO) {
        userEmail?.let {
            val acuityUser = FirestoreAcuityUser(firebaseAuth.getUserAfterSignIn()!!.uid)

            firestoreDB
                .collection("AcuityUsers")
                .document(userEmail)
                .set(acuityUser)
        }
    }

    // save user to firebase
    suspend fun saveUserItem(
        userID: String,
        displayName: String,
        profilePhoto: String,
        email: String,
        phoneNumber: String,
        address: String,
        city: String,
        zipCode: Int,
        isActive: Boolean,
        fcmToken: String,
        completedOnBoarding: Boolean,
    ) = withContext(Dispatchers.IO) {
        val userItem = FirestoreUser(
            user_id = userID,
            display_name = displayName,
            profile_photo = profilePhoto,
            email = email,
            phone_number = phoneNumber,
            address = address,
            city = city,
            zip_code = zipCode,
            is_active = isActive,
            fcm_token = fcmToken,
            completed_onboarding = completedOnBoarding,
        )
        userDocument()?.set(userItem)
    }

    suspend fun checkIfUserExists() {
        try {
            userDocument()?.get()?.await()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    suspend fun updateUserToken(token: String?) {
        try {
            userDocument()?.update("fcm_token", token)?.await()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    // save address to firebase
    suspend fun saveNotification(firestoreMessageItem: FirestoreMessage) {
        try {
            userDocument()?.collection("saved_notifications")
                ?.document(firestoreMessageItem.timestamp)
                ?.set(firestoreMessageItem)
                ?.await()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getMessageListItemsFlow(): Flow<List<FirestoreMessage?>>? {
        return userDocument()?.collection("saved_notifications")?.getDataFlow { querySnapshot ->
            querySnapshot?.documents?.map {
                getMessageListItemFromSnapshot(it)
            } ?: listOf()
        }
    }

    // Parses the document snapshot to the desired object
    private fun getMessageListItemFromSnapshot(documentSnapshot: DocumentSnapshot): FirestoreMessage? {
        return documentSnapshot.toObject(FirestoreMessage::class.java)
    }

    @ExperimentalCoroutinesApi
    suspend fun getUserFlow(): Flow<FirestoreUser?>? {
        return userDocument()?.getDataFlow { documentSnapshot ->
            documentSnapshot?.toObject(FirestoreUser::class.java)
        }
    }

    suspend fun getUserModel(): FirestoreUser? {
        return userDocument()?.get()?.await().let {
            it?.toObject(FirestoreUser::class.java)
        }
    }

    suspend fun getAngelCamUser(user_id: String?): FirestoreAngelCamUser? =
        withContext(Dispatchers.IO) {
            user_id?.let {
                angelCamDocument(it)?.get()?.await()?.toObject(FirestoreAngelCamUser::class.java)
            }
        }

    suspend fun saveDogSitterRecording(
        nowRecording: Boolean,
        customerId: String?,
    ) = withContext(Dispatchers.IO) {
        firebaseAuth.getUserAfterSignIn()?.let {
            angelCamDocument(it.uid)
                ?.update("now_recording", nowRecording)
        }
        customerId?.let { nonNullCustomerId ->
            firebaseAuth.getUserAfterSignIn()?.let {
                angelCamDocument(it.uid)
                    ?.update("customer_id", nonNullCustomerId)
            }
        }
    }

    suspend fun getAcuityUser(email: String?): FirestoreAcuityUser? =
        withContext(Dispatchers.IO) {
            email?.let {
                acuityDocument(it)?.get()?.await()?.toObject(FirestoreAcuityUser::class.java)
            }
        }

    fun getAngelCamUserFlow(userId: String?): Flow<FirestoreAngelCamUser?>? {
        return userId?.let {
            angelCamDocument(it)?.getDataFlow { documentSnapshot ->
                documentSnapshot.let { snapshot ->
                    snapshot?.toObject(FirestoreAngelCamUser::class.java)
                }
            }
        }
    }

    fun getDogSitterUserFlow(uid: String): Flow<FirestoreUser?> {
        return getDogSitterDocument(uid).getDataFlow { documentSnapshot ->
            documentSnapshot?.toObject(FirestoreUser::class.java)
        }
    }

    suspend fun getDogSitterUser(uid: String): FirestoreUser? {
        return getDogSitterDocument(uid).get().await()?.toObject(FirestoreUser::class.java)
    }

    fun getCustomerUser(uid: String): Flow<FirestoreUser?> {
        return getCustomerDocument(uid).getDataFlow { documentSnapshot ->
            documentSnapshot?.toObject(FirestoreUser::class.java)
        }
    }

    suspend fun getRecordedClips(): Flow<MutableList<AngelCamRecordedClips>?>? {
        return userDocument()?.collection("RecordedClips")?.getDataFlow {
            it?.let {
                getRecordedClipsListItemFromSnapshot(it)
            }
        }
    }

    // Parses the document snapshot to the desired object
    private fun getRecordedClipsListItemFromSnapshot(documentSnapshot: QuerySnapshot): MutableList<AngelCamRecordedClips> {
        return documentSnapshot.toObjects(AngelCamRecordedClips::class.java)
    }

    suspend fun updateRecordedClipAsync(recordedClip: AngelCamRecordedClips): Deferred<Void>? {
        return if (recordedClip.thumbnail_url.isNullOrEmpty()) {
            userDocument()
                ?.collection("RecordedClips")
                ?.document(recordedClip.id)
                ?.update(
                    "download_url", recordedClip.download_url
                )?.asDeferred()
        } else {
            null
        }
    }

    @ExperimentalCoroutinesApi
    fun getAppointmentsFlow(): Flow<List<AcuityAppointment?>>? {
        return user?.let {
            firestoreDB.collection("AcuityAppointments").document(it.uid).collection("Appointments").getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map { snapshot ->
                    getAppointmentListItemFromSnapshot(snapshot)
                } ?: listOf()
            }
        }
    }

    // Parses the document snapshot to the desired object
    private fun getAppointmentListItemFromSnapshot(documentSnapshot: DocumentSnapshot): AcuityAppointment? {
        return documentSnapshot.toObject(AcuityAppointment::class.java)
    }

    @ExperimentalCoroutinesApi
    fun getLiveView(): Flow<OnGoingAppointment?>? {
        return user?.let {
            firestoreDB.collection("OngoingAppointments").document(it.uid).getDataFlow { documentSnapshot ->
                documentSnapshot?.toObject(OnGoingAppointment::class.java)
            }
        }
    }
}
