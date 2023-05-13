package com.bobmitchigan.medialert.android.ui.component

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.bobmitchigan.medialert.domain.Destination
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class NavigationKtTest {

    @Test
    fun testNavigateSingleTop() {
        val navController = mockk<NavController>(relaxUnitFun = true)
        val destination = Destination.MedicineList
        val slot = slot<NavOptionsBuilder.() -> Unit>()

        navController.navigateSingleTop(destination)

        verify { navController.navigate(destination.destination(), capture(slot)) }
    }
}
