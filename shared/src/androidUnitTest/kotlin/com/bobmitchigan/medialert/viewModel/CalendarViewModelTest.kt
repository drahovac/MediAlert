package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

internal class CalendarViewModelTest {

    private var currentInstant: Instant = Instant.parse("2023-05-13T00:00:00Z") // Saturday
    private val viewModel: CalendarViewModel = CalendarViewModel(object : Clock {
        override fun now(): Instant {
            return currentInstant
        }
    })

    @Test
    fun `set initial first day correctly`() {
        assertEquals(LocalDate.parse("2023-05-08"), viewModel.state.value.startingWeekDay)
    }
}
