package com.bobmitchigan.medialert.viewModel

data class CreateMedicineState(
    val name: String? = null,
    val blisterPackCount: Int? = null,
    val areAllPacksIdentical: Boolean = false,
    val rowCount: Int? = null,
    val columnCount: Int? = null,
)
