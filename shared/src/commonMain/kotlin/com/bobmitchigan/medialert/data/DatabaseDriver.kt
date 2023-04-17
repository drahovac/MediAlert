package com.bobmitchigan.medialert.data

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriver {
    fun createDriver(): SqlDriver
}
