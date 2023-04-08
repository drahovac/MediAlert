package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.InitialDestination
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: MedicineRepository) : BaseViewModel() {

    var nextDestination: InitialDestination? = null

    init {
        scope.launch {
            repository.allItems.collect {
                nextDestination = when {
                    it.isEmpty() -> InitialDestination.CREATE_MEDICINE
                    it.size == 1 -> InitialDestination.SINGLE_MEDICINE_DETAIL
                    else -> InitialDestination.MEDICINE_LIST
                }
            }
        }
    }

}