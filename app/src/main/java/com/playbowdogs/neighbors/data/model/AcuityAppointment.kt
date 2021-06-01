package com.playbowdogs.neighbors.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AcuityAppointment(
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "firstName")
    var firstName: String? = "",
    @Json(name = "lastName")
    var lastName: String? = "",
    @Json(name = "phone")
    var phone: String? = "",
    @Json(name = "email")
    var email: String? = "",
    @Json(name = "date")
    var date: String? = "",
    @Json(name = "time")
    var time: String? = "",
    @Json(name = "endTime")
    var endTime: String? = "",
    @Json(name = "dateCreated")
    var dateCreated: String? = "",
    @Json(name = "datetime")
    var datetime: String? = "",
    @Json(name = "price")
    var price: String? = "",
    @Json(name = "paid")
    var paid: String? = "",
    @Json(name = "amountPaid")
    var amountPaid: String? = "",
    @Json(name = "type")
    var type: String? = "",
    @Json(name = "appointmentTypeID")
    var appointmentTypeID: Int? = 0,
    @Json(name = "addonIDs")
    var addonIDs: List<Any>? = listOf(),
    @Json(name = "classID")
    var classID: Int? = 0,
    @Json(name = "duration")
    var duration: String? = "",
    @Json(name = "calendar")
    var calendar: String? = "",
    @Json(name = "calendarID")
    var calendarID: Int? = 0,
    @Json(name = "canClientCancel")
    var canClientCancel: Boolean? = false,
    @Json(name = "canClientReschedule")
    var canClientReschedule: Boolean? = false,
    @Json(name = "location")
    var location: String? = "",
    @Json(name = "certificate")
    var certificate: Any? = null,
    @Json(name = "confirmationPage")
    var confirmationPage: String? = "",
    @Json(name = "formsText")
    var formsText: String? = "",
    @Json(name = "notes")
    var notes: String? = "",
    @Json(name = "timezone")
    var timezone: String? = "",
    @Json(name = "forms")
    var forms: List<Any>? = listOf(),
    @Json(name = "labels")
    var labels: List<Any>? = listOf(),
)
