package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 * A data class representing the state of the calendar.
 *
 * @property startingWeekDay The day of the week that the calendar starts on.
 * @property selectedHour The hour that is currently selected in the calendar.
 * @property events A list of medicine events in future.
 * @property taken A list of blister cavities that have been taken.
 */
data class CalendarState(
    val startingWeekDay: LocalDate,
    val selectedHour: LocalDateTime? = null,
    val events: List<MedicineEvent> = emptyList(),
    val taken: List<BlisterCavity.EATEN> = emptyList()
)
