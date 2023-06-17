package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.NotificationScheduler
import com.bobmitchigan.medialert.domain.dateTimeNow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock

class MedicineDetailViewModel(
    medicineRepository: MedicineRepository,
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
                scheduler.scheduleNotifications(medicine.schedule, days)
                // TODO save scheduled time to db
            }
        }
    }
}
