package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

/**
 * Represents blister pack with medicine pills.
 */
typealias  BlisterPack = List<BlisterCavity>

/**
 * State of single blister cavity for medicine pill.
 */
sealed interface BlisterCavity {
    @JvmInline
    value class EATEN(val taken: LocalDateTime) : BlisterCavity// pill was eaten
    object LOST : BlisterCavity // pill was lost
    object FILLED : BlisterCavity // pill still in cavity
    object NONE : BlisterCavity// placeholder for uneven rows, no pill
}
