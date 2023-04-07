package com.bobmitchigan.medialert.domain

/**
 * Represents blister pack with medicine pills.
 *
 * @property capacity maxCapacity of pills
 * @property indexes of used
 */
data class BlisterPack(
    val capacity: Int,
    val used: List<Int>,
)

/**
 * State of single blister cavity,
 */
enum class BlisterCavity {
    EMPTY, FILLED, NONE
}