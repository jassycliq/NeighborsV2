package com.playbowdogs.neighbors.data.api

import com.playbowdogs.neighbors.data.model.AngelCamAccountCameras
import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips
import com.playbowdogs.neighbors.data.model.AngelCamRecordingInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AngelCamApiService {

    @GET("cameras/")
    suspend fun getCameraList(
        @Header(value = "Authorization") authorization: String,
    ): AngelCamAccountCameras

    @GET("cameras/{camera_id}/")
    suspend fun getCameraStatus(
        @Header(value = "Authorization") authorization: String,
        @Path(value = "camera_id", encoded = true) camera_id: String,
    ): AngelCamAccountCameras.Result

    @GET("cameras/{camera_id}/recording/")
    suspend fun getRecordingInfo(
        @Header(value = "Authorization") authorization: String,
        @Path(value = "camera_id", encoded = true) camera_id: String,
    ): AngelCamRecordingInfo

    @GET("cameras/{camera_id}/clips/{clip_id}/")
    suspend fun getRecordedClip(
        @Header(value = "Authorization") authorization: String,
        @Path(value = "camera_id", encoded = true) camera_id: String,
        @Path(value = "clip_id", encoded = true) clip_id: String,
    ): AngelCamRecordedClips

}
