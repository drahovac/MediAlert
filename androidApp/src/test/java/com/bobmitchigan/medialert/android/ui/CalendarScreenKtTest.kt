package com.bobmitchigan.medialert.android.ui

import junit.framework.TestCase.assertEquals
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import java.util.Locale

internal class CalendarScreenKtTest {

    private var saturdayInstant: Instant = Instant.parse("2023-05-13T00:00:00Z")
    private var thursdayInstant: Instant = Instant.parse("2023-05-11T00:00:00Z")
    private var sundayInstant: Instant = Instant.parse("2023-05-14T00:00:00Z")
    private var mondayInstant: Instant = Instant.parse("2023-05-15T00:00:00Z")

    private fun getClock(instant: Instant) = object : Clock {
        override fun now(): Instant {
            return instant
        }
    }

    @Test
    fun `return weekdays for czech locale`() {
        setCsLocale()

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Po, Út, St, Čt, Pá, So, Ne", weekDays)
    }

    private fun setCsLocale() {
        Locale.setDefault(Locale("cs", "CZ"))
    }

    @Test
    fun `return weekdays for us locale`() {
        setUsLocale()

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Sun, Mon, Tue, Wed, Thu, Fri, Sat", weekDays)
    }

    private fun setUsLocale() {
        Locale.setDefault(Locale("en", "US"))
    }

    @Test
    fun `return weekdays for gb locale`() {
        Locale.setDefault(Locale("en", "GB"))

        val weekDays = getShortWeekDays().joinToString()

        assertEquals("Mon, Tue, Wed, Thu, Fri, Sat, Sun", weekDays)
    }

    @Test
    fun `return first weekday in cs locale`() {
        setCsLocale()

        assertEquals(LocalDate.parse("2023-05-08"), getFirstWeekDay(0, getClock(saturdayInstant)))
        assertEquals(LocalDate.parse("2023-05-08"), getFirstWeekDay(0, getClock(sundayInstant)))
        assertEquals(LocalDate.parse("2023-05-08"), getFirstWeekDay(0, getClock(thursdayInstant)))
        assertEquals(LocalDate.parse("2023-05-15"), getFirstWeekDay(0, getClock(mondayInstant)))
    }

    @Test
    fun `return first weekday in cs locale minus weeks`() {
        setCsLocale()

        assertEquals(LocalDate.parse("2023-04-17"), getFirstWeekDay(-3, getClock(saturdayInstant)))
        assertEquals(LocalDate.parse("2023-04-17"), getFirstWeekDay(-3, getClock(sundayInstant)))
        assertEquals(LocalDate.parse("2023-04-17"), getFirstWeekDay(-3, getClock(thursdayInstant)))
        assertEquals(LocalDate.parse("2023-04-24"), getFirstWeekDay(-3, getClock(mondayInstant)))
    }

    @Test
    fun `return first weekday for us locale`() {
        setUsLocale()

        assertEquals(LocalDate.parse("2023-05-07"), getFirstWeekDay(0, getClock(saturdayInstant)))
        assertEquals(LocalDate.parse("2023-05-14"), getFirstWeekDay(0, getClock(sundayInstant)))
        assertEquals(LocalDate.parse("2023-05-07"), getFirstWeekDay(0, getClock(thursdayInstant)))
        assertEquals(LocalDate.parse("2023-05-14"), getFirstWeekDay(0, getClock(mondayInstant)))
    }

    @Test
    fun `return first weekday for us locale plus weeks`() {
        setUsLocale()

        assertEquals(LocalDate.parse("2023-05-28"), getFirstWeekDay(3, getClock(saturdayInstant)))
        assertEquals(LocalDate.parse("2023-06-04"), getFirstWeekDay(3, getClock(sundayInstant)))
        assertEquals(LocalDate.parse("2023-05-28"), getFirstWeekDay(3, getClock(thursdayInstant)))
        assertEquals(LocalDate.parse("2023-06-04"), getFirstWeekDay(3, getClock(mondayInstant)))
    }
}
