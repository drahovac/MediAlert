package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.filterAllCavities
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

class CalendarViewModel(repository: MedicineRepository) : BaseViewModel(), CalendarActions {

    private val _state: MutableStateFlow<CalendarState> =
        MutableStateFlow(
            CalendarState()
        )
    val state = _state.asStateFlow()
    private var medicines: List<Medicine> = emptyList()

    init {
        scope.launch {
            repository.allItems.collect {
                medicines = it
                _state.update { CalendarState() }
            }
        }
    }

    override fun selectCell(row: Int, column: Int) {
        println("Index $row")
    }

    override fun fetchWeekCells(startingWeekDay: LocalDate) {
        if (!state.value.events.containsKey(startingWeekDay)) {
            _state.update {
                it.copy(
                    events = it.events.addEvent(startingWeekDay, getEvents(startingWeekDay))
                )
            }
        }
    }

    /**
    Retrieves a list of medicine events for a week given starting week day.
    @param startingWeekDay The starting week day from which to retrieve the events.
    @return A list of [MedicineEvent] objects representing the medicine events.
     */
    private fun getEvents(startingWeekDay: LocalDate): List<MedicineEvent> {
        val start = startingWeekDay.atStartOfDayIn(TimeZone.currentSystemDefault())
        val end = startingWeekDay.plus(DAYS_IN_WEEK, DateTimeUnit.DAY)
            .atTime(LAST_DAY_HOUR, LAT_MIN_SEC, LAT_MIN_SEC)
            .toInstant(TimeZone.currentSystemDefault())
        return medicines.flatMap { medicine ->
            println(medicine)
            medicine.blisterPacks.filterAllCavities { cavity ->
                cavity is BlisterCavity.EATEN && cavity.taken.toInstant(
                    TimeZone.currentSystemDefault()
                ) in start..end
            }.map { (it as BlisterCavity.EATEN).toEvent(medicine) }
        }
    }
}

private const val DAYS_IN_WEEK = 7
private const val LAST_DAY_HOUR = 23
private const val LAT_MIN_SEC = 59

private fun BlisterCavity.EATEN.toEvent(medicine: Medicine): MedicineEvent {
    return MedicineEvent(taken, medicine)
}

private fun Map<LocalDate, List<MedicineEvent>>.addEvent(
    startingWeekDay: LocalDate,
    events: List<MedicineEvent>
): Map<LocalDate, List<MedicineEvent>> {
    return HashMap(this).apply { put(startingWeekDay, events) }
}

interface CalendarActions {

    fun selectCell(row: Int, column: Int)

    fun fetchWeekCells(startingWeekDay: LocalDate)
}
