package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicineListViewModel(
    private val medicineRepository: MedicineRepository
) : BaseViewModel() {

    val state: StateFlow<List<Medicine>> =
        medicineRepository.allItems.stateIn(
            scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )

    fun deleteMedicine(medicineId: Int) {
        scope.launch { medicineRepository.deleteMedicine(medicineId) }
    }
}
