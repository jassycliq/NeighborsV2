package com.playbowdogs.neighbors.data.repository

import com.playbowdogs.neighbors.data.api.AcuityApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AcuityRepository(private val acuityApiService: AcuityApiService) {

    suspend fun getClient(phoneNumber: String) =
        withContext(Dispatchers.IO) { acuityApiService.getClient(phoneNumber) }

    suspend fun getAppointments(
        email: String,
    ) = withContext(Dispatchers.IO) {
        acuityApiService.getAppointments(email)
    }

    suspend fun getAppointmentsPhone(
        phone: String,
    ) = withContext(Dispatchers.IO) {
        acuityApiService.getAppointmentsPhone(phone)
    }

    suspend fun getAppointments(
        email: String,
        minDate: String,
        maxDate: String,
    ) = withContext(Dispatchers.IO) {
        acuityApiService.getAppointments(email, minDate, maxDate)
    }

    suspend fun getAppointmentsDogSitter(
        calendarID: Int,
    ) = withContext(Dispatchers.IO) {
        acuityApiService.getAppointmentsDogSitter(calendarID)
    }

    suspend fun getAppointmentsDogSitter(
        calendarID: Int,
        minDate: String,
        maxDate: String,
    ) = withContext(Dispatchers.IO) {
        acuityApiService.getAppointmentsDogSitter(calendarID, minDate, maxDate)
    }

    suspend fun getCalendars() = withContext(Dispatchers.IO) { acuityApiService.getCalendars() }
}
