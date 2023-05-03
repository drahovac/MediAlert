package com.bobmitchigan.medialert.data

import kotlinx.datetime.LocalDateTime

object ScheduleAdapter {

    private const val SEPARATOR = ";"

    fun List<LocalDateTime>.serialize(): String {
        return joinToString(SEPARATOR) { it.toString() }
    }

    fun deserialize(value: String): List<LocalDateTime> {
        return value.split(SEPARATOR).map { LocalDateTime.parse(it) }
    }
}
