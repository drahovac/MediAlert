package com.bobmitchigan.medialert.domain

/**
 * Destination where to navigate.
 */
sealed interface Destination {

    object CreateMedicine : Destination

    data class SingleMedicine(val medicineId: Int?) : Destination {

        override fun destination(): String =
            detailDestination(medicineId?.toString() ?: MEDICINE_ID)

        companion object {
            fun detailDestination(id: String = MEDICINE_ID) =
                "${SingleMedicine::class.qualifiedName}/$id"
        }
    }

    object MedicineList : Destination

    fun destination(): String = this::class.qualifiedName.orEmpty()

    private companion object {
        const val MEDICINE_ID = "{medicineId}"
    }
}