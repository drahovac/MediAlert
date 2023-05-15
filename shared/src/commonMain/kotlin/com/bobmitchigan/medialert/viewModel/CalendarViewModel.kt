package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.viewModel.state.CalendarCell
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
        MutableStateFlow(
            CalendarState(
                startingWeekDay = getFirstDate(),
                cells = initialCells()
            )
        )
    val state = _state.asStateFlow()

    private fun initialCells(): List<CalendarCell> {
        return List(CELLS_COUNT) {
            if (isFirstColumn(it)) {
                val hour =
                    if (it % COLUMN_WITH_HOUR == 0) it / COLUMN_WITH_HOUR else null
                CalendarCell.TimeCell(hour)
            } else {
                CalendarCell.SlotCell()
            }
        }
    }

    private fun isFirstColumn(cellIndex: Int) = cellIndex == 0 || cellIndex % (COLUMN_COUNT) == 0

    private fun getFirstDate(): LocalDate {
        return clock.todayIn(TimeZone.currentSystemDefault()).run {
            minus(value = dayOfWeek.ordinal, unit = DateTimeUnit.DAY)
        }
    }

    private companion object {
        const val COLUMN_COUNT = 8
        const val COLUMN_WITH_HOUR = 16
        const val HOURS_PER_DAY = 24
        const val CELLS_COUNT =
            (HOURS_PER_DAY) * 2 * COLUMN_COUNT // two rows per hour, 8 columns (1 per day and spacer)
    }
}
