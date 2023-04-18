package com.bobmitchigan.medialert.data

import com.bobmitchigan.medialert.AppDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

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
}