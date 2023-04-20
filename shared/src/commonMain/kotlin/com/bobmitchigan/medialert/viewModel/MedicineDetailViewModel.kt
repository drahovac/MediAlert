package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val medicineId: Int? = null
) : BaseViewModel() {

    private val _state: MutableStateFlow<Medicine?> = MutableStateFlow(null)
    val state: StateFlow<Medicine?> = _state

    init {
        scope.launch {
            _state.value = medicineRepository.getMedicineDetail(medicineId)
        }
    }
}
