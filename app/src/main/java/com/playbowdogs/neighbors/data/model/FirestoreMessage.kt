package com.playbowdogs.neighbors.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class FirestoreMessage(
        @Json(name = "receiver_uid")
        var receiver_uid: String = "",
        @Json(name = "receiver_display_name")
        var receiver_display_name: String = "",
        @Json(name = "receiver_profile_image")
        var receiver_profile_image: String = "",
        @Json(name = "sender_uid")
        var sender_uid: String = "",
        @Json(name = "sender_display_name")
        var sender_display_name: String = "",
        @Json(name = "sender_profile_image")
        var sender_profile_image: String = "",
        @Json(name = "message_channel")
        var message_channel: String = "",
        @Json(name = "timestamp")
        var timestamp: String = "",
        @Json(name = "title")
        var title: String = "",
        @Json(name = "body")
        var body: String = "",
        @Json(name = "image")
        var image: String = "",
        @Json(name = "is_read")
        var is_read: Boolean = false,
)
