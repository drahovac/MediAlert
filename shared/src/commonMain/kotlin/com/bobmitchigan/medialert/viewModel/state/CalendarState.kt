package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.LocalDateTime

/**
 * A data class representing the state of the calendar.
 *
 * @property startingWeekDay First day of selected week.
 * @property selectedHour The hour that is currently selected in the calendar.
 * @property cells A list of calendar cells.
 */
data class CalendarState(
    val startingWeekIndex: Int = 0,
    val selectedHour: LocalDateTime? = null,
    val cells: List<CalendarCell> = emptyList()
)

/**
 * Represents a slot cell in a calendar. May contain event of taken or to be taken medicine.
 */
data class CalendarCell(
    val medicine: MedicineEvent? = null
)
