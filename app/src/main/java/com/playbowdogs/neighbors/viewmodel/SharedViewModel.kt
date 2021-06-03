package com.playbowdogs.neighbors.viewmodel

import android.content.SharedPreferences
import com.playbowdogs.neighbors.data.model.FirestoreAcuityUser
import com.playbowdogs.neighbors.data.model.FirestoreAngelCamUser
import com.playbowdogs.neighbors.data.model.FirestoreUser
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.data.repository.FirebaseAuthRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.intent.SharedEffect
import com.playbowdogs.neighbors.intent.SharedState
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.USER_UNIQUE_IDENTIFICATION
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SharedViewModel(
    private val angelCamRepo: AngelCamRepository,
    private val acuityRepo: AcuityRepository,
    private val firestoreRepo: FirestoreRepository,
    private val firebaseAuthRepo: FirebaseAuthRepository,
    private val sharedPrefEditor: SharedPreferences.Editor,
    scope: CoroutineScope,
) : BaseViewModel(scope), ContainerHost<SharedState, SharedEffect> {
    init {
        sharedPrefEditor
            .putString(firebaseAuthRepo.getUserAfterSignIn()?.uid, USER_UNIQUE_IDENTIFICATION)
            .apply()
    }

    private val job: Job = Job()
    val authUIInstance = firebaseAuthRepo.getAuthUIInstance()

//    val dogSitterUser: MutableLiveData<FirestoreUser> by lazy { MutableLiveData<FirestoreUser>() }
//    val cameraResultForCustomer: MutableLiveData<AngelCamAccountCameras.Result?> by lazy { MutableLiveData<AngelCamAccountCameras.Result?>() }
//    val isRecording by lazy { MutableLiveData(false) }

    private val currentDate = LocalDate.now()
    private val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val formattedDate = currentDate.format(formatter)

    private fun getTodayCustomerAppointmentAsync() =
        scope.async(context = scope.coroutineContext + job, start = CoroutineStart.LAZY) {
            firestoreRepo.getUserModel()?.email?.let {
                acuityRepo.getAppointments(it, formattedDate, formattedDate,)
            }
        }

    private fun getCalendarListAsync() =
        scope.async(context = scope.coroutineContext + job, start = CoroutineStart.LAZY) {
            acuityRepo.getCalendars()
        }

    private fun getDogSitterAcuityByEmailAsync(
        email: String?,
    ) = scope.async(context = scope.coroutineContext + job, start = CoroutineStart.LAZY) {
        email?.let {
            firestoreRepo.getAcuityUser(email)
        }
    }

    private fun getAngelCamUserFlow(
        uid: String?,
    ): Flow<FirestoreAngelCamUser?>? {
        return firestoreRepo.getAngelCamUserFlow(uid)
    }

    private fun getDogSitterUserAsync(
        uid: String?,
    ) = scope.async(context = scope.coroutineContext + job, start = CoroutineStart.LAZY) {
        uid?.let {
            firestoreRepo.getDogSitterUser(uid)
        }
    }

    private fun getDogSitterCamListAsync(
        token: String?,
    ) = scope.async(context = scope.coroutineContext + job, start = CoroutineStart.LAZY) {
        token?.let {
            angelCamRepo.getCameraList(token).results.firstOrNull()
        }
    }

    fun updateFCMToken(token: String?) = scope.launch {
        firestoreRepo.updateUserToken(token)
    }

    fun cancelJobs() {
        job.cancel()
    }

    override val container = container<SharedState, SharedEffect>(SharedState()) {
        if (it.user == null) {
            getData()
        }
    }

    private fun getData() = intent {
        scope.launch {
            val calendarList = getCalendarListAsync().await()
            val todayCustomerAppointment = getTodayCustomerAppointmentAsync().await() ?: emptyList()

            val matchedAppointments = calendarList.firstOrNull { calendar ->
                todayCustomerAppointment.any { it.calendarID == calendar.id }
            }.also {
                if (it == null) {
                    reduce {
                        state.copy(user = FirestoreUser())
                    }
                }
            }

            val dogSitterAcuityUser: FirestoreAcuityUser? =
                getDogSitterAcuityByEmailAsync(matchedAppointments?.email).await()
            getAngelCamUserFlow(dogSitterAcuityUser?.user_id)?.collect { dogSitterAngelCamUser ->
                getDogSitterUserAsync(dogSitterAngelCamUser?.user_id).await().let {
                    reduce {
                        state.copy(user = it)
                    }
                }
//                cameraResultForCustomer.value =
//                    getDogSitterCamListAsync(dogSitterAngelCamUser?.angelcam_token).await()
                dogSitterAngelCamUser?.now_recording?.let { isRecording ->
                    postSideEffect(SharedEffect.IsRecording(isRecording))
                }
            }
        }
    }
}
