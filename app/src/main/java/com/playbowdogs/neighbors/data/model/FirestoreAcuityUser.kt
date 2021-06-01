package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class FirestoreAcuityUser(
    @Json(name = "user_id")
    var user_id: String = "",
    @Json(name = "email")
    var email: String = "",
)
