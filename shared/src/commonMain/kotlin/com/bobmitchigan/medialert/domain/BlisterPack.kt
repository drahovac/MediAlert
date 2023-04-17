package com.bobmitchigan.medialert.domain

import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

/**
 * Represents blister pack with medicine pills.
 */
@JvmInline
value class BlisterPack(val rows: List<BlisterPackRow>)

@JvmInline
value class BlisterPackRow(val value: List<BlisterCavity>)


fun createNewBlisterPack(rows: Int, columns: Int): BlisterPack = BlisterPack(List(rows) {
    createNewBlisterPackRow(columns)
})

fun createNewBlisterPackRow(columns: Int): BlisterPackRow = BlisterPackRow(List(columns) {
    BlisterCavity.FILLED
})

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
