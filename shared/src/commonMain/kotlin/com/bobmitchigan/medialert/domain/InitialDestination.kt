package com.bobmitchigan.medialert.domain

/**
 * Initial destination where to navigate after splashcreen.
 */
sealed interface InitialDestination {

    object CreateMedicine : InitialDestination

    data class SingleMedicine(val medicineId: Int?) : InitialDestination {

        override fun destination(): String =
            detailDestination(medicineId?.toString() ?: MEDICINE_ID)

        companion object {
            fun detailDestination(id: String = MEDICINE_ID) =
                "${SingleMedicine::class.qualifiedName}/$id"
        }
    }

    object MedicineList : InitialDestination

    fun destination(): String = this::class.qualifiedName.orEmpty()

    private companion object {
        const val MEDICINE_ID = "medicineId"
    }
}
