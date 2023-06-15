package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.MockMedicineRepository.Companion.PREVIEW_BLISTER_PACKS
import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.viewModel.state.CavityCoordinates
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class BlisterPacksViewModelTest {

    private val medicineRepository: MedicineRepository = mockk(relaxUnitFun = true)
    private lateinit var blisterPacksViewModel: BlisterPacksViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { medicineRepository.getMedicineDetail(DETAIL_ID) } returns flowOf(MEDICINE)
        blisterPacksViewModel = BlisterPacksViewModel(medicineRepository, DETAIL_ID)
    }

    @Test
    fun `fetch medicine detail by id on init`() {
        coVerify { medicineRepository.getMedicineDetail(DETAIL_ID) }
        assertEquals(MEDICINE, blisterPacksViewModel.state.value!!.medicine)
    }

    /**
    @see MockMedicineRepository PREVIEW_BLISTER_PACKS
     */
    @Test
    fun `set selected cavity`() {
        selectCavity()

        assertEquals(MEDICINE, blisterPacksViewModel.state.value!!.medicine)
        assertEquals("E", blisterPacksViewModel.state.value!!.selectedCavity!!.shortName)
        assertEquals(
            "EATEN(taken=2022-04-03T03:06)",
            blisterPacksViewModel.state.value!!.selectedCavity!!.toString()
        )
    }

    @Test
    fun `cleat selected cavity`() {
        selectCavity()

        blisterPacksViewModel.clearSelectedCavity()

        assertEquals(MEDICINE, blisterPacksViewModel.state.value!!.medicine)
        assertNull(blisterPacksViewModel.state.value!!.selectedCavity)
    }

    private fun selectCavity() {
        blisterPacksViewModel.selectCavity(
            CavityCoordinates(
                2, 1, 0
            )
        )
    }

    @Test
    fun `consume selected cavity`() {
        val slot = slot<Medicine>()
        blisterPacksViewModel.selectCavity(
            CavityCoordinates(0, 0, 0)
        )

        blisterPacksViewModel.consumeSelected()

        assertEquals(MEDICINE, blisterPacksViewModel.state.value!!.medicine)
        assertNull(blisterPacksViewModel.state.value!!.selectedCavity)
        coVerify { medicineRepository.updateMedicine(capture(slot)) }
        slot.captured.let {
            assertEquals(MEDICINE.name, it.name)
            assertEquals(MEDICINE.id, it.id)
            assertTrue { it.blisterPacks[0].rows[0].value[0] is BlisterCavity.EATEN }
        }
    }

    @Test
    fun `mark as lost selected cavity`() {
        val slot = slot<Medicine>()
        blisterPacksViewModel.selectCavity(
            CavityCoordinates(0, 0, 0)
        )

        blisterPacksViewModel.setLostSelected()

        assertEquals(MEDICINE, blisterPacksViewModel.state.value!!.medicine)
        assertNull(blisterPacksViewModel.state.value!!.selectedCavity)
        coVerify { medicineRepository.updateMedicine(capture(slot)) }
        slot.captured.let {
            assertEquals(MEDICINE.name, it.name)
            assertEquals(MEDICINE.id, it.id)
            assertEquals(
                BlisterCavity.LOST,
                it.blisterPacks[0].rows[0].value[0]
            )
        }
    }

    private companion object {
        const val DETAIL_ID = 23
        val MEDICINE = Medicine("Name", PREVIEW_BLISTER_PACKS, listOf(), dateTimeNow())
    }
}