package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AcuityCalendar(
    @Json(name = "id")
    var id: Int = 0,
    @Json(name = "name")
    var name: String = "",
    @Json(name = "email")
    var email: String = "",
    @Json(name = "replyTo")
    var replyTo: String = "",
    @Json(name = "description")
    var description: String = "",
    @Json(name = "location")
    var location: String = "",
    @Json(name = "timezone")
    var timezone: String = "",
)
