package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MedicineDetailViewModel(
    medicineRepository: MedicineRepository,
    medicineId: Int? = null
) : BaseViewModel() {

    val state: StateFlow<Medicine?> = medicineRepository.getMedicineDetail(medicineId)
        .stateIn(
            scope, started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
}
