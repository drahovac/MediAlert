package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.data.BlisterPackAdapter
import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.EventType
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.viewModel.state.CalendarCoordinates
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CalendarViewModelTest {

    private val medicineRepository: MedicineRepository = mockk()
    private lateinit var viewModel: CalendarViewModel
    private val blisterPacks = BlisterPackAdapter.deserializeBlisterPacks(
        "F.F.N.L,E2023-05-24T03:06.E2023-12-25T05:35.F.F;F.F,F.F.F.F,F.F.F.F;" +
                "F.F.N.L,E2023-05-26T12:00.E2023-12-25T03:06.F.F"
    )
    private val firstPillDate = dateTimeNow()
    private val medicine1 = Medicine("Medicine 1", blisterPacks, listOf(), firstPillDate)
    private val medicine2 = Medicine("Medicine 2", blisterPacks, listOf(), firstPillDate)
    private val medicine3 = Medicine("Medicine 3", blisterPacks, listOf(), firstPillDate)
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
        assertEquals(6, viewModel.state.value.events[date1]!!.size)
        assertEquals(
            MedicineEvent(dateTime1, medicine1, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.events[date1]?.get(0)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine1, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.events[date1]?.get(1)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine2, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.events[date1]?.get(2)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine2, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.events[date1]?.get(3)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine3, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.events[date1]?.get(4)
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine3, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.events[date1]?.get(5)
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine1, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[0]
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine2, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[1]
        )
        assertEquals(
            MedicineEvent(dateTime1, medicine3, BlisterCavity.EATEN(dateTime1)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(6, 0))[2]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine1, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[0]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine2, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[1]
        )
        assertEquals(
            MedicineEvent(dateTime2, medicine3, BlisterCavity.EATEN(dateTime2)),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(24, 2))[2]
        )
        assertEquals(
            emptyList(),
            viewModel.state.value.getEvents(date1, CalendarCoordinates(25, 2))
        )
    }


    @Suppress("MagicNumber")
    @Test
    fun `generate missing pills`() {
        val today = LocalDateTime.parse("2023-06-02T12:30")
        val firstPillDate = LocalDateTime.parse("2023-05-31T03:06")
        val firstWeekDay = LocalDate.parse("2023-05-29")
        val blisterPacks = BlisterPackAdapter.deserializeBlisterPacks(
            "F.E2023-05-31T03:06.E2023-05-31T05:35.F.F.E2023-06-01T03:06.F.F.F.F.F"
        )
        val medicine = Medicine(
            "Medicine 1", blisterPacks, listOf(
                LocalTime(1, 0),
                LocalTime(4, 30),
                LocalTime(18, 0)
            ), firstPillDate
        )
        every { medicineRepository.allItems } returns flowOf(
            listOf(medicine)
        )
        viewModel = CalendarViewModel(medicineRepository, clock = object : Clock {
            override fun now(): Instant {
                return today.toInstant(TimeZone.currentSystemDefault())
            }
        })

        viewModel.fetchWeekCells(firstWeekDay)

        viewModel.state.value.events[firstWeekDay]!!.let { events ->
            // 2 eaten + 5 missing (1 for 31.5., 3 for 1.6., 2 for 2.6.)
            assertEquals(2 + 6, events.size)
            assertTrue { events[0].cavity is BlisterCavity.EATEN }
            assertTrue { events[1].cavity is BlisterCavity.EATEN }
            assertTrue { events[2].cavity is BlisterCavity.EATEN }
            assertEquals(LocalDateTime.parse("2023-05-31T18:00"), events[3].dateTime)
            assertTrue { events[3].eventType == EventType.MISSING }
            assertEquals(LocalDateTime.parse("2023-06-01T01:00"), events[4].dateTime)
            assertTrue { events[4].eventType == EventType.MISSING }
            assertEquals(LocalDateTime.parse("2023-06-01T18:00"), events[5].dateTime)
            assertTrue { events[5].eventType == EventType.MISSING }
            assertEquals(LocalDateTime.parse("2023-06-02T01:00"), events[6].dateTime)
            assertTrue { events[6].eventType == EventType.MISSING }
            assertEquals(LocalDateTime.parse("2023-06-02T04:30"), events[7].dateTime)
            assertTrue { events[7].eventType == EventType.MISSING }
        }
    }

    @Test
    fun `should set selected events`() {
        val events = listOf(
            MedicineEvent(
                LocalDateTime.parse("2023-05-26T12:00"),
                medicine3,
                BlisterCavity.FILLED
            )
        )

        viewModel.selectCell(events)

        assertEquals(events, viewModel.state.value.selectedEvents)
    }

    @Test
    fun `dismiss selected events`() {
        val events = listOf(
            MedicineEvent(
                LocalDateTime.parse("2023-05-26T12:00"),
                medicine3,
                BlisterCavity.FILLED
            )
        )
        viewModel.selectCell(events)

        viewModel.dismissSelected()

        assertTrue { viewModel.state.value.selectedEvents.isEmpty() }
    }
}
