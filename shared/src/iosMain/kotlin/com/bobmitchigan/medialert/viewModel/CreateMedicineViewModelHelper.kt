package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.MedicineRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateMedicineViewModelHelper : KoinComponent {
    val medicineRepository: MedicineRepository by inject()
}
