package com.bobmitchigan.medialert.domain

import com.bobmitchigan.medialert.domain.Destination.MedicineList.getStaticDestinationRoute
import org.junit.Assert.assertEquals
import org.junit.Test

class DestinationTest {

    @Test
    fun `return valid destination route`() {
        assertEquals(
            "com.bobmitchigan.medialert.domain.Destination.CreateMedicine",
            Destination.CreateMedicine.destination().getStaticDestinationRoute()
        )
        assertEquals(
            "com.bobmitchigan.medialert.domain.Destination.MedicineList",
            Destination.MedicineList.destination().getStaticDestinationRoute()
        )
        assertEquals(
            "com.bobmitchigan.medialert.domain.Destination.SingleMedicine",
            Destination.SingleMedicine(2).destination().getStaticDestinationRoute()
        )
        assertEquals(
            "com.bobmitchigan.medialert.domain.Destination.SingleMedicine",
            Destination.SingleMedicine(null).destination().getStaticDestinationRoute()
        )
    }
}
