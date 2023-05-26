package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalendarStateTest {

    private val medicine1: Medicine = Medicine("Name1", listOf(), listOf())
    private val medicine2: Medicine = Medicine("Name2", listOf(), listOf())
    private val medicine3: Medicine = Medicine("Name3", listOf(), listOf())
    private val date1 = LocalDate.parse("2023-05-24")
    private val dateTime1 = LocalDateTime.parse("2023-05-24T00:10")
    private val dateTime2 = LocalDateTime.parse("2023-05-26T12:35")
    private val dateTime3 = LocalDateTime.parse("2023-05-24T00:00")
    private val event1 = MedicineEvent(dateTime1, medicine1)
    private val event2 = MedicineEvent(dateTime2, medicine2)
    private val event3 = MedicineEvent(dateTime3, medicine3)
    private val event4 = MedicineEvent(dateTime2, medicine2)


    @Test
    fun `return correct events for calendar coordinates`() {
        val state = CalendarState(
            events = mapOf(
                date1 to listOf(event1, event2, event3, event4)
            )
        )

        assertEquals(event1, state.getEvents(date1, CalendarCoordinates(0, 0))[0])
        assertEquals(event3, state.getEvents(date1, CalendarCoordinates(0, 0))[1])
        assertEquals(event2, state.getEvents(date1, CalendarCoordinates(25, 2))[0])
        assertEquals(event4, state.getEvents(date1, CalendarCoordinates(25, 2))[1])
    }

    @Test
    fun `return empty event for calendar coordinates out of 24 hours`() {
        val state = CalendarState(
            events = mapOf(
                date1 to listOf(event1, event2, event3, event4)
            )
        )

        assertTrue { state.getEvents(date1, CalendarCoordinates(48, 2)).isEmpty()}
        assertTrue { state.getEvents(date1, CalendarCoordinates(-1, 2)).isEmpty()}
        assertTrue { state.getEvents(date1, CalendarCoordinates(120, 2)).isEmpty()}
    }
}
