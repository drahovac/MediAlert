package com.bobmitchigan.medialert.viewModel

import kotlinx.datetime.LocalDateTime

/**
 * Date formatter - format based on system Locale
 */
expect fun LocalDateTime.toFormattedDateTime(): String
