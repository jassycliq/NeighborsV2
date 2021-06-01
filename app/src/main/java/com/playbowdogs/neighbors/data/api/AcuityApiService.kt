package com.playbowdogs.neighbors.data.api

import com.playbowdogs.neighbors.data.model.AcuityAppointment
import com.playbowdogs.neighbors.data.model.AcuityCalendar
import com.playbowdogs.neighbors.data.model.AcuityClient
import retrofit2.http.GET
import retrofit2.http.Query

interface AcuityApiService {

    @GET("clients")
    suspend fun getClient(
        @Query(value = "search") phone_number: String,
    ): List<AcuityClient>

    @GET("appointments")
    suspend fun getAppointments(
        @Query(value = "email") email: String,
    ): List<AcuityAppointment>

    @GET("appointments")
    suspend fun getAppointmentsPhone(
        @Query(value = "phone") phone: String,
    ): List<AcuityAppointment>

    @GET("appointments")
    suspend fun getAppointments(
        @Query(value = "email") email: String,
        @Query(value = "minDate") minDate: String,
        @Query(value = "maxDate") maxDate: String,
    ): List<AcuityAppointment>

    @GET("appointments")
    suspend fun getAppointmentsDogSitter(
        @Query(value = "calendarID") calendarID: Int,
    ): List<AcuityAppointment>

    @GET("appointments")
    suspend fun getAppointmentsDogSitter(
        @Query(value = "calendarID") calendarID: Int,
        @Query(value = "minDate") minDate: String,
        @Query(value = "maxDate") maxDate: String,
    ): List<AcuityAppointment>

    @GET("calendars")
    suspend fun getCalendars(): List<AcuityCalendar>
}
