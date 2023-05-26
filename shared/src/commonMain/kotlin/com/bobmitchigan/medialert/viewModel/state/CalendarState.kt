package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * A data class representing the state of the calendar.
 *
 * @property selectedHour The hour that is currently selected in the calendar.
 * @property events Map containing calendar rows for week - key is date of first week.
 */
data class CalendarState(
    val selectedHour: LocalDateTime? = null,
    val events: Map<LocalDate, List<MedicineEvent>> = emptyMap()
) {
    fun getEvents(
        startingWeekDay: LocalDate,
        coordinates: CalendarCoordinates
    ): List<MedicineEvent> {
        return events[startingWeekDay]?.takeIf { coordinates.row in 0..HAX_ROW_COUNT }?.let {
            println(coordinates)
            val startTime = startingWeekDay.plusDays(coordinates.column).atTime(
                LocalTime(
                    coordinates.row / 2,
                    if (coordinates.row % 2 == 0) 0 else HALF_HOUR_MINUTES
                )
            )
            val endTime = startTime.toInstant(TimeZone.currentSystemDefault())
                .plus(HALF_HOUR_MINUTES, DateTimeUnit.MINUTE)
                .toLocalDateTime(TimeZone.currentSystemDefault())

            return it.filter { event -> event.dateTime >= startTime && event.dateTime < endTime }
        } ?: emptyList()
    }
}

fun LocalDate.plusDays(days: Int) = plus(days, DateTimeUnit.DAY)

data class CalendarCoordinates(
    val row: Int,
    val column: Int,
)

private const val HALF_HOUR_MINUTES = 30
private const val HAX_ROW_COUNT = 47 // half hour per row, 0:00 is row 0 so 48 not allowed,
