package com.playbowdogs.neighbors.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.playbowdogs.neighbors.data.model.AcuityAppointment
import com.playbowdogs.neighbors.data.model.AngelCamAccountCameras
import com.playbowdogs.neighbors.data.model.FirestoreUser
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber

@ExperimentalCoroutinesApi
class DogSitterViewModel(
    private val angelCamRepo: AngelCamRepository,
    private val acuityRepo: AcuityRepository,
    private val firestoreRepo: FirestoreRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {
    val customerUserForDogSitter by lazy { MutableLiveData<FirestoreUser>() }
    val cameraResultForDogSitter by lazy { MutableLiveData<AngelCamAccountCameras.Result>() }
    val isRecording by lazy { MutableLiveData(false) }

    private val currentDate = LocalDate.now()
    private val fullFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    private val fullDate = currentDate.format(fullFormatter)

    private val networkCallsLiveData = MediatorLiveData<List<AcuityAppointment>?>()

    private fun getDogSitterUserAsync() = scope.async(start = CoroutineStart.LAZY) {
        firestoreRepo.getUserModel()
    }

    private fun getCalendarListAsync() = scope.async(start = CoroutineStart.LAZY) {
        acuityRepo.getCalendars()
    }

    private fun getCustomerAcuityUser(
        email: String,
    ) = liveData {
        emit(firestoreRepo.getAcuityUser(email))
    }

    fun getAngelCamUser(
        uid: String?,
    ) = liveData {
        firestoreRepo.getAngelCamUserFlow(uid)?.collect {
            emit(it)
        }
    }

    // TODO: When multiple cam support is added, this will need to be adjusted
    private fun getCameraListAsync(
        token: String?,
    ) = scope.async(start = CoroutineStart.LAZY) {
        token?.let {
            return@async angelCamRepo.getCameraList(token).results.firstOrNull()
        }
    }

    private fun getDogSitterAppointmentsAsync(
        calendarID: Int,
    ) = scope.async(start = CoroutineStart.LAZY) {
        acuityRepo.getAppointmentsDogSitter(calendarID)
    }

    private fun getCustomerAcuityUserForDogSitterAsync(email: String?) =
        scope.async(start = CoroutineStart.LAZY) {
            val userId: String = firestoreRepo.getAcuityUser(email)?.user_id ?: "".also {
                val message = "Null Customer Acuity User"
                Timber.e(message)
                cancel(message)
            }

            return@async firestoreRepo.getCustomerUser(userId).firstOrNull()
        }

    fun saveDogSitterRecording(nowRecording: Boolean, customerId: String? = null) = scope.launch {
        firestoreRepo.saveDogSitterRecording(nowRecording, customerId)
    }

    init {
        /**
         * Dog Sitter Live Data networking
         */
        scope.launch {
            Timber.e("Starting network calls!")
            val dogSitterUserVal: FirestoreUser =
                getDogSitterUserAsync().await() ?: FirestoreUser().also {
                    val message = "Null firestore user"
                    Timber.e(message)
                    cancel(message)
                }
            val calendarList = getCalendarListAsync().await()
            val matchingCalendar = calendarList.single { calendar ->
                calendar.email == dogSitterUserVal.email
            }

            val dogSitterAppointments = getDogSitterAppointmentsAsync(matchingCalendar.id).await()

            networkCallsLiveData.value = dogSitterAppointments

            val appointmentToday = dogSitterAppointments.firstOrNull {
                it.date == fullDate
            }.also {
                if (it == null) {
                    customerUserForDogSitter.value = null
                    isRecording.value = false
                    Timber.e("No appointment today, no stream!")
                }
            } ?: AcuityAppointment().also {
                val message = "Null acuity appointments"
                Timber.e(message)
                cancel(message)
            }

            customerUserForDogSitter.value =
                getCustomerAcuityUserForDogSitterAsync(appointmentToday.email).await()

            firestoreRepo.getAngelCamUserFlow(firestoreRepo.getUserModel()?.user_id)?.collect {
                val cameraList = getCameraListAsync(it?.angelcam_token).await()
                cameraResultForDogSitter.value = cameraList
                isRecording.value = it?.now_recording
            }
        }
    }
}
