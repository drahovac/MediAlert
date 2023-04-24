package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.AppDatabase
import com.bobmitchigan.medialert.Medicine
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class Database(databaseDriverFactory: DatabaseDriver) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllMedicines()
        }
    }

    internal fun getAllMedicines(): Flow<List<com.bobmitchigan.medialert.Medicine>> =
        dbQuery.selectAllMedicines().asFlow().mapToList()

    internal fun insertMedicine(name: String, blisterPacks: String) {
        dbQuery.transaction {
            dbQuery.insertMedicine(name, blisterPacks)
        }
    }

    internal fun updateMedicine(id: Int, name: String, blisterPacks: String) {
        dbQuery.transaction {
            dbQuery.updateMedicine(name = name, blisterPacks = blisterPacks, id = id.toLong())
        }
    }

    internal fun getMedicineByIdOrFirst(id: Int?): Flow<Medicine?> {
        val query =
            id?.let { dbQuery.selectMedicineById(it.toLong()) } ?: dbQuery.selectAllMedicines()
        return query.asFlow().mapToList().map { it.firstOrNull() }
    }
}