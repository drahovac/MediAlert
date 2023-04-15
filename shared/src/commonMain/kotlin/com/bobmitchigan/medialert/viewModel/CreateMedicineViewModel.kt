package com.bobmitchigan.medialert.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateMedicineViewModel : BaseViewModel(), CreateMedicineActions {

    private val _state = MutableStateFlow(CreateMedicineState())
    val state = _state.asStateFlow()

    override fun updateName(name: String) = _state.update { it.copy(name = name) }

    override fun updateBlisterPacksCount(count: String) =
        _state.update { it.copy(blisterPackCount = count.toIntOrNull()) }

    override fun updateAllPacksIdentical() =
        _state.update { it.copy(areAllPacksIdentical = !state.value.areAllPacksIdentical) }

    override fun updateRowCount(count: String) {
        _state.update { it.copy(rowCount = count.toIntOrNull()) }
    }

    override fun updateColumnCount(count: String) {
        _state.update { it.copy(columnCount = count.toIntOrNull()) }
    }
}

interface CreateMedicineActions {
    fun updateName(name: String)

    fun updateBlisterPacksCount(count: String)

    fun updateAllPacksIdentical()

    fun updateRowCount(count: String)

    fun updateColumnCount(count: String)
}
