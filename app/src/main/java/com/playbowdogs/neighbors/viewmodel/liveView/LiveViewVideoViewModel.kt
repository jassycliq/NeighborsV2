package com.playbowdogs.neighbors.viewmodel.liveView

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber


@ExperimentalCoroutinesApi
class LiveViewVideoViewModel(
    private val firebaseFunctionsRepository: FirebaseFunctionsRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {
    val videoURI by lazy { MutableLiveData<Uri>() }
    val videoState by lazy { MutableLiveData<Int>() }

    fun sendTestMessage(
        fcmToken: String?,
        androidChannel: String? = null,
    ) = scope.launch {
        val result = firebaseFunctionsRepository.testNotification(fcmToken, androidChannel)
        Timber.e(result.await().data.toString())
    }
}
