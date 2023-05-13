package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalDateTime

/**
 * A data class representing a medicine event in future when user should take pill.
 *
 * @property dateTime The date and time of the event.
 * @property medicine The medicine should be taken.
 */
data class MedicineEvent(
    val dateTime: LocalDateTime,
    val medicine: Medicine,
)
