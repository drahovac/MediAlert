package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockMedicineRepository(
    override val allItems: Flow<List<Medicine>> = flow {
        delay(2000)
        emit(emptyList())
    }
) : MedicineRepository