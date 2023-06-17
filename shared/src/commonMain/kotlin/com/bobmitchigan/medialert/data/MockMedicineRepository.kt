package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.dateTimeNow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("MagicNumber")
class MockMedicineRepository(
    override val allItems: Flow<List<Medicine>> = flow {
        delay(2000)
        emit(emptyList())
    }
) : MedicineRepository {

    override suspend fun saveMedicine(medicine: Medicine) {
        delay(1000)
    }

    override suspend fun updateMedicine(medicine: Medicine) {
        delay(1000)
    }

    override fun getMedicineDetail(id: Int?): Flow<Medicine?> {
        return flow {
            delay(1000)
            Medicine(
                "Mock medicine",
                PREVIEW_BLISTER_PACKS,
                listOf(),
                dateTimeNow()
            )
        }
    }

    override suspend fun deleteMedicine(medicineId: Int) {
        delay(1000)
    }

    companion object {
        // Used in preview and in tests
        val PREVIEW_BLISTER_PACKS = BlisterPackAdapter.deserializeBlisterPacks(
            "F.F.N.L,E2022-04-03T03:06.E2023-12-25T03:06.F.F;F.F,F.F.F.F,F.F.F.F;" +
                    "F.F.N.L,E2022-04-03T04:06.E2023-12-25T03:06.F.F"
        )
    }
}
