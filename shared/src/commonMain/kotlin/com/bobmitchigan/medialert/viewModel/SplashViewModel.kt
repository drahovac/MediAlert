package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: MedicineRepository) : BaseViewModel() {

    private var _nextDestination = MutableStateFlow<Destination?>(null)
    var nextDestination: StateFlow<Destination?> = _nextDestination

    init {
        scope.launch {
            repository.allItems.first().let { medicineList ->
                _nextDestination.update {
                    when {
                        medicineList.isEmpty() -> Destination.CreateMedicine
                        medicineList.size == 1 -> Destination.SingleMedicine(null)
                        else -> Destination.MedicineList
                    }
                }
            }
        }
    }
}
