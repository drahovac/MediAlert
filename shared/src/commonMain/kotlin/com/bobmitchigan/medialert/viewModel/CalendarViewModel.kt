package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

class CalendarViewModel(
    private val clock: Clock = Clock.System
) : BaseViewModel() {

    private val _state: MutableStateFlow<CalendarState> =
        MutableStateFlow(CalendarState(getFirstDate()))
    val state = _state.asStateFlow()

    private fun getFirstDate(): LocalDate {
        return clock.todayIn(TimeZone.currentSystemDefault()).run {
            minus(value = dayOfWeek.ordinal, unit = DateTimeUnit.DAY)
        }
    }
}
