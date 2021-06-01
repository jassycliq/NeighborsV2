package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AngelCamAccountCameras(
    @Json(name = "count")
    var count: Int = 0,
    @Json(name = "next")
    var next: Any? = null,
    @Json(name = "previous")
    var previous: Any? = null,
    @Json(name = "results")
    var results: List<Result> = listOf(),
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "id")
        var id: Int = 0,
        @Json(name = "snapshot")
        var snapshot: Snapshot = Snapshot(),
        @Json(name = "status")
        var status: String = "",
        @Json(name = "live_snapshot")
        var liveSnapshot: String = "",
        @Json(name = "streams")
        var streams: List<Stream> = listOf(),
        @Json(name = "applications")
        var applications: List<Application> = listOf(),
        @Json(name = "audio_enabled")
        var audioEnabled: Boolean = false,
        @Json(name = "name")
        var name: String = "",
        @Json(name = "type")
        var type: String = "",
        @Json(name = "connection_type")
        var connectionType: String = "",
        @Json(name = "site")
        var site: Int = 0,
    ) {
        @Keep
        @JsonClass(generateAdapter = true)
        data class Snapshot(
            @Json(name = "url")
            var url: String = "",
            @Json(name = "created_at")
            var createdAt: String = "",
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Stream(
            @Json(name = "format")
            var format: String = "",
            @Json(name = "url")
            var url: String = "",
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Application(
            @Json(name = "code")
            var code: String = "",
        )
    }
}
