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

fun List<BlisterPack>.filterAllCavities(predicate: (BlisterCavity) -> Boolean): List<BlisterCavity> {
    return this.flatMap { pack ->
        pack.rows.flatMap { it.value.filter(predicate) }
    }
}

/**
 * State of single blister cavity for medicine pill.
 */
sealed interface BlisterCavity {
    val shortName: String

    @JvmInline
    value class EATEN(val taken: LocalDateTime) : BlisterCavity {
        // pill was eaten
        override val shortName: String
            get() = "E"
    }

    object LOST : BlisterCavity { // pill was lost
        override val shortName: String
            get() = "L"
    }

    object FILLED : BlisterCavity { // pill still in cavity
        override val shortName: String
            get() = "F"
    }

    object NONE : BlisterCavity {
        // placeholder for uneven rows, no pill
        override val shortName: String
            get() = "N"
    }
}
