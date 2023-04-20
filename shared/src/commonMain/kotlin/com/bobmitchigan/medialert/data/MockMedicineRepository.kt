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
) : MedicineRepository {

    override suspend fun saveMedicine(medicine: Medicine) {
        delay(1000)
    }

    override suspend fun getMedicineDetail(id: Int?): Medicine {
        delay(1000)
        return Medicine("Mock medicine", PREVIEW_BLISTER_PACKS, listOf())
    }

    companion object {
        val PREVIEW_BLISTER_PACKS = BlisterPackAdapter.deserializeBlisterPacks(
            "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F;F.F,F.F.F.F,F.F.F.F;" +
                    "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F"
        )
    }
}