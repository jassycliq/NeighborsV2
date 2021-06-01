package com.playbowdogs.neighbors.viewmodel.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.utils.BaseViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@ExperimentalCoroutinesApi
class CalendarViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val acuityRepo: AcuityRepository,
    scope: CoroutineScope,
) : BaseViewModel(scope) {
    val calendarDaySet: MutableLiveData<Set<CalendarDay>> by lazy { MutableLiveData<Set<CalendarDay>>() }

    private val currentDate = LocalDate.now()
    val threeMonthsBefore: LocalDate = currentDate.minusMonths(3)
    val sixMonthsAhead: LocalDate = currentDate.plusMonths(6)

    private val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    private val threeBeforeFormatted = threeMonthsBefore.format(formatter)
    private val sixAheadFormatted = sixMonthsAhead.format(formatter)

    fun getCustomerAppointments() = liveData(scope.coroutineContext) {
        firestoreRepo.getUserModel()?.let {
            emit(acuityRepo.getAppointments(
                email = it.email,
                minDate = threeBeforeFormatted,
                maxDate = sixAheadFormatted,
            ))
        }
    }
}
