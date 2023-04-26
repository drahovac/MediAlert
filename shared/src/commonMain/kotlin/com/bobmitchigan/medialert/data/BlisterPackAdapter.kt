package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.BlisterPackRow
import kotlinx.datetime.toLocalDateTime

/**
 * Allows serialisation and deserialization of blister pack models to be saved in database
 */
object BlisterPackAdapter {

    private const val BLISTER_PACK_SEPARATOR = ";"
    private const val BLISTER_ROW_SEPARATOR = ","
    private const val BLISTER_CAVITY_SEPARATOR = "."

    fun List<BlisterPack>.serialize() =
        joinToString(separator = BLISTER_PACK_SEPARATOR) {
            it.serializeRows()
        }

    fun deserializeBlisterPacks(serialized: String): List<BlisterPack> {
        return serialized.split(BLISTER_PACK_SEPARATOR).map { it.deserializeRows() }
    }

    private fun BlisterPack.serializeRows() = rows.joinToString(
        BLISTER_ROW_SEPARATOR
    ) { it.serializeRow() }

    private fun String.deserializeRows(): BlisterPack {
        return BlisterPack(split(BLISTER_ROW_SEPARATOR).map { it.deserializeRow() })
    }

    private fun BlisterPackRow.serializeRow(): String =
        value.joinToString(BLISTER_CAVITY_SEPARATOR) {
            it.serializeCavity()
        }

    private fun String.deserializeRow(): BlisterPackRow =
        BlisterPackRow(this.split(BLISTER_CAVITY_SEPARATOR).map { it.deserializeCavity() })

    private fun BlisterCavity.serializeCavity(): String {
        return when (this) {
            is BlisterCavity.EATEN -> "E${this.taken.toString().substringBefore(".")}"
            BlisterCavity.FILLED -> "F"
            BlisterCavity.LOST -> "L"
            BlisterCavity.NONE -> "N"
        }
    }

    private fun String.deserializeCavity(): BlisterCavity {
        return when {
            this == "F" -> BlisterCavity.FILLED
            this == "L" -> BlisterCavity.LOST
            this == "N" -> BlisterCavity.NONE
            this.startsWith("E") -> BlisterCavity.EATEN(this.substring(1).toLocalDateTime())
            else -> throw IllegalArgumentException("Unknown cavity type: $this")
        }
    }
}
