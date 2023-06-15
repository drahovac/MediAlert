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

    data class BlisterPacks(val medicineId: Int?) : Destination {

        override fun destination(): String =
            detailDestination(medicineId?.toString() ?: MEDICINE_ID)

        companion object {
            fun detailDestination(id: String = MEDICINE_ID) =
                "${BlisterPacks::class.qualifiedName}/$id"
        }
    }

    object MedicineList : Destination

    object Calendar : Destination

    /**
     * String representation of destination used as route in navigation.
     */
    fun destination(): String = this::class.qualifiedName.orEmpty()

    private companion object {
        const val MEDICINE_ID = "{medicineId}"
    }
}
