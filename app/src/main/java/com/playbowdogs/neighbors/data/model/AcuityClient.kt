package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AcuityClient(
        @Json(name = "firstName")
        var firstName: String = "",
        @Json(name = "lastName")
        var lastName: String = "",
        @Json(name = "email")
        var email: String = "",
        @Json(name = "phone")
        var phone: String = "",
        @Json(name = "notes")
        var notes: String = "",
)
