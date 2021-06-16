package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class OnGoingAppointment(
    @Json(name = "user_id")
    var user_id: String? = "",
    @Json(name = "user")
    var user: String? = "",
    @Json(name = "user_phone_number")
    var user_phone_number: String? = "",
    @Json(name = "user_profile_photo")
    var user_profile_photo: String? = "",
    @Json(name = "now_recording")
    var now_recording: Boolean? = false,
    @Json(name = "stream_links")
    var stream_links: List<StreamLink>? = null,
    @Json(name = "angelcam_id")
    var angelcam_id: String? = "",
    @Json(name = "angelcam_token")
    var angelcam_token: String? = "",
)
