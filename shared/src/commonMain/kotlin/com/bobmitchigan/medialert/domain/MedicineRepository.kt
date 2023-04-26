package com.bobmitchigan.medialert.domain

import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    val allItems: Flow<List<Medicine>>

    suspend fun saveMedicine(medicine: Medicine)

    suspend fun updateMedicine(medicine: Medicine)

    fun getMedicineDetail(id: Int?): Flow<Medicine?>

    suspend fun deleteMedicine(medicineId: Int)
}