package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.test.assertEquals

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


    @Test
    fun `return correct cell for calendar coordinates`() {
        val state = CalendarState(
            events = mapOf(
                date1 to listOf(event1, event2, event3)
            )
        )

        assertEquals(event1, state.getEvent(date1, CalendarCoordinates(0, 0)))
        assertEquals(event2, state.getEvent(date1, CalendarCoordinates(25, 2)))
    }
}
