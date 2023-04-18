package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.InitialDestination
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: MedicineRepository) : BaseViewModel() {

    private var _nextDestination = MutableStateFlow<InitialDestination?>(null)
    var nextDestination: StateFlow<InitialDestination?> = _nextDestination

    init {
        scope.launch {
            repository.allItems.first().let { medicineList ->
                _nextDestination.update {
                    when {
                        medicineList.isEmpty() -> InitialDestination.CREATE_MEDICINE
                        medicineList.size == 1 -> InitialDestination.SINGLE_MEDICINE_DETAIL
                        else -> InitialDestination.MEDICINE_LIST
                    }
                }
            }
        }
    }
}
