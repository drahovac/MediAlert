package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.data.BlisterPackAdapter.deserializeBlisterPacks
import com.bobmitchigan.medialert.data.BlisterPackAdapter.serialize
import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.BlisterPackRow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.Test
import kotlin.test.assertEquals

internal class BlisterPackAdapterTest {

    private val dateA =
        LocalDateTime(
            year = 2022,
            month = Month.APRIL,
            dayOfMonth = 3,
            hour = 3,
            minute = 6
        )
    private val dateB =
        LocalDateTime(
            year = 2023,
            month = Month.DECEMBER,
            dayOfMonth = 25,
            hour = 3,
            minute = 6
        )
    private val blisterPackA: BlisterPack = BlisterPack(
        rows = listOf(
            BlisterPackRow(
                listOf(
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.NONE,
                    BlisterCavity.LOST
                )
            ),
            BlisterPackRow(
                listOf(
                    BlisterCavity.EATEN(dateA),
                    BlisterCavity.EATEN(dateB),
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED
                )
            ),
        )
    )

    private val blisterPackB: BlisterPack = BlisterPack(
        rows = listOf(
            BlisterPackRow(
                listOf(
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                )
            ),
            BlisterPackRow(
                listOf(
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED
                )
            ),
            BlisterPackRow(
                listOf(
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED
                )
            ),
        )
    )
    private val expectedSerialised =
        "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F;F.F,F.F.F.F,F.F.F.F;" +
                "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F"

    @Test
    fun `serialize blister packs`() {
        val serialised = listOf(blisterPackA, blisterPackB, blisterPackA).serialize()

        assertEquals(expectedSerialised, serialised)
    }

    @Test
    fun `deserialize blister packs`() {
        val deserialized = deserializeBlisterPacks(expectedSerialised)

        assertEquals(deserialized, listOf(blisterPackA, blisterPackB, blisterPackA))
    }
}