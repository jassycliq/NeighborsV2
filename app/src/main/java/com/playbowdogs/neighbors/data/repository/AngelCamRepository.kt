package com.playbowdogs.neighbors.data.repository

import com.playbowdogs.neighbors.data.api.AngelCamApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AngelCamRepository(private val angelCamApiService: AngelCamApiService) {

    suspend fun getCameraStatus(
        authorization: String,
        camera_id: String,
    ) = withContext(Dispatchers.IO) { angelCamApiService.getCameraStatus(authorization, camera_id) }

    suspend fun getRecordingInfo(
        authorization: String,
        camera_id: String,
    ) = withContext(Dispatchers.IO) {
        angelCamApiService.getRecordingInfo(authorization,
            camera_id)
    }

    suspend fun getCameraList(authorization: String) = withContext(Dispatchers.IO) {
        angelCamApiService.getCameraList(authorization)
    }

    suspend fun getRecordedClip(
        authorization: String,
        camera_id: String,
        clip_id: String,
    ) = withContext(Dispatchers.IO) {
        angelCamApiService.getRecordedClip(
            authorization,
            camera_id,
            clip_id,
        )
    }
}
