package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.NotificationScheduler
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.viewModel.state.plusDays
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val scheduler: NotificationScheduler,
    medicineId: Int? = null,
    private val clock: Clock = Clock.System
) : BaseViewModel() {

    val state: StateFlow<Medicine?> = medicineRepository.getMedicineDetail(medicineId)
        .stateIn(
            scope, started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun scheduleNotification() {
        state.value?.let { medicine ->
            val filledCount = medicine.filledPills().size
            val eatenTodayCount = medicine.eatenPills(dateTimeNow(clock).date).count()
            // plan only for pills in blister pack
            if (filledCount > 0) {
                val days = 1 + (filledCount - eatenTodayCount) / medicine.schedule.size
                val scheduleIndex = getLastNotificationScheduleIndex(filledCount, eatenTodayCount, medicine)
                scheduler.scheduleNotifications(medicine.schedule, days)
                scope.launch {
                    medicineRepository.updateMedicine(
                        medicine.copy(
                            lastScheduledNotificationTime = getLastNotDay(
                                dateTimeNow(clock).date.plusDays(days),
                                medicine.schedule[scheduleIndex]
                            )
                        )
                    )
                }
            }
        }
    }

    /**
     * Returns the index of the time in schedule of the last notification for the given medicine.
     *
     * @param filledCount The total number of pills that have been filled.
     * @param eatenTodayCount The number of pills that have been eaten today.
     * @param medicine The medicine object.
     * @return The index of the time in schedule of last notification.
     */
    private fun getLastNotificationScheduleIndex(
        filledCount: Int,
        eatenTodayCount: Int,
        medicine: Medicine
    ): Int {
        val lastDayPills = (filledCount - eatenTodayCount) % medicine.schedule.size
        return if (lastDayPills == 0) medicine.schedule.lastIndex else lastDayPills - 1
    }

    private fun getLastNotDay(day: LocalDate, dayTime: LocalTime): LocalDateTime {
        return day.atTime(dayTime)
    }
}
