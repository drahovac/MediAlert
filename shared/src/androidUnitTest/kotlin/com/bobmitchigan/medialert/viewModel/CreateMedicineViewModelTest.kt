package com.bobmitchigan.medialert.viewModel

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CreateMedicineViewModelTest {

    private lateinit var createMedicineViewModel: CreateMedicineViewModel

    @Before
    fun setUp() {
        createMedicineViewModel = CreateMedicineViewModel()
    }

    @Test
    fun `update blister pack count with valid number`() {
        createMedicineViewModel.updateBlisterPacksCount("6")

        assertEquals(6, stateValue().blisterPackCount)
    }

    @Test
    fun `update blister pack count with empty string`() {
        createMedicineViewModel.updateBlisterPacksCount("")

        assertNull(stateValue().blisterPackCount)
    }

    @Test
    fun `update blister pack count with invalid number string`() {
        createMedicineViewModel.updateBlisterPacksCount("invalid")

        assertNull(stateValue().blisterPackCount)
    }

    @Test
    fun `update blister pack row count `() {
        createMedicineViewModel.updateRowCount("2")

        assertEquals(2, stateValue().rowCount)
    }

    @Test
    fun `update blister pack column count `() {
        createMedicineViewModel.updateColumnCount("10")

        assertEquals(10, stateValue().columnCount)
    }

    private fun stateValue() = createMedicineViewModel.state.value

}