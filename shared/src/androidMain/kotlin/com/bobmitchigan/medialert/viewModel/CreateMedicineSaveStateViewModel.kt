package com.bobmitchigan.medialert.viewModel

import androidx.lifecycle.SavedStateHandle
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.state.CreateMedicineState
import kotlinx.coroutines.flow.StateFlow

class CreateMedicineSaveStateViewModel(
    private val savedStateHandle: SavedStateHandle,
    medicineRepository: MedicineRepository,
) : CreateMedicineViewModel(medicineRepository) {

    override val state: StateFlow<CreateMedicineState> =
        savedStateHandle.getStateFlow(SAVED_STATE_KEY, CreateMedicineState())

    override fun updateState(update: (CreateMedicineState) -> CreateMedicineState) {
        savedStateHandle[SAVED_STATE_KEY] = update(state.value)
    }

    private companion object {
        const val SAVED_STATE_KEY = "SAVED_STATE_KEY"
    }
}
