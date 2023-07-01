package com.bobmitchigan.medialert.viewModel

import androidx.lifecycle.SavedStateHandle
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.state.CreateMedicineState
import kotlinx.coroutines.flow.StateFlow

/**
 * A view model for creating or updating a medicine.
 *
 * @param savedStateHandle The SavedStateHandle to use to store the state of the view model.
 * @param medicineId The ID of the medicine to be updated, if any.
 * @param medicineRepository The MedicineRepository to use to save the medicine.
 */
class CreateMedicineSaveStateViewModel(
    private val savedStateHandle: SavedStateHandle,
    medicineId: Int? = null,
    medicineRepository: MedicineRepository,
) : CreateMedicineViewModel(medicineRepository) {

    override val state: StateFlow<CreateMedicineState> =
        savedStateHandle.getStateFlow(SAVED_STATE_KEY, CreateMedicineState(medicineId = medicineId))

    override fun updateState(update: (CreateMedicineState) -> CreateMedicineState) {
        savedStateHandle[SAVED_STATE_KEY] = update(state.value)
    }

    private companion object {
        const val SAVED_STATE_KEY = "SAVED_STATE_KEY"
    }
}
