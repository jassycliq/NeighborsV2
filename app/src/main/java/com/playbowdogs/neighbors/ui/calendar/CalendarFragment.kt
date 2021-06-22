 package com.playbowdogs.neighbors.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.jakewharton.threetenabp.AndroidThreeTen
import com.playbowdogs.neighbors.data.decorators.EventDecorator
import com.playbowdogs.neighbors.databinding.FragmentCalendarBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.viewmodel.calendar.CalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


 @InternalCoroutinesApi
 @ExperimentalCoroutinesApi
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

     private val viewModel: CalendarViewModel by viewModel()

     private val chosenDayArray = ArrayList<CalendarDay>()
     private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AndroidThreeTen.init(activity)

        viewModel.getAppointments()

        setInsetPadding(view)
        setCalendarTheme()
        setOnClickListeners()
        setObservers()
    }

    private fun setInsetPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            // Move our CalendarView below status bar
            binding.calendarView.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    private fun setCalendarTheme() {
        binding.calendarView.leftArrow?.setTint(requireActivity().getColor(com.playbowdogs.neighbors.R.color.onSurface))
        binding.calendarView.rightArrow?.setTint(requireActivity().getColor(com.playbowdogs.neighbors.R.color.onSurface))
        binding.calendarView.setDateTextAppearance(requireActivity().getColor(com.playbowdogs.neighbors.R.color.onSurface))

//        binding.calendarView.state()?.edit()
//            ?.setMinimumDate(viewModel.threeMonthsBefore)
//            ?.setMaximumDate(viewModel.sixMonthsAhead)
//            ?.commit()
    }

    private fun setOnClickListeners() {
        binding.calendarView.setOnTitleClickListener {
            binding.calendarView.currentDate = CalendarDay.today()
        }
    }

    private fun setObservers() {
        viewModel.appointments.observe(viewLifecycleOwner) {
            it?.let { acuityAppointments ->
                when {
                    acuityAppointments.isEmpty() -> {
                        binding.calendarView.alpha = .25f
                        binding.noAppointmentsTextView.visibility = View.VISIBLE
                    }
                    else -> {
                        lifecycleScope.launch {
                            acuityAppointments.reversed().forEach { appointment ->
                                val calendarDay = CalendarDay.from(LocalDate.parse(
                                    appointment?.datetime,
                                    formatter
                                ))
                                chosenDayArray.add(calendarDay)
                                viewModel.calendarDaySet.value =
                                    chosenDayArray.toSet()
                                when (calendarDay.month == CalendarDay.today().month) {
                                    true -> delay(250L)
                                    false -> Unit
                                }
                            }
                        }
                    }
                }
            }
        }

        viewModel.calendarDaySet.observe(viewLifecycleOwner) {
            it?.let {
                binding.calendarView.addDecorator(
                    EventDecorator(
                        requireContext().getColor(com.playbowdogs.neighbors.R.color.colorPrimary),
                        it
                    )
                )
            }
        }
    }
}
