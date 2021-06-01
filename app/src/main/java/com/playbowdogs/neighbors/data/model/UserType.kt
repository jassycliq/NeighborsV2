package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserType(
    @Json(name = "type")
    var type: String,
    @Json(name = "description")
    var description: String,
    @Json(name = "drawableRes")
    var drawableRes: Int,
    @Json(name = "backgroundColor")
    var backgroundColor: Int,
)
