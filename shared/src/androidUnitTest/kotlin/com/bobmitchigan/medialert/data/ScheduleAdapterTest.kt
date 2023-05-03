package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.data.ScheduleAdapter.deserialize
import com.bobmitchigan.medialert.data.ScheduleAdapter.serialize
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.test.assertEquals

internal class ScheduleAdapterTest {

    @Test
    fun `serialize dates list`() {
        val list = listOf(DATE_1, DATE_2, DATE_3, DATE_4)

        assertEquals(
            "2021-12-26T01:04;2023-11-01T03:34;2025-04-04T15:06;2023-03-23T15:55",
            list.serialize()
        )
    }

    @Test
    fun `deserialize dates list`() {
        val list =
            deserialize("2021-12-26T01:04;2023-11-01T03:34;2025-04-04T15:06;2023-03-23T15:55")

        assertEquals(listOf(DATE_1, DATE_2, DATE_3, DATE_4), list)
    }

    private companion object {
        val DATE_1 = LocalDateTime(2021, 12, 26, 1, 4)
        val DATE_2 = LocalDateTime(2023, 11, 1, 3, 34)
        val DATE_3 = LocalDateTime(2025, 4, 4, 15, 6)
        val DATE_4 = LocalDateTime(2023, 3, 23, 15, 55)
    }
}
