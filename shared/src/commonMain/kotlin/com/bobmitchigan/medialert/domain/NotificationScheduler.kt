package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalTime

/**
 * An interface for scheduling notifications.
 */
interface NotificationScheduler {

    /**
     * Schedules notifications for the given times and days.
     *
     * @param times The times for the notifications.
     * @param daysCount The number of days for the notifications including today - means 1 is for today only.
     */
    fun scheduleNotifications(times: List<LocalTime>, daysCount: Int)
}
