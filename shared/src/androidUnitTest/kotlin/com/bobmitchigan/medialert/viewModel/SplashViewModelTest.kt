@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.InitialDestination
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class SplashViewModelTest {

    private val medicine = Medicine("Medicine", emptyList(), emptyList())
    private var allItemsFlow: MutableStateFlow<List<Medicine>> = MutableStateFlow(emptyList())
    private val repository: MedicineRepository = object : MedicineRepository {
        override val allItems: Flow<List<Medicine>> = allItemsFlow
        override suspend fun saveMedicine(medicine: Medicine) {
        }

        override suspend fun getMedicineDetail(id: Int?): Medicine? {
            return null
        }
    }
    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `when medicines empty return create new destination`() {
        splashViewModel = SplashViewModel(repository)

        assertEquals(InitialDestination.CreateMedicine, splashViewModel.nextDestination.value)
    }

    @Test
    fun `when single medicine return single medicine detail`() {
        allItemsFlow.update { listOf(medicine) }

        splashViewModel = SplashViewModel(repository)

        assertEquals(
            InitialDestination.SingleMedicine::class.java,
            splashViewModel.nextDestination.value!!::class.java
        )
    }

    @Test
    fun `when multiple medicines return medicine list destination`() {
        allItemsFlow.update { listOf(medicine, medicine) }

        splashViewModel = SplashViewModel(repository)

        assertEquals(InitialDestination.MedicineList, splashViewModel.nextDestination.value)
    }
}