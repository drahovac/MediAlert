package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.domain.NotificationScheduler
import kotlinx.datetime.LocalTime

actual class NotificationSchedulerImpl : NotificationScheduler {
    override fun scheduleNotifications(times: List<LocalTime>, daysCount: Int) {
        // TODO alarm manager
    }
}
