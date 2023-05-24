package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.MedicineEvent
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

class CalendarViewModel(medicalRepository: MedicineRepository) : BaseViewModel(), CalendarActions {

    private val _state: MutableStateFlow<CalendarState> =
        MutableStateFlow(
            CalendarState(
                events = initialCells()
            )
        )
    val state = _state.asStateFlow()

    override fun selectCell(row: Int, column: Int) {
        println("Index $row")
    }

    override fun fetchWeekCells(startingWeekDay: LocalDate) {
        println("vaclav $startingWeekDay")
    }

    private fun initialCells(): Map<LocalDate, List<MedicineEvent>> {
        return emptyMap()
    }

    companion object {
        private const val COLUMN_COUNT = 8
        private const val HOURS_PER_DAY = 24
    }
}

interface CalendarActions {

    fun selectCell(row: Int, column: Int)

    fun fetchWeekCells(startingWeekDay: LocalDate)
}
