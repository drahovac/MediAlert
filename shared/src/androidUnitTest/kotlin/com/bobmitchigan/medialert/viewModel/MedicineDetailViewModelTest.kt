package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.MockMedicineRepository.Companion.PREVIEW_BLISTER_PACKS
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class MedicineDetailViewModelTest {

    private val medicineRepository: MedicineRepository = mockk()
    private lateinit var medicineDetailViewModel: MedicineDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { medicineRepository.getMedicineDetail(DETAIL_ID) } returns MEDICINE
        medicineDetailViewModel = MedicineDetailViewModel(medicineRepository, DETAIL_ID)
    }

    @Test
    fun `fetch medicine detail by id on init`() {
        coVerify { medicineRepository.getMedicineDetail(DETAIL_ID) }
        assertEquals(MEDICINE, medicineDetailViewModel.state.value!!.medicine)
    }

    /**
    @see MockMedicineRepository PREVIEW_BLISTER_PACKS
     */
    @Test
    fun `set selected cavity`() {
        selectCavity()

        assertEquals(MEDICINE, medicineDetailViewModel.state.value!!.medicine)
        assertEquals("E", medicineDetailViewModel.state.value!!.selectedCavity!!.shortName)
        assertEquals(
            "EATEN(taken=2022-04-03T03:06)",
            medicineDetailViewModel.state.value!!.selectedCavity!!.toString()
        )
    }

    @Test
    fun `cleat selected cavity`() {
        selectCavity()

        medicineDetailViewModel.clearSelectedCavity()

        assertEquals(MEDICINE, medicineDetailViewModel.state.value!!.medicine)
        assertNull(medicineDetailViewModel.state.value!!.selectedCavity)
    }

    private fun selectCavity() {
        medicineDetailViewModel.selectCavity(
            CavityCoordinates(
                2, 1, 0
            )
        )
    }

    private companion object {
        const val DETAIL_ID = 23
        val MEDICINE = Medicine("Name", PREVIEW_BLISTER_PACKS, listOf())
    }
}