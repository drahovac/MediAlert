package com.bobmitchigan.medialert.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFalse

class MedicineEventTest {

    private val clock = object : Clock {
        override fun now(): Instant {
            return Instant.parse("2023-05-11T12:00:00Z")
        }
    }

    @Test
    fun `return correct has take action`() {
        assertTrue(createEvent(eventType = EventType.PLANNED).hasTakeAction(clock))
        assertTrue(
            createEvent(
                eventType = EventType.MISSING,
                dateTime = LocalDateTime.parse("2023-05-11T03:00:00")
            ).hasTakeAction(clock)
        )
        assertFalse(
            createEvent(
                eventType = EventType.MISSING,
                dateTime = LocalDateTime.parse("2023-05-10T03:00:00")
            ).hasTakeAction(clock)
        )
    }

    private fun createEvent(
        eventType: EventType, dateTime: LocalDateTime = LocalDateTime.parse(
            "2023-04-11T00:00:00"
        )
    ) = MedicineEvent(
        dateTime = dateTime,
        medicine = Medicine(
            "", emptyList(), emptyList(), LocalDateTime.parse(
                "2023-04-11T00:00:00"
            )
        ),
        cavity = null,
        eventType = eventType
    )
}