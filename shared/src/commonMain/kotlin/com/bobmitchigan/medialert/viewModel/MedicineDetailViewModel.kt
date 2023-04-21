package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val medicineId: Int? = null
) : MedicineDetailActions, BaseViewModel() {

    private val _state: MutableStateFlow<MedicineDetailState?> = MutableStateFlow(null)
    private var selectedCoordinates: CavityCoordinates? = null
    val state: StateFlow<MedicineDetailState?> = _state

    init {
        scope.launch {
            _state.value = medicineRepository.getMedicineDetail(medicineId)?.run {
                MedicineDetailState(
                    medicine = this,
                    selectedCavity = null
                )
            }
        }
    }

    override fun selectCavity(coordinates: CavityCoordinates) {
        selectedCoordinates = coordinates
        _state.update {
            it?.copy(selectedCavity = findCavity(it.medicine.blisterPacks, coordinates))
        }
    }

    override fun clearSelectedCavity() {
        _state.update { it?.copy(selectedCavity = null) }
    }

    private fun findCavity(
        blisterPacks: List<BlisterPack>,
        coordinates: CavityCoordinates
    ): BlisterCavity {
        return blisterPacks[coordinates.blisterPack].rows[coordinates.rowIndex].value[coordinates.cavityIndex]
    }
}

interface MedicineDetailActions {
    /*
    Select cavity and show dialog with actions based on cavity type
     */
    fun selectCavity(coordinates: CavityCoordinates)

    /*
    Clear cavity selected by user
     */
    fun clearSelectedCavity()
}
