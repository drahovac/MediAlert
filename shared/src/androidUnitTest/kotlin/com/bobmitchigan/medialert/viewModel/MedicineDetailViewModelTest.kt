package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.NotificationScheduler
import com.bobmitchigan.medialert.domain.dateTimeNow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineDetailViewModelTest {

    private val medicineRepository: MedicineRepository = mockk(relaxUnitFun = true)
    private val scheduler: NotificationScheduler = mockk(relaxUnitFun = true)
    private val clock = object : Clock {
        override fun now(): Instant {
            return Instant.parse(INSTANT)
        }
    }
    private lateinit var medicineDetailViewModel: MedicineDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { medicineRepository.getMedicineDetail(DETAIL_ID) } returns flowOf(MEDICINE)
        medicineDetailViewModel =
            MedicineDetailViewModel(medicineRepository, scheduler, DETAIL_ID, clock)
    }

    @Test
    fun `fetch medicine detail by id on init`() =
        whileStateObserved(medicineDetailViewModel.state) {
            coVerify { medicineRepository.getMedicineDetail(DETAIL_ID) }
            assertEquals(MEDICINE, medicineDetailViewModel.state.value)
        }

    @Test
    fun `schedule notifications`() {
        whileStateObserved(medicineDetailViewModel.state) {
            medicineDetailViewModel.scheduleNotification()

            verify { scheduler.scheduleNotifications(schedule, 6) }
        }
    }

    private companion object {
        const val DETAIL_ID = 23
        val schedule: List<LocalTime> =
            listOf(
                LocalTime(1, 1),
                LocalTime(12, 10),
                LocalTime(15, 18)
            )
        val MEDICINE =
            Medicine("Name", MockMedicineRepository.PREVIEW_BLISTER_PACKS, schedule, dateTimeNow())
        const val INSTANT = "2022-04-03T03:06:00Z"
    }
}