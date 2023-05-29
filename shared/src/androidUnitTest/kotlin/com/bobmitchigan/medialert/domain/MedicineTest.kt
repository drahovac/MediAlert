package com.bobmitchigan.medialert.domain

import com.bobmitchigan.medialert.data.BlisterPackAdapter
import org.junit.Test
import kotlin.test.assertEquals

internal class MedicineTest {

    @Test
    fun `set zero eaten when no pill eaten`() {
        val medicine = MEDICINE.copy(blisterPacks = NOTHING_EATEN_PACKS)

        assertEquals(0, medicine.eatenCount())
    }

    @Test
    fun `set valid eaten count when some pills eaten`() {
        val medicine = MEDICINE.copy(blisterPacks = SOME_EATEN)

        assertEquals(4, medicine.eatenCount())
    }

    @Test
    fun `set zero filled when no pill remaining`() {
        val medicine = MEDICINE.copy(blisterPacks = NOTHING_REMAINING)

        assertEquals(0, medicine.remainingCount())
    }

    @Test
    fun `set valid filled count when pills remaining`() {
        val medicine = MEDICINE.copy(blisterPacks = SOME_EATEN)

        assertEquals(18, medicine.remainingCount())
    }

    private companion object {
        val MEDICINE = Medicine("Prototype", listOf(), listOf(), dateTimeNow())
        val NOTHING_EATEN_PACKS = BlisterPackAdapter.deserializeBlisterPacks(
            "F.F.N.L,F.F.F;F.F,F.F.F.F,F.F.F.F;" +
                    "F.F.N.L,N.F.F"
        )
        val SOME_EATEN = BlisterPackAdapter.deserializeBlisterPacks(
            "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F;F.F,F.F.F.F,F.F.F.F;" +
                    "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F"
        )
        val NOTHING_REMAINING = BlisterPackAdapter.deserializeBlisterPacks(
            "L.L.L,E2022-04-03T03:06.E2023-12-25T03:06;" +
                    "L.N.L,E2022-04-03T03:06.E2023-12-25T03:06"
        )
    }
}