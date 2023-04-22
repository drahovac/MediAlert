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
)
