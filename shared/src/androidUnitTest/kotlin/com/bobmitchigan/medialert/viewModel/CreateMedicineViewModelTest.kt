package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.domain.*
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CreateMedicineViewModelTest {

    private val medicineRepository: MedicineRepository = mockk(relaxUnitFun = true)
    private lateinit var createMedicineViewModel: CreateMedicineViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        createMedicineViewModel = CreateMedicineViewModel(medicineRepository)
    }

    @Test
    fun `update blister pack count with valid number`() {
        createMedicineViewModel.updateBlisterPacksCount("6")

        assertEquals(6, stateValue().blisterPackCount.value)
    }

    @Test
    fun `update blister pack count with empty string`() {
        createMedicineViewModel.updateBlisterPacksCount("")

        assertNull(stateValue().blisterPackCount.value)
    }

    @Test
    fun `update blister pack count with invalid number string`() {
        createMedicineViewModel.updateBlisterPacksCount("invalid")

        assertNull(stateValue().blisterPackCount.value)
    }

    @Test
    fun `update blister pack row count `() {
        createMedicineViewModel.updateRowCount("2")

        assertEquals(2, stateValue().rowCount.value)
    }

    @Test
    fun `update blister pack column count `() {
        createMedicineViewModel.updateColumnCount("10")

        assertEquals(10, stateValue().columnCount.value)
    }

    @Test
    fun `set error on submit if all not filled`() {
        createMedicineViewModel.submit()

        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().name.error
        )
        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().rowCount.error
        )
        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().columnCount.error
        )
        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().blisterPackCount.error
        )
    }

    @Test
    fun `clear error on submit if all filled`() {
        createMedicineViewModel.submit()

        createMedicineViewModel.updateRowCount("3")
        createMedicineViewModel.updateColumnCount("4")
        createMedicineViewModel.updateBlisterPacksCount("3")
        createMedicineViewModel.updateName("Name")

        assertNull(stateValue().rowCount.error)
        assertNull(stateValue().columnCount.error)
        assertNull(stateValue().blisterPackCount.error)
        assertNull(stateValue().name.error)
    }

    @Test
    fun `set error on submit if only name not filled`() {
        createMedicineViewModel.updateRowCount("3")
        createMedicineViewModel.updateColumnCount("4")
        createMedicineViewModel.updateBlisterPacksCount("3")
        createMedicineViewModel.updateName("")

        createMedicineViewModel.submit()

        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().name.error
        )
        assertNull(stateValue().rowCount.error)
        assertNull(stateValue().columnCount.error)
        assertNull(stateValue().blisterPackCount.error)
    }

    @Test
    fun `save medicine on submit if all filled`() {
        createMedicineViewModel.updateRowCount("2")
        createMedicineViewModel.updateColumnCount("3")
        createMedicineViewModel.updateBlisterPacksCount("4")
        createMedicineViewModel.updateName("Name")
        val blisterPackRow =
            BlisterPackRow(listOf(BlisterCavity.FILLED, BlisterCavity.FILLED, BlisterCavity.FILLED))

        createMedicineViewModel.submit()

        coVerify {
            medicineRepository.saveMedicine(
                Medicine(
                    name = "Name",
                    blisterPacks = listOf(
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                    ),
                    listOf()
                )
            )
        }
    }

    private fun stateValue() = createMedicineViewModel.state.value

}