package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.data.BlisterPackAdapter.deserializeBlisterPacks
import com.bobmitchigan.medialert.data.BlisterPackAdapter.serialize
import com.bobmitchigan.medialert.data.ScheduleAdapter.serialize
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MedicineRepositoryImpl(private val database: Database) : MedicineRepository {

    override val allItems: Flow<List<Medicine>> = database.getAllMedicines().map { list ->
        list.map {
            Medicine(
                it.name,
                deserializeBlisterPacks(it.blisterPacks),
                listOf(),
                it.id.toInt()
            )
        }
    }

    override suspend fun saveMedicine(medicine: Medicine) {
        withContext(Dispatchers.Default) {
            launch {
                database.insertMedicine(
                    medicine.name,
                    medicine.blisterPacks.serialize(),
                    medicine.schedule.serialize()
                )
            }
        }
    }

    override suspend fun updateMedicine(medicine: Medicine) {
        withContext(Dispatchers.Default) {
            launch {
                database.updateMedicine(
                    medicine.id!!,
                    medicine.name,
                    medicine.blisterPacks.serialize()
                )
            }
        }
    }

    override fun getMedicineDetail(id: Int?): Flow<Medicine?> {
        return database.getMedicineByIdOrFirst(id).map {
            it?.let {
                Medicine(
                    it.name,
                    deserializeBlisterPacks(it.blisterPacks),
                    listOf(),
                    it.id.toInt()
                )
            }
        }
    }

    override suspend fun deleteMedicine(medicineId: Int) {
        withContext(Dispatchers.Default) {
            launch {
                database.deleteMedicine(medicineId)
            }
        }
    }
}
