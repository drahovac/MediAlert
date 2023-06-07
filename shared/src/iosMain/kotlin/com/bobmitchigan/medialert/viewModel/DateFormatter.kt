package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.localTimeZone

/**
 * iOS implementation of date formatter.
 */
actual fun LocalDateTime.toFormattedDateTime(): String {
    val dateFormatter = NSDateFormatter().apply {
        timeZone = NSTimeZone.localTimeZone
        locale = NSLocale.autoupdatingCurrentLocale
        dateStyle = NSDateFormatterMediumStyle
        timeStyle = NSDateFormatterShortStyle
    }

    return dateFormatter.stringFromDate(this.toInstant(TimeZone.currentSystemDefault()).toNSDate())
}
