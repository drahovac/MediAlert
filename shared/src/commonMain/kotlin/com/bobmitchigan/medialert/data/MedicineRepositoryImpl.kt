package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MedicineRepositoryImpl(private val database: Database) : MedicineRepository {

    override val allItems: Flow<List<Medicine>> = database.getAllMedicines().map { list ->
        list.map { Medicine(it.name, listOf(), listOf()) }
    }

    override suspend fun saveMedicine(medicine: Medicine) {
        withContext(Dispatchers.Default) {
            launch { database.insertMedicine(medicine) }
        }
    }
}