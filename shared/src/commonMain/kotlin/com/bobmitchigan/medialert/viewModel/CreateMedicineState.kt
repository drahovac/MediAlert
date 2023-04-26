package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.createNewBlisterPack

data class CreateMedicineState(
    val name: InputState<String> = InputState(),
    val blisterPackCount: InputState<Int> = InputState(),
    val areAllPacksIdentical: Boolean = true,
    val rowCount: InputState<Int> = InputState(),
    val columnCount: InputState<Int> = InputState(),
) {
    fun toMedicine() = runCatching {
        Medicine(
            name = name.value!!,
            blisterPacks = List(blisterPackCount.value!!) {
                createNewBlisterPack(rowCount.value!!, columnCount.value!!)
            },
            schedule = listOf()
        )
    }.getOrNull()

    fun validate() = CreateMedicineState(
        name.copy(error = MR.strings.create_medicine_mandatory_field.takeIf { name.value.isNullOrEmpty() }),
        blisterPackCount.copy(
            error = MR.strings.create_medicine_mandatory_field.takeIf {
                blisterPackCount.value == null
            }),
        areAllPacksIdentical,
        rowCount.copy(error = MR.strings.create_medicine_mandatory_field.takeIf {
            rowCount.value == null
        }),
        columnCount.copy(error = MR.strings.create_medicine_mandatory_field.takeIf {
            columnCount.value == null
        })
    )
}
