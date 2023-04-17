package com.bobmitchigan.medialert.domain

import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    val allItems: Flow<List<Medicine>>

    suspend fun saveMedicine(medicine: Medicine)
}