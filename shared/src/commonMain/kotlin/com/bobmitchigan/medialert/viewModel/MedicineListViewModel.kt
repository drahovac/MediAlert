package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MedicineListViewModel(
    medicineRepository: MedicineRepository
) : BaseViewModel() {

    val state: StateFlow<List<Medicine>> =
        medicineRepository.allItems.stateIn(
            scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )
}
