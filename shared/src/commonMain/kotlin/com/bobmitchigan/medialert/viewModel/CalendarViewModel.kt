package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.EventType
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineEvent
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.domain.filterAllCavities
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import com.bobmitchigan.medialert.viewModel.state.plusDays
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.math.abs

class CalendarViewModel(
    repository: MedicineRepository,
    private val clock: Clock = Clock.System
) : BaseViewModel(), CalendarActions {

    private val _state: MutableStateFlow<CalendarState> =
        MutableStateFlow(
            CalendarState()
        )
    val state = _state.asStateFlow()
    private var medicines: List<Medicine> = emptyList()

    // wait to fetch medicines before mapping events
    private var firstFetch = Job()

    init {
        firstFetch.start()
        scope.launch {
            repository.allItems.collect {
                medicines = it
                _state.update { CalendarState() }
                firstFetch.complete()
            }
        }
    }

    override fun selectCell(events: List<MedicineEvent>) {
        _state.update { it.copy(selectedEvents = events) }
    }

    override fun fetchWeekCells(startingWeekDay: LocalDate) {
        if (!state.value.events.containsKey(startingWeekDay)) {
            scope.launch {
                firstFetch.join()
                _state.update {
                    it.copy(
                        events = it.events.addEvent(startingWeekDay, getEvents(startingWeekDay))
                    )
                }
            }
        }
    }

    override fun dismissSelected() {
        _state.update { it.copy(selectedEvents = emptyList()) }
    }

    /**
    Retrieves a list of medicine events for a week given starting week day.
    @param startingWeekDay The starting week day from which to retrieve the events.
    @return A list of [MedicineEvent] objects representing the medicine events.
     */
    private fun getEvents(startingWeekDay: LocalDate): List<MedicineEvent> {
        return mutableListOf<MedicineEvent>().apply {
            addAll(generateEatenEvents(startingWeekDay))
            addAll(generateMissingEvents(startingWeekDay))
            addAll(generatePlannedFutureEvents(startingWeekDay))
        }
    }

    private fun generatePlannedFutureEvents(startingWeekDay: LocalDate): List<MedicineEvent> {
        return medicines.flatMap { medicine ->
            val dateNow = dateTimeNow(clock).date
            val start = listOf(dateNow, startingWeekDay).max()
            val result = mutableListOf<MedicineEvent>()
            val endOfWeek = startingWeekDay.plus(1, DateTimeUnit.WEEK)

            // check if there are filled pills for planned events
            var remainingPills = medicine.filledPills().size
            for (i in 0..start.daysUntil(endOfWeek)) {
                if (remainingPills <= 0) continue
                getFreeSlotsForDay(
                    medicine,
                    day = start.plusDays(i),
                    dateNow = dateTimeNow(clock)
                ).take(
                    remainingPills
                ).let { slots ->
                    remainingPills -= slots.size
                    result.addAll(slots.map {
                        MedicineEvent(it, medicine, null, EventType.PLANNED)
                    })
                }
            }

            result
        }
    }

    private fun getFreeSlotsForDay(
        medicine: Medicine,
        dateNow: LocalDateTime,
        day: LocalDate
    ): List<LocalDateTime> {
        return medicine.eatenPills(day).let { eaten ->
            val schedule = medicine.schedule
            when {
                eaten.isEmpty() -> schedule.filter { day != dateNow.date || it > dateNow.time }
                    .map {
                        day.atTime(it)
                    }

                eaten.size == schedule.size -> emptyList()
                else -> findMissingEvents(
                    schedule,
                    eaten,
                    timeUntil = null,
                    timeFrom = dateTimeNow(clock).time.takeIf { dateNow.date == day }).map {
                    day.atTime(it)
                }
            }
        }
    }

    /**
     * Generates a list of missing medicine events for the given starting week day.
     *
     * @param startingWeekDay The starting week day of the period to generate missing events for.
     * @return A list of missing medicine events.
     */
    private fun generateMissingEvents(startingWeekDay: LocalDate): List<MedicineEvent> {
        return medicines.flatMap { medicine ->
            // no missing medicine possible if first pill after current week
            if (medicine.remainingCount() == 0 || medicine.firstPillDateTime.date > startingWeekDay.plus(
                    1,
                    DateTimeUnit.WEEK
                )
            ) return@flatMap emptyList()
            val dateNow = dateTimeNow(clock).date
            // Find the start date of the period - either start of week
            // if first pill was taken before this week or fist pill date
            val start = maxOf(medicine.firstPillDateTime, startingWeekDay.atTime(0, 0))
            // how many days with missing events
            val daysCount = start.date.daysUntil(dateNow)
            val result = mutableListOf<MedicineEvent>()
            for (i in 0..daysCount) {
                val eaten = medicine.eatenPills(start.date.plusDays(i))
                if (eaten.size < medicine.schedule.size) {
                    result.addAll(
                        findMissingEvents(
                            medicine.schedule,
                            eaten,
                            dateTimeNow(clock).time.takeIf { dateNow == start.date.plusDays(i) },
                            null
                        ).mapToMissingEvents(start.date.plusDays(i), medicine)
                    )
                }
            }
            result
        }
    }

    /**
     * Find missing events for current day.
     * If search should be only to concrete time time until is not null.
     * If search should be only from concrete time time from is not null.
     */
    private fun findMissingEvents(
        schedule: List<LocalTime>,
        eaten: List<BlisterCavity.EATEN>,
        timeUntil: LocalTime?,
        timeFrom: LocalTime?,
    ): List<LocalTime> {
        schedule.filter { (timeUntil == null || it < timeUntil) && (timeFrom == null || it > timeFrom) }
            .toMutableList().let { mutated ->
                eaten.forEach { eaten ->
                    mutated.sortBy { abs(it.toSecondOfDay() - eaten.taken.time.toSecondOfDay()) }
                    mutated.removeFirstOrNull()
                }
                return mutated
            }
    }

    private fun List<LocalTime>.mapToMissingEvents(date: LocalDate, medicine: Medicine) =
        map { timeSlot ->
            MedicineEvent(
                date.atTime(timeSlot),
                medicine,
                null,
                EventType.MISSING
            )
        }

    private fun generateEatenEvents(startingWeekDay: LocalDate): List<MedicineEvent> {
        val start = startingWeekDay.atStartOfDayIn(TimeZone.currentSystemDefault())
        val end = startingWeekDay.plus(DAYS_IN_WEEK, DateTimeUnit.DAY)
            .atTime(LAST_DAY_HOUR, LAT_MIN_SEC, LAT_MIN_SEC)
            .toInstant(TimeZone.currentSystemDefault())
        return medicines.flatMap { medicine ->
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
    return MedicineEvent(taken, medicine, this)
}

private fun Map<LocalDate, List<MedicineEvent>>.addEvent(
    startingWeekDay: LocalDate,
    events: List<MedicineEvent>
): Map<LocalDate, List<MedicineEvent>> {
    return HashMap(this).apply { put(startingWeekDay, events) }
}

interface CalendarActions {

    fun selectCell(events: List<MedicineEvent>)

    fun fetchWeekCells(startingWeekDay: LocalDate)

    fun dismissSelected()
}
