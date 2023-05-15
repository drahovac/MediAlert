package com.bobmitchigan.medialert.android.ui

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

internal class CalendarScreenKtTest {

    @Test
    fun `return weekdays for czech locale`() {
        Locale.setDefault(Locale("cs", "CZ"))

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Po, Út, St, Čt, Pá, So, Ne", weekDays)
    }

    @Test
    fun `return weekdays for us locale`() {
        Locale.setDefault(Locale("en", "US"))

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Sun, Mon, Tue, Wed, Thu, Fri, Sat", weekDays)
    }

    @Test
    fun `return weekdays for gb locale`() {
        Locale.setDefault(Locale("en", "GB"))

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Mon, Tue, Wed, Thu, Fri, Sat, Sun", weekDays)
    }
}
