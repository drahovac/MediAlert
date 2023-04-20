package com.bobmitchigan.medialert.viewModel

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
        assertEquals(MEDICINE, medicineDetailViewModel.state.value)
    }

    private companion object {
        const val DETAIL_ID = 23
        val MEDICINE = Medicine("Name", listOf(), listOf())
    }
}