package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.data.ScheduleAdapter.deserialize
import com.bobmitchigan.medialert.data.ScheduleAdapter.serialize
import kotlinx.datetime.LocalTime
import org.junit.Test
import kotlin.test.assertEquals

internal class ScheduleAdapterTest {

    @Test
    fun `serialize dates list`() {
        val list = listOf(TIME_1, TIME_2, TIME_3, TIME_4)

        assertEquals(
            "01:04;03:34;04:15:06;15:55",
            list.serialize()
        )
    }

    @Test
    fun `deserialize dates list`() {
        val list =
            deserialize("01:04;03:34;04:15:06;15:55")

        assertEquals(listOf(TIME_1, TIME_2, TIME_3, TIME_4), list)
    }

    private companion object {
        val TIME_1 = LocalTime(1, 4)
        val TIME_2 = LocalTime(3, 34)
        val TIME_3 = LocalTime(4, 15, 6)
        val TIME_4 = LocalTime(15, 55)
    }
}
