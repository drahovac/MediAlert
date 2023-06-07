package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.LocalDateTime
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class DateFormatterTest {

    @Test
    fun `format czech locale`() {
        Locale.setDefault(Locale("cs", "CZ"))

       assertEquals("24. 5. 2023 3:06:00", DATE_1.toFormattedDateTime())
       assertEquals("1. 1. 2023 13:24:00", DATE_2.toFormattedDateTime())
       assertEquals("20. 10. 2023 13:24:00", DATE_3.toFormattedDateTime())
    }

    @Test
    fun `format en locale`() {
        Locale.setDefault(Locale.US)

        assertEquals("May 24, 2023, 3:06:00 AM", DATE_1.toFormattedDateTime())
        assertEquals("Jan 1, 2023, 1:24:00 PM", DATE_2.toFormattedDateTime())
        assertEquals("Oct 20, 2023, 1:24:00 PM", DATE_3.toFormattedDateTime())
    }

    private companion object {
        val DATE_1 = LocalDateTime.parse("2023-05-24T03:06")
        val DATE_2 = LocalDateTime.parse("2023-01-01T13:24")
        val DATE_3 = LocalDateTime.parse("2023-10-20T13:24")
    }
}
