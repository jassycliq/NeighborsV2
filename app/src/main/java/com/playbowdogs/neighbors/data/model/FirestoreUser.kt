package com.playbowdogs.neighbors.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class FirestoreUser(
    @Json(name = "user_id")
    var user_id: String = "",
    @Json(name = "display_name")
    var display_name: String = "",
    @Json(name = "profile_photo")
    var profile_photo: String = "",
    @Json(name = "email")
    var email: String = "",
    @Json(name = "phone_number")
    var phone_number: String = "",
    @Json(name = "address")
    var address: String = "",
    @Json(name = "city")
    var city: String = "",
    @Json(name = "zip_code")
    var zip_code: Int = 0,
    @Json(name = "is_active")
    var is_active: Boolean = false,
    @Json(name = "fcm_token")
    var fcm_token: String? = "",
    @Json(name = "completed_onboarding")
    var completed_onboarding: Boolean = false,
) : Serializable, Parcelable
