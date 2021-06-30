package com.playbowdogs.neighbors.viewmodel.recordedClipsList

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips
import com.playbowdogs.neighbors.data.repository.AngelCamRepository
import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.format.TextStyle
import timber.log.Timber
import java.util.*

@ExperimentalCoroutinesApi
class RecordedClipsListViewModel(
    private val repo: AngelCamRepository,
    private val firestoreRepo: FirestoreRepository,
    private val functionRepo: FirebaseFunctionsRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {
    private val job = Job()
    private var cloudFunctionCount = 0
    fun cancelJob() {
        job.cancel()
    }

    val chosenCamera: MutableLiveData<Resource<AngelCamRecordedClips>> by lazy { MutableLiveData<Resource<AngelCamRecordedClips>>() }
    val cameraUri: MutableLiveData<Uri> by lazy { MutableLiveData<Uri>() }

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    fun getTimeDiff(result: AngelCamRecordedClips) = runBlocking(Dispatchers.IO) {
        val diff = timeDiff(result) ?: return@runBlocking null
        return@runBlocking "Duration: $diff mins"
    }

    fun humanReadableDate(dateString: String?): CharSequence? = runBlocking(Dispatchers.IO) {
        if (dateString.isNullOrEmpty()) {
            return@runBlocking null
        } else {
            try {
                val dateTime = LocalDateTime.parse(dateString, formatter)
                return@runBlocking "${
                    dateTime.dayOfWeek.getDisplayName(
                        TextStyle.FULL,
                        Locale.US)
                }, ${dateTime.dayOfMonth} ${
                    dateTime.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.US
                    )
                } ${dateTime.year} at ${dateTime.hour}:${dateTime.minute}"
            } catch (e: DateTimeParseException) {
                Timber.e(e)
                return@runBlocking null
            }
        }
    }

    private fun timeDiff(result: AngelCamRecordedClips): Long? {
        return when (result.start.isEmpty() || result.end.isEmpty()) {
            true -> null
            false -> try {
                val start = LocalDateTime.parse(result.start, formatter)
                val end = LocalDateTime.parse(result.end, formatter)

                Duration.between(start, end).toMinutes()
            } catch (e: DateTimeParseException) {
                Timber.e(e)
                null
            }
        }
    }

    fun recordedClips() = liveData {
        try {
            firestoreRepo.getRecordedClips()?.collect {
                when (it.isNullOrEmpty()) {
                    true -> {
                        Timber.e("Recorded Clip is empty!")
                        emit(arrayListOf<AngelCamRecordedClips>())
                    }
                    false -> {
                        val newList = ArrayList<AngelCamRecordedClips>()
                        for (dbClip in it) {
                            val networkClip = getRecordedClipAsync(
                                cameraID = dbClip.camera_id,
                                clipID = dbClip.id
                            ).await()
                            val localClip = AngelCamRecordedClips(
                                id = networkClip.id,
                                name = networkClip.name,
                                status = networkClip.status,
                                camera_id = networkClip.camera_id,
                                customer_id = networkClip.customer_id,
                                sharing_token = networkClip.sharing_token,
                                start = networkClip.start,
                                end = networkClip.end,
                                created_at = networkClip.created_at,
                                download_url = networkClip.download_url,
                                thumbnail_url = dbClip.thumbnail_url,
                            )
                            Timber.e("dbclip: ${dbClip.download_url}\nnetwork clip: ${networkClip.download_url}")

                            if (dbClip.download_url.isNullOrEmpty()) {
                                cloudFunctionCount++
                                Timber.e("Cloud Function count: $cloudFunctionCount")
                                firestoreRepo.updateRecordedClipAsync(localClip)?.await()
                            }
                            newList.add(localClip)
                        }
                        emit(newList)
                    }
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            emit(arrayListOf<AngelCamRecordedClips>())
        }
    }

    private fun getRecordedClipAsync(
        cameraID: String,
        clipID: String,
    ) = scope.async {
        repo.getRecordedClip(
            authorization = "PersonalAccessToken afec708ac67fbccaa1a9b1c3ec3c31a34d740879",
            camera_id = cameraID,
            clip_id = clipID,
        )
    }
}
