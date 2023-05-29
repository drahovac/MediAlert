package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.createNewBlisterPack
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.viewModel.CommonSerializable
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.serialization.Serializable

@Serializable
data class CreateMedicineState(
    val name: InputState<String> = InputState(),
    val blisterPackCount: InputState<Int> = InputState(),
    val areAllPacksIdentical: Boolean = true,
    val dimensions: List<BlisterPackDimension> = emptyList(),
    val timesPerDay: InputState<Int> = InputState(),
    val timeSchedule: List<InputState<LocalTime>> = emptyList()
) : CommonSerializable {

    fun toMedicine(clock: Clock) = runCatching {
        val schedule = timeSchedule.mapNotNull { it.value }.sorted()
        Medicine(
            name = name.value!!,
            blisterPacks = if (areAllPacksIdentical) mapFirstDimension() else mapDimensionsToFilledPacks(),
            schedule = schedule,
            firstPillDateTime = dateTimeNow(clock).withTime(schedule.firstOrNull()),
        )
    }.getOrNull()

    private fun mapFirstDimension(): List<BlisterPack> {
        return (0 until blisterPackCount.value!!).map {
            createNewBlisterPack(
                rows = dimensions.first().rowCount.value!!,
                dimensions.first().columnCount.value!!
            )
        }.apply { require(isNotEmpty()) }
    }

    private fun mapDimensionsToFilledPacks() = dimensions.map {
        createNewBlisterPack(rows = it.rowCount.value!!, columns = it.columnCount.value!!)
    }.apply { require(isNotEmpty() && isValid()) }

    fun validate() = CreateMedicineState(
        name = name.copy(error = MR.strings.create_medicine_mandatory_field.takeIf { name.value.isNullOrEmpty() }),
        blisterPackCount = blisterPackCount.copy(
            error = MR.strings.create_medicine_mandatory_field.takeIf {
                blisterPackCount.value.isNullOrZero()
            }),
        areAllPacksIdentical = areAllPacksIdentical,
        dimensions = dimensions.map { dimension ->
            BlisterPackDimension(
                dimension.rowCount.copy(
                    error = MR.strings.create_medicine_mandatory_field.takeIf {
                        dimension.rowCount.value.isNullOrZero()
                    }),
                dimension.columnCount.copy(error = MR.strings.create_medicine_mandatory_field.takeIf {
                    dimension.columnCount.value.isNullOrZero()
                })
            )
        },
        timesPerDay = timesPerDay,
        timeSchedule = timeSchedule,
    )

    companion object {
        fun initCreateMedicineState(): CreateMedicineState {
            return CreateMedicineState()
        }
    }
}

private fun LocalDateTime.withTime(time: LocalTime?): LocalDateTime {
    return time?.let { this.date.atTime(time) } ?: this
}

private fun List<BlisterPack>.isValid(): Boolean {
    return all { pack -> pack.rows.isNotEmpty() && pack.rows.all { it.value.isNotEmpty() } }
}

private fun Int?.isNullOrZero() = this == null || this == 0

@Serializable
data class BlisterPackDimension(
    val rowCount: InputState<Int> = InputState(),
    val columnCount: InputState<Int> = InputState(),
) : CommonSerializable
