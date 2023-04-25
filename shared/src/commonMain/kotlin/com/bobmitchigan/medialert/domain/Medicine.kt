package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalDateTime

/**
 * Represent medicine packaging.
 *
 * @property name name of medicine
 * @property blisterPacks list of blister packs with pill,
 * contains info about eaten and remaining pills
 * @property schedule list of date times when to take pills in future
 * @property id unique generated id
 */
data class Medicine(
    val name: String,
    val blisterPacks: List<BlisterPack>,
    val schedule: List<LocalDateTime>,
    val id: Int? = null
) {
    fun eatenCount(): Int = sumCavity(::increaseIfEaten)

    fun remainingCount(): Int = sumCavity(::increaseIfFilled)

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
