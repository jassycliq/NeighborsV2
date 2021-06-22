package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Stream(
    @Json(name = "format")
    var format: String? = "",
    @Json(name = "url")
    var url: String? = "",
    @Json(name = "refresh_rate")
    var refreshRate: Int = 0,
)