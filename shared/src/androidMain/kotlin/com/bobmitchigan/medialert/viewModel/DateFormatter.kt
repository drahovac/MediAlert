package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * JVM implementation of date formatter.
 */
actual fun LocalDateTime.toFormattedDateTime(): String {
    return toJavaLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}