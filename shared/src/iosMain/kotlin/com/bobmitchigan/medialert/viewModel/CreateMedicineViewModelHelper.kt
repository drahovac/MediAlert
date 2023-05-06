package com.bobmitchigan.medialert.viewModel

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateMedicineViewModelHelper : KoinComponent {
    val viewModel: CreateMedicineViewModel by inject()
}
