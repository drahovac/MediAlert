package com.bobmitchigan.medialert.viewModel

import org.junit.Test
import kotlin.test.assertEquals

internal class CalendarViewModelTest {

    private val viewModel: CalendarViewModel = CalendarViewModel()

    @Test
    fun `set initial index`() {
        assertEquals(0, viewModel.state.value.startingWeekIndex)
    }
}
