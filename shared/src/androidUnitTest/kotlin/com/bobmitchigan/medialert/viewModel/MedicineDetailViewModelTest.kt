package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineDetailViewModelTest {

    private val medicineRepository: MedicineRepository = mockk(relaxUnitFun = true)
    private lateinit var medicineDetailViewModel: MedicineDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { medicineRepository.getMedicineDetail(DETAIL_ID) } returns flowOf(MEDICINE)
        medicineDetailViewModel = MedicineDetailViewModel(medicineRepository, DETAIL_ID)
    }

    @Test
    fun `fetch medicine detail by id on init`() =
        whileStateObserved(medicineDetailViewModel.state) {
            coVerify { medicineRepository.getMedicineDetail(DETAIL_ID) }
            assertEquals(MEDICINE, medicineDetailViewModel.state.value)
        }


    private companion object {
        const val DETAIL_ID = 23
        val MEDICINE =
            Medicine("Name", MockMedicineRepository.PREVIEW_BLISTER_PACKS, listOf(), dateTimeNow())
    }
}