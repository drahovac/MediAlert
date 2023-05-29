package com.bobmitchigan.medialert.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun dateTimeNow(clock: Clock = Clock.System) = clock.now().toLocalDateTime(
    TimeZone.currentSystemDefault()
)
