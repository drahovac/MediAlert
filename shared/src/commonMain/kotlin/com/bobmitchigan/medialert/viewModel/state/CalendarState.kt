package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 * A data class representing the state of the calendar.
 *
 * @property startingWeekDay First day of selected week.
 * @property selectedHour The hour that is currently selected in the calendar.
 * @property cells A list of calendar cells.
 */
data class CalendarState(
    val startingWeekDay: LocalDate,
    val selectedHour: LocalDateTime? = null,
    val cells: List<CalendarCell> = emptyList()
)

fun getFirstDay(): LocalDate {
    return LocalDate.fromEpochDays(0)
}

/**
 * Represents a cell in a calendar.
 */
sealed interface CalendarCell {

    /**
     * Represents a time cell in a calendar. Used for first column.
     * If hour is null, means cell should not show hour label.
     */
    data class TimeCell(
        val hour: Int? = 0
    ) : CalendarCell

    /**
     * Represents a slot cell in a calendar. May contain event of taken or to be taken medicine.
     */
    data class SlotCell(
        val medicine: MedicineEvent? = null
    ) : CalendarCell
}
