package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.viewModel.state.CalendarCell
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalendarViewModel : BaseViewModel() {

    private val _state: MutableStateFlow<CalendarState> =
        MutableStateFlow(
            CalendarState(
                cells = initialCells()
            )
        )
    val state = _state.asStateFlow()

    private fun initialCells(): List<CalendarCell> {
        return List(CELLS_COUNT) {
            CalendarCell()
        }
    }

    companion object {
        private const val COLUMN_COUNT = 8
        private const val HOURS_PER_DAY = 24
        private const val CELLS_COUNT =
            (HOURS_PER_DAY) * 2 * COLUMN_COUNT // two rows per hour, 8 columns (1 per day and spacer)
    }
}
