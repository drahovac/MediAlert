package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.viewModel.state.CalendarCell
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

    @Test
    fun `set initial time cells correctly`() {
        assertEquals(24 * 2 * 8, viewModel.state.value.cells.size)
        viewModel.state.value.cells.let {
            println(it.joinToString { it.toString() })
            listOf(
                8, 24, 40, 56, 72, 88, 104, 120, 136, 152, 168, 184,
                200, 216, 232, 248, 264, 280, 296, 312, 328, 344, 360, 376
            ).forEach { index ->
                assertEquals(null, it.timeCellValue(index))
            }
            assertEquals(0, it.timeCellValue(0))
            assertEquals(1, it.timeCellValue(16))
            assertEquals(2, it.timeCellValue(32))
            assertEquals(3, it.timeCellValue(48))
            assertEquals(4, it.timeCellValue(64))
            assertEquals(5, it.timeCellValue(80))
            assertEquals(6, it.timeCellValue(96))
            assertEquals(7, it.timeCellValue(112))
            assertEquals(8, it.timeCellValue(128))
            assertEquals(9, it.timeCellValue(144))
            assertEquals(10, it.timeCellValue(160))
            assertEquals(11, it.timeCellValue(176))
            assertEquals(12, it.timeCellValue(192))
            assertEquals(13, it.timeCellValue(208))
            assertEquals(14, it.timeCellValue(224))
            assertEquals(15, it.timeCellValue(240))
            assertEquals(16, it.timeCellValue(256))
            assertEquals(17, it.timeCellValue(272))
            assertEquals(18, it.timeCellValue(288))
            assertEquals(19, it.timeCellValue(304))
            assertEquals(20, it.timeCellValue(320))
            assertEquals(21, it.timeCellValue(336))
            assertEquals(22, it.timeCellValue(352))
            assertEquals(23, it.timeCellValue(368))
            assertEquals(24, it.timeCellValue(384))
        }
    }

    private fun List<CalendarCell>.timeCellValue(index: Int) =
        (this[index] as CalendarCell.TimeCell).hour
}
