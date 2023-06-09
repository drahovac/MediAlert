package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.state.BlisterPackDimension
import com.bobmitchigan.medialert.viewModel.state.CreateMedicineState
import com.bobmitchigan.medialert.viewModel.state.toInputState
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

open class CreateMedicineViewModel(
    private val medicineRepository: MedicineRepository,
    private val clock: Clock = Clock.System,
) : NavigationViewModel(), CreateMedicineActions {

    constructor(medicineRepository: MedicineRepository) : this(medicineRepository, Clock.System)

    private val _state = MutableStateFlow(CreateMedicineState())

    @NativeCoroutines
    open val state = _state.asStateFlow()

    override fun updateName(name: String) = updateState { it.copy(name = name.toInputState()) }

    override fun updateBlisterPacksCount(count: String) =
        updateState { state ->
            val countInt = count.toIntOrNull()
            state.copy(
                blisterPackCount = countInt.toInputState(),
                dimensions = countInt?.let {
                    List(countInt) { index ->
                        state.dimensions.getOrNull(index) ?: BlisterPackDimension()
                    }
                } ?: emptyList()
            )
        }

    override fun updateAllPacksIdentical() =
        updateState {
            it.copy(areAllPacksIdentical = it.areAllPacksIdentical.not())
        }

    override fun updateRowCount(count: String, packIndex: Int) {
        updateDimension(packIndex) { dimen ->
            dimen.copy(rowCount = count.toIntOrNull().toInputState())
        }
    }

    override fun updateColumnCount(count: String, packIndex: Int) {
        updateDimension(packIndex) {
            it.copy(columnCount = count.toIntOrNull().toInputState())
        }
    }

    override fun updateTimesPerDay(times: String) {
        updateState {
            it.copy(timesPerDay = times.toIntOrNull().toInputState(),
                timeSchedule = List(times.toIntOrNull() ?: 0) {
                    LocalTime(0, 0).toString().toInputState()
                })
        }
    }

    private fun updateDimension(
        packIndex: Int,
        dimUpdate: (BlisterPackDimension) -> BlisterPackDimension
    ) {
        updateState {
            it.copy(dimensions = it.dimensions.mapItemWithIndex(packIndex) { dimen ->
                dimUpdate(dimen)
            })
        }
    }

    override fun submit() {
        updateState { it.validate() }
        viewModelScope.coroutineScope.launch {
            state.value.toMedicine(clock)?.let {
                medicineRepository.saveMedicine(it)
                navigate()
            }
        }
    }

    override fun updateTimeSchedule(index: Int, hour: Int, minute: Int) {
        updateState {
            it.copy(
                timeSchedule = it.timeSchedule.mapItemWithIndex(index) {
                    LocalTime(
                        hour,
                        minute
                    ).toString().toInputState()
                }
            )
        }
    }

    /**
     * Copies list of items and maps item with index, allows copying previous item in update.
     */
    private fun <T> List<T>.mapItemWithIndex(index: Int, update: (T) -> T): List<T> {
        return this.mapIndexed { i, item ->
            if (i == index) {
                update(item)
            } else item
        }
    }

    protected open fun updateState(update: (CreateMedicineState) -> CreateMedicineState) {
        _state.update { update(it) }
    }
}

interface CreateMedicineActions {
    fun updateName(name: String)

    fun updateBlisterPacksCount(count: String)

    fun updateAllPacksIdentical()

    fun updateRowCount(count: String, packIndex: Int = 0)

    fun updateColumnCount(count: String, packIndex: Int = 0)

    fun updateTimesPerDay(times: String)

    fun submit()

    fun updateTimeSchedule(index: Int, hour: Int, minute: Int)
}

fun LocalTime.Companion.tryParse(value: String?): LocalTime? {
    return runCatching { value?.let { LocalTime.parse(it) } }.getOrNull()
}
