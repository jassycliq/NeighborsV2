package com.playbowdogs.neighbors.viewmodel.onboard

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.playbowdogs.neighbors.data.model.AcuityClient
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.Resource
import com.playbowdogs.neighbors.viewmodel.onboard.ClientResult.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class OnBoardingViewModel(
    private val repo: AcuityRepository,
    private val firebaseAuthRepo: FirebaseAuthRepository,
    private val firestoreRepo: FirestoreRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {

    val currentUser = MutableLiveData<FirebaseUser?>()

    // save user to firebase
    fun saveUserToFirestore(
        userID: String?,
        displayName: String?,
        profilePhoto: String?,
        email: String?,
        phoneNumber: String?,
        address: String?,
        city: String?,
        zipCode: Int?,
        isActive: Boolean,
        fcmToken: String?,
        completedOnBoarding: Boolean,
    ) = scope.launch {
        firestoreRepo.saveUserItem(
            userID = userID.orEmpty(),
            displayName = displayName.orEmpty(),
            profilePhoto = profilePhoto.orEmpty(),
            email = email.orEmpty(),
            phoneNumber = phoneNumber.orEmpty(),
            address = address.orEmpty(),
            city = city.orEmpty(),
            zipCode = zipCode ?: 0,
            isActive = isActive,
            fcmToken = fcmToken.orEmpty(),
            completedOnBoarding = completedOnBoarding,
        )
    }

    fun saveAcuityUserToFirestore(
        userEmail: String?,
    ) = scope.launch {
        firestoreRepo.saveAcuityUser(userEmail)
    }

    val fullName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>(adjustPhoneNumber(Firebase.auth.currentUser?.phoneNumber))

    val client: MutableLiveData<Resource<List<AcuityClient>>> by lazy { MutableLiveData<Resource<List<AcuityClient>>>() }

    fun getCurrentUser() = scope.launch {
        currentUser.value = Firebase.auth.currentUser
    }

    fun getToken() = liveData {
        try {
            emit(Resource.success(data = firebaseAuthRepo.tokenAwait()?.token))
        } catch (exception: KotlinNullPointerException) {
            emit(Resource.error(data = null, message = exception.message ?: "Unknown error"))
        }
    }

    val emailCheck = object : MutableLiveData<Boolean?>(null) {
        override fun onActive() {
            value?.let { return }

            scope.launch {
                var job: Deferred<Unit>? = null

                email.asFlow()
                    .debounce(DEBOUNCE_TIME)
                    .distinctUntilChanged()
                    .collect {
                        job?.cancel()
                        job = async(Dispatchers.Main) {
                            value = isEmailValid(email.value.toString())
                        }
                    }
            }
        }
    }

    val fullNameCheck = object : MutableLiveData<Boolean?>(null) {
        override fun onActive() {
            value?.let { return }

            scope.launch {
                var job: Deferred<Unit>? = null

                fullName.asFlow()
                    .debounce(DEBOUNCE_TIME)
                    .distinctUntilChanged()
                    .collect {
                        job?.cancel()
                        job = async(Dispatchers.Main) {
                            value = isFullNameValid(fullName.value.toString())
                        }
                    }
            }
        }
    }

    val phoneNumberCheck = object : MutableLiveData<Boolean>() {
        override fun onActive() {
            value?.let { return }

            scope.launch {
                var job: Deferred<Unit>? = null

                phoneNumber.asFlow()
                    .debounce(DEBOUNCE_TIME)
                    .distinctUntilChanged()
                    .collect {
                        job?.cancel()
                        job = async(Dispatchers.Main) {
                            value = isPhoneNumberValid(phoneNumber.value.toString())
                        }
                    }
            }
        }
    }

    val formChecker =
        fullNameCheck.asFlow().combine(emailCheck.asFlow()) { fullNameCheck, emailCheck ->
            Timber.e("In form check\nfullname is: $fullNameCheck\nemail is: $emailCheck")
            return@combine when {
                fullNameCheck == null || emailCheck == null -> null
                !fullNameCheck || !emailCheck -> false
                fullNameCheck && emailCheck -> true
                else -> false
            }
        }.asLiveData()


    fun getClient() = scope.launch {
        client.value = Resource.loading(data = null)
        try {
            client.value =
                Resource.success(data = repo.getClient(oldAdjust(phoneNumber.value)))
        } catch (exception: Exception) {
            client.value = Resource.error(data = null, message = exception.message.orEmpty())
        }
    }

    fun validateClient(acuityClient: AcuityClient?): ClientResult {
        return when {
            acuityClient == null -> {
                PHONE_NUMBER_ERROR
            }
            acuityClient.firstName + " " + acuityClient.lastName != fullName.value -> {
                FULL_NAME_ERROR
            }
            acuityClient.email != email.value -> {
                EMAIL_ERROR
            }
            else -> {
                NO_ERROR
            }
        }
    }

    private fun isFullNameValid(fullName: String): Boolean? {
        return when {
            fullName.isBlank() -> {
                null
            }
            fullName.length > 3 -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private fun isEmailValid(email: String): Boolean? {
        return when {
            email.isBlank() -> {
                null
            }
            email.contains("@") -> {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
            else -> {
                false
            }
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean? {
        return when {
            phoneNumber.isBlank() -> {
                null
            }
            phoneNumber.length == 12 -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private fun adjustPhoneNumber(phoneNumber: String?): String {
        return when (phoneNumber != null) {
            true -> StringBuilder(phoneNumber).insert(5, "-").insert(9, "-").toString().run {
                replace("+1", "")
            }
            false -> ""
        }
    }

    private fun oldAdjust(phoneNumber: String?): String {
        return phoneNumber?.replace("-", "") ?: ""
    }

    companion object {
        const val DEBOUNCE_TIME: Long = 500
    }
}

enum class ClientResult {
    FULL_NAME_ERROR,
    EMAIL_ERROR,
    PHONE_NUMBER_ERROR,
    NO_ERROR
}
