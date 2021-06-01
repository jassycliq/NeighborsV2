package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class FirestoreAngelCamUser(
    @Json(name = "angelcam_id")
    var angelcam_id: String = "",
    @Json(name = "angelcam_token")
    var angelcam_token: String = "",
    @Json(name = "now_recording")
    var now_recording: Boolean = false,
    @Json(name = "user_id")
    var user_id: String = "",
)
