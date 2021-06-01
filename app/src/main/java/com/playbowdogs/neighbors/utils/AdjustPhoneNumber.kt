package com.playbowdogs.neighbors.utils

fun adjustPhoneNumber(phoneNumber: String): String {
    return if (phoneNumber.length > 9) {
        val newPhone = StringBuilder(phoneNumber).insert(5, "-").insert(9, "-").toString()
        newPhone.replace("+1", "")
    } else {
        ""
    }
}