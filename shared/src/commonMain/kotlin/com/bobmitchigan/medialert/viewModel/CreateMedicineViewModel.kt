package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateMedicineViewModel(
    private val medicineRepository: MedicineRepository
) : NavigationViewModel(), CreateMedicineActions {

    private val _state = MutableStateFlow(CreateMedicineState())
    val state = _state.asStateFlow()

    override fun updateName(name: String) = _state.update { it.copy(name = name.toInputState()) }

    override fun updateBlisterPacksCount(count: String) =
        _state.update { state ->
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
        _state.update {
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

    private fun updateDimension(
        packIndex: Int,
        dimUpdate: (BlisterPackDimension) -> BlisterPackDimension
    ) {
        _state.update {
            it.copy(dimensions = it.dimensions.mapItemWithIndex(packIndex) { dimen ->
                dimUpdate(dimen)
            })
        }
    }

    override fun submit() {
        _state.update { it.validate() }
        scope.launch {
            _state.value.toMedicine()?.let {
                medicineRepository.saveMedicine(it)
                navigate()
            }
        }
    }

    /**
     * Copies list of items and maps item with index, allows copying previous item in update.
     */
    fun <T> List<T>.mapItemWithIndex(index: Int, update: (T) -> T): List<T> {
        return this.mapIndexed { i, item ->
            if (i == index) {
                update(item)
            } else item
        }
    }
}

interface CreateMedicineActions {
    fun updateName(name: String)

    fun updateBlisterPacksCount(count: String)

    fun updateAllPacksIdentical()

    fun updateRowCount(count: String, packIndex: Int = 0)

    fun updateColumnCount(count: String, packIndex: Int = 0)

    fun submit()
}
