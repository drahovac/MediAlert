package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

// TODO is it possible to test another locale?
class DateFormatterTest {

    @Test
    fun `format en locale`() {
        assertEquals("May 24, 2023 at 3:06 AM", DATE_1.toFormattedDateTime())
        assertEquals("Jan 1, 2023 at 1:24 PM", DATE_2.toFormattedDateTime())
        assertEquals("Oct 20, 2023 at 1:24 PM", DATE_3.toFormattedDateTime())
    }

    private companion object {
        val DATE_1 = LocalDateTime.parse("2023-05-24T03:06")
        val DATE_2 = LocalDateTime.parse("2023-01-01T13:24")
        val DATE_3 = LocalDateTime.parse("2023-10-20T13:24")
    }
}
