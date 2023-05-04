package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.domain.*
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalTime
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
    fun `update blister pack count with valid number and set number of empty dimensions if previously empty`() {
        createMedicineViewModel.updateBlisterPacksCount("6")

        assertEquals(6, stateValue().blisterPackCount.value)
        assertEquals(List(6) { BlisterPackDimension() }, stateValue().dimensions)
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
    fun `update first blister pack row count if all packs same`() {
        createMedicineViewModel.updateBlisterPacksCount("3")
        createMedicineViewModel.updateRowCount("2")

        assertEquals(2, stateValue().dimensions.first().rowCount.value)
    }

    @Test
    fun `update first blister pack column count if all pack same`() {
        createMedicineViewModel.updateBlisterPacksCount("5")
        createMedicineViewModel.updateColumnCount("10")

        assertEquals(10, stateValue().dimensions.first().columnCount.value)
    }

    @Test
    fun `clear dimensions if count set to zero`() {
        createMedicineViewModel.updateBlisterPacksCount("6")
        createMedicineViewModel.updateBlisterPacksCount("unknown")

        assertTrue(createMedicineViewModel.state.value.dimensions.isEmpty())
    }

    @Test
    fun `add additional dimensions if count of packs increased`() {
        createMedicineViewModel.updateBlisterPacksCount("2")
        createMedicineViewModel.updateRowCount("3", 1)
        createMedicineViewModel.updateRowCount("2", 0)
        createMedicineViewModel.updateColumnCount("3", 0)

        createMedicineViewModel.updateBlisterPacksCount("4")

        assertEquals(4, stateValue().dimensions.size)
        assertEquals(BlisterPackDimension(), stateValue().dimensions[2])
        assertEquals(BlisterPackDimension(), stateValue().dimensions[3])
        assertEquals(2, stateValue().dimensions[0].rowCount.value)
        assertEquals(3, stateValue().dimensions[0].columnCount.value)
        assertEquals(3, stateValue().dimensions[1].rowCount.value)
        assertNull(stateValue().dimensions[1].columnCount.value)
    }

    @Test
    fun `remove additional dimensions if count of packs decreased`() {
        createMedicineViewModel.updateBlisterPacksCount("4")
        createMedicineViewModel.updateRowCount("3", 1)
        createMedicineViewModel.updateRowCount("2", 0)
        createMedicineViewModel.updateColumnCount("3", 0)
        createMedicineViewModel.updateRowCount("4", 3)
        createMedicineViewModel.updateColumnCount("5", 3)

        createMedicineViewModel.updateBlisterPacksCount("2")

        assertEquals(2, stateValue().dimensions.size)
        assertEquals(2, stateValue().dimensions[0].rowCount.value)
        assertEquals(3, stateValue().dimensions[0].columnCount.value)
        assertEquals(3, stateValue().dimensions[1].rowCount.value)
        assertNull(stateValue().dimensions[1].columnCount.value)
    }

    @Test
    fun `update times per day`() {
        createMedicineViewModel.updateTimesPerDay("6")

        assertEquals(6, stateValue().timesPerDay.value)
    }

    @Test
    fun `set time input states on times per day`() {
        createMedicineViewModel.updateTimesPerDay("5")

        assertEquals(5, createMedicineViewModel.state.value.timeSchedule.size)
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
            stateValue().blisterPackCount.error
        )
        assertTrue(stateValue().dimensions.isEmpty())
    }

    @Test
    fun `clear error on submit if all filled`() {
        createMedicineViewModel.submit()

        createMedicineViewModel.updateRowCount("3")
        createMedicineViewModel.updateColumnCount("4")
        createMedicineViewModel.updateBlisterPacksCount("3")
        createMedicineViewModel.updateName("Name")

        stateValue().dimensions.forEach {
            assertNull(it.rowCount.error)
            assertNull(it.columnCount.error)
        }
        assertNull(stateValue().blisterPackCount.error)
        assertNull(stateValue().name.error)
    }

    @Test
    fun `set error on submit if only name not filled`() {
        createMedicineViewModel.updateRowCount("3")
        createMedicineViewModel.updateColumnCount("4")
        createMedicineViewModel.updateBlisterPacksCount("3")
        (0..3).forEach {
            createMedicineViewModel.updateRowCount("3", it)
            createMedicineViewModel.updateColumnCount("3", it)
        }
        createMedicineViewModel.updateTimesPerDay("6")
        createMedicineViewModel.updateName("")

        createMedicineViewModel.submit()

        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().name.error
        )
        stateValue().dimensions.forEach {
            assertNull(it.rowCount.error)
            assertNull(it.columnCount.error)
        }
        assertNull(stateValue().blisterPackCount.error)
        assertEquals(6, stateValue().timesPerDay.value)
    }

    @Test
    fun `set error on submit if blister count zero`() {
        createMedicineViewModel.updateRowCount("3")
        createMedicineViewModel.updateColumnCount("4")
        createMedicineViewModel.updateBlisterPacksCount("0")
        createMedicineViewModel.updateName("Name")

        createMedicineViewModel.submit()

        assertEquals(
            MR.strings.create_medicine_mandatory_field,
            stateValue().blisterPackCount.error
        )
        assertFalse { createMedicineViewModel.navigationEvent.value }
    }

    @Test
    fun `set error on submit if only some dimensions not filled`() {
        createMedicineViewModel.updateName("Name")
        createMedicineViewModel.updateBlisterPacksCount("5")
        (0..2).forEach {
            createMedicineViewModel.updateRowCount("3", it)
            createMedicineViewModel.updateColumnCount("3", it)
        }
        createMedicineViewModel.updateAllPacksIdentical()

        createMedicineViewModel.submit()

        assertNull(stateValue().name.error)
        stateValue().dimensions.forEachIndexed { index, dimen ->
            if (index <= 2) {
                assertNull(dimen.rowCount.error)
                assertNull(dimen.columnCount.error)
            } else {
                assertEquals(
                    MR.strings.create_medicine_mandatory_field,
                    dimen.rowCount.error
                )
                assertEquals(
                    MR.strings.create_medicine_mandatory_field,
                    dimen.columnCount.error
                )
            }
        }
        assertNull(stateValue().blisterPackCount.error)
        coVerify(exactly = 0) { medicineRepository.saveMedicine(any()) }
        assertFalse(createMedicineViewModel.navigationEvent.value)
    }

    @Test
    fun `submit if only first dimensions filled and all pack same true`() {
        createMedicineViewModel.updateName("Name")
        createMedicineViewModel.updateBlisterPacksCount("5")
        (0..2).forEach {
            createMedicineViewModel.updateRowCount("3", it)
            createMedicineViewModel.updateColumnCount("6", it)
        }
        createMedicineViewModel.updateRowCount("2", 0)
        createMedicineViewModel.updateColumnCount("3", 0)
        val blisterPackRow =
            BlisterPackRow(listOf(BlisterCavity.FILLED, BlisterCavity.FILLED, BlisterCavity.FILLED))

        createMedicineViewModel.submit()

        assertNull(stateValue().name.error)
        assertNull(stateValue().blisterPackCount.error)
        coVerify { medicineRepository.saveMedicine(any()) }
        assertTrue(createMedicineViewModel.navigationEvent.value)
        coVerify {
            medicineRepository.saveMedicine(
                Medicine(
                    name = "Name",
                    blisterPacks = listOf(
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
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

    @Test
    fun `set error on submit if zero dimensions not filled`() {
        createMedicineViewModel.updateName("Name")
        createMedicineViewModel.updateBlisterPacksCount("5")
        stateValue().dimensions.forEachIndexed { index, _ ->
            createMedicineViewModel.updateRowCount("0", index)
            createMedicineViewModel.updateColumnCount("0", index)
        }
        createMedicineViewModel.updateAllPacksIdentical()

        createMedicineViewModel.submit()

        assertNull(stateValue().name.error)
        stateValue().dimensions.forEach {
            assertEquals(
                MR.strings.create_medicine_mandatory_field,
                it.rowCount.error
            )
            assertEquals(
                MR.strings.create_medicine_mandatory_field,
                it.columnCount.error
            )
        }
        assertNull(stateValue().blisterPackCount.error)
        coVerify(exactly = 0) { medicineRepository.saveMedicine(any()) }
        assertFalse(createMedicineViewModel.navigationEvent.value)
    }

    @Test
    fun `save medicine on submit if all filled and not all packs same`() {
        createMedicineViewModel.updateBlisterPacksCount("4")
        createMedicineViewModel.updateName("Name")
        createMedicineViewModel.updateAllPacksIdentical()
        val blisterPackRow =
            BlisterPackRow(listOf(BlisterCavity.FILLED, BlisterCavity.FILLED, BlisterCavity.FILLED))
        val blisterPackRowLarge =
            BlisterPackRow(
                listOf(
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED,
                    BlisterCavity.FILLED
                )
            )
        stateValue().dimensions.forEachIndexed { index, _ ->
            createMedicineViewModel.updateColumnCount("3", index)
            createMedicineViewModel.updateRowCount("2", index)
        }
        createMedicineViewModel.updateColumnCount("4", 2)
        createMedicineViewModel.updateRowCount("1", 2)
        createMedicineViewModel.updateTimesPerDay("3")
        createMedicineViewModel.updateTimeSchedule(0, TIME_1)
        createMedicineViewModel.updateTimeSchedule(1, TIME_2)
        createMedicineViewModel.updateTimeSchedule(2, TIME_2)

        createMedicineViewModel.submit()

        coVerify {
            medicineRepository.saveMedicine(
                Medicine(
                    name = "Name",
                    blisterPacks = listOf(
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                        BlisterPack(listOf(blisterPackRowLarge)),
                        BlisterPack(listOf(blisterPackRow, blisterPackRow)),
                    ),
                    listOf(TIME_1, TIME_2, TIME_3)
                )
            )
        }
    }

    @Test
    fun `set navigation event on submit`() {
        createMedicineViewModel.updateRowCount("2")
        createMedicineViewModel.updateColumnCount("3")
        createMedicineViewModel.updateBlisterPacksCount("4")
        stateValue().dimensions.forEachIndexed { index, _ ->
            createMedicineViewModel.updateColumnCount("1", index)
            createMedicineViewModel.updateRowCount("1", index)
        }
        createMedicineViewModel.updateName("Name")

        createMedicineViewModel.submit()

        assertTrue { createMedicineViewModel.navigationEvent.value }
    }

    private fun stateValue() = createMedicineViewModel.state.value

    private companion object {
        val TIME_1 = LocalTime(10, 30)
        val TIME_2 = LocalTime(10, 30)
        val TIME_3 = LocalTime(10, 30)
    }
}
