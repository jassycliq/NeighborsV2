package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AngelCamRecordedClips(
    @Json(name = "id")
    var id: String = "",
    @Json(name = "name")
    var name: String = "",
    @Json(name = "status")
    var status: String = "",
    @Json(name = "sharing_token")
    var sharing_token: String = "",
    @Json(name = "start")
    var start: String = "",
    @Json(name = "end")
    var end: String = "",
    @Json(name = "created_at")
    var created_at: String = "",
    @Json(name = "stream")
    var stream: Stream? = Stream(),
    @Json(name = "download_url")
    var download_url: String? = "",
    @Json(name = "camera_id")
    var camera_id: String = "",
    @Json(name = "customer_id")
    var customer_id: String = "",
    @Json(name = "thumbnail_url")
    var thumbnail_url: String? = "",
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class Stream(
        @Json(name = "format")
        var format: String = "",
        @Json(name = "url")
        var url: String = "",
    )
}

