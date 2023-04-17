package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateMedicineViewModel(
    private val medicineRepository: MedicineRepository
) : BaseViewModel(), CreateMedicineActions {

    private val _state = MutableStateFlow(CreateMedicineState())
    val state = _state.asStateFlow()

    override fun updateName(name: String) = _state.update { it.copy(name = name.toInputState()) }

    override fun updateBlisterPacksCount(count: String) =
        _state.update { it.copy(blisterPackCount = count.toIntOrNull().toInputState()) }

    override fun updateAllPacksIdentical() =
        _state.update {
            it.copy(areAllPacksIdentical = it.areAllPacksIdentical.not())
        }

    override fun updateRowCount(count: String) {
        _state.update { it.copy(rowCount = count.toIntOrNull().toInputState()) }
    }

    override fun updateColumnCount(count: String) {
        _state.update { it.copy(columnCount = count.toIntOrNull().toInputState()) }
    }

    override fun submit() {
        _state.update { it.validate() }
        scope.launch {
            _state.value.toMedicine()?.let { medicineRepository.saveMedicine(it) }
        }
    }
}

interface CreateMedicineActions {
    fun updateName(name: String)

    fun updateBlisterPacksCount(count: String)

    fun updateAllPacksIdentical()

    fun updateRowCount(count: String)

    fun updateColumnCount(count: String)

    fun submit()
}
