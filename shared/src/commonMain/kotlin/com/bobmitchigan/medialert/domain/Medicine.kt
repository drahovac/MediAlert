package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * Represent medicine packaging.
 *
 * @property name name of medicine
 * @property blisterPacks list of blister packs with pill,
 * contains info about eaten and remaining pills
 * @property schedule list of date times when to take pills in future
 * @property firstPillDateTime date time when first pill was taken
 * @property id unique generated id
 * @property lastScheduledNotificationTime time of last scheduled notification
 */
data class Medicine(
    val name: String,
    val blisterPacks: List<BlisterPack>,
    val schedule: List<LocalTime>,
    val firstPillDateTime: LocalDateTime,
    val id: Int? = null,
    val lastScheduledNotificationTime: LocalDateTime? = null,
) {
    /**
     * Returns linear list of blister cavities.
     */
    private val blisterCavities: List<BlisterCavity> =
        blisterPacks.flatMap { it.rows.flatMap { row -> row.value } }

    fun eatenCount(): Int = sumCavity(::increaseIfEaten)

    fun remainingCount(): Int = sumCavity(::increaseIfFilled)

    /**
     * Return all eaten pills during day.
     */
    fun eatenPills(day: LocalDate): List<BlisterCavity.EATEN> {
        return blisterCavities.filterIsInstance<BlisterCavity.EATEN>().filter {
            it.taken.date == day
        }
    }

    /**
     * Return all filled pills.
     */
    fun filledPills(): List<BlisterCavity.FILLED> {
        return blisterCavities.filterIsInstance<BlisterCavity.FILLED>()
    }

    private fun sumCavity(increase: (BlisterCavity) -> Int): Int {
        return blisterPacks.sumOf { blisterPack ->
            blisterPack.rows.sumOf { row ->
                row.value.sumOf(increase)
            }
        }
    }

    private fun increaseIfEaten(cavity: BlisterCavity): Int =
        if (cavity is BlisterCavity.EATEN) 1 else 0

    private fun increaseIfFilled(cavity: BlisterCavity): Int =
        if (cavity is BlisterCavity.FILLED) 1 else 0
}
