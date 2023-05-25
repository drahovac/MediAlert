package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.BlisterPackAdapter
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.state.CalendarCoordinates
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class CalendarViewModelTest {

    private val medicineRepository: MedicineRepository = mockk()
    private lateinit var viewModel: CalendarViewModel
    private val blisterPacks = BlisterPackAdapter.deserializeBlisterPacks(
        "F.F.N.L,E2023-05-24T03:06.E2023-12-25T05:35.F.F;F.F,F.F.F.F,F.F.F.F;" +
                "F.F.N.L,E2023-05-26T12:00.E2023-12-25T03:06.F.F"
    )
    private val medicine1 = Medicine("Medicine 1", blisterPacks, listOf())
    private val medicine2 = Medicine("Medicine 2", blisterPacks, listOf())
    private val medicine3 = Medicine("Medicine 3", blisterPacks, listOf())
    private val date1 = LocalDate.parse("2023-05-24")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { medicineRepository.allItems } returns flowOf(
            listOf(
                medicine1,
                medicine2,
                medicine3
            )
        )
        viewModel = CalendarViewModel(medicineRepository)
    }

    @Suppress("MagicNumber")
    @Test
    fun `update state with filled cavities for week`() {
        viewModel.fetchWeekCells(date1)

        val dateTime1 = LocalDateTime.parse("2023-05-24T03:06")
        val dateTime2 = LocalDateTime.parse("2023-05-26T12:00")
        println(viewModel.state.value.events[date1])
        assertEquals(6, viewModel.state.value.events[date1]!!.size)
        assertEquals(
            MedicineEvent(dateTime1, medicine1),
            viewModel.state.value.events[date1]?.get(0)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine1),
            viewModel.state.value.events[date1]?.get(1)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine2),
            viewModel.state.value.events[date1]?.get(2)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine2),
            viewModel.state.value.events[date1]?.get(3)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine3),
            viewModel.state.value.events[date1]?.get(4)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine3),
            viewModel.state.value.events[date1]?.get(5)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine1),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[0]
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine2),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[1]
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine3),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[2]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine1),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[0]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine2),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[1]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine3),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[2]
        )
        assertEquals(
            emptyList(),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(25, 2))
        )
    }
}
