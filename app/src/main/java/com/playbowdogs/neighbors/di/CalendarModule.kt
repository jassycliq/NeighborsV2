package com.playbowdogs.neighbors.di

import com.playbowdogs.neighbors.data.repository.AcuityRepository
import com.playbowdogs.neighbors.data.repository.FirestoreRepository
import com.playbowdogs.neighbors.viewmodel.calendar.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val calendarModule: Module = module {
    viewModel {
        CalendarViewModel(
            get<FirestoreRepository>(),
            get<AcuityRepository>(),
            get<CoroutineScope>()
        )
    }
}
