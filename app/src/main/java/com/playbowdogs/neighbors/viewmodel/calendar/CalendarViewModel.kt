package com.playbowdogs.neighbors.viewmodel.calendar

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.FirebaseFunctionsRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.playbowdogs.neighbors.utils.USER_UNIQUE_IDENTIFICATION
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@ExperimentalCoroutinesApi
class CalendarViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val functionRepo: FirebaseFunctionsRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {
    val calendarDaySet: MutableLiveData<Set<CalendarDay>> by lazy { MutableLiveData<Set<CalendarDay>>() }

    private val currentDate = LocalDate.now()
    val threeMonthsBefore: LocalDate = currentDate.minusMonths(3)
    val sixMonthsAhead: LocalDate = currentDate.plusMonths(6)

    suspend fun getAppointments() = functionRepo.getCalendar()

    val appointments = liveData(scope.coroutineContext) {
        firestoreRepo.getAppointmentsFlow()?.collect { appointmentList ->
            emit(appointmentList)
        }
    }
}
