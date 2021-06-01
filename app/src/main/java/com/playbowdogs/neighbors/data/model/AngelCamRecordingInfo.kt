package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AngelCamRecordingInfo(
        @Json(name = "status")
        var status: String = "",
        @Json(name = "retention")
        var retention: String = "",
        @Json(name = "active_service_id")
        var activeServiceId: Int? = 0,
        @Json(name = "deactivated_at")
        var deactivatedAt: Any? = null,
)
