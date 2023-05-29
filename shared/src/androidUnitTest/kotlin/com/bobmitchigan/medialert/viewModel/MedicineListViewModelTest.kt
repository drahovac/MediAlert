package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class MedicineListViewModelTest {

    private val medicineRepository: MedicineRepository = mockk(relaxUnitFun = true)
    private lateinit var medicineListViewModel: MedicineListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { medicineRepository.allItems } returns flowOf(listOf(MEDICINE_1, MEDICINE_2))
        medicineListViewModel = MedicineListViewModel(medicineRepository)
    }

    @Test
    fun `set medicines as state`() = runTest(UnconfinedTestDispatcher()) {
        val job = launch { medicineListViewModel.state.collect() }

        assertEquals(listOf(MEDICINE_1, MEDICINE_2), medicineListViewModel.state.value)
        job.cancel()
    }

    @Test
    fun `delete medicine`() {
        medicineListViewModel.deleteMedicine(MEDICINE_ID)

        coVerify { medicineRepository.deleteMedicine(MEDICINE_ID) }
    }

    private companion object {
        val MEDICINE_1 = Medicine(
            "Name1",
            listOf(),
            listOf(),
            dateTimeNow()
        )

        val MEDICINE_2 = Medicine(
            "Name2",
            listOf(),
            listOf(),
            dateTimeNow()
        )

        const val MEDICINE_ID = 2
    }
}