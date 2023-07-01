package com.bobmitchigan.medialert.data

import kotlinx.datetime.LocalTime

object ScheduleAdapter {

    private const val SEPARATOR = ";"

    fun List<LocalTime>.serialize(): String {
        return joinToString(SEPARATOR) { it.toString() }
    }

    fun deserializeSchedule(value: String): List<LocalTime> {
        return runCatching {
            value.split(SEPARATOR).map { LocalTime.parse(it) }
        }.getOrElse { emptyList() }
    }
}
